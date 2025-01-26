package fr.pantheonsorbonne.ufr27.miage.gateway;

import fr.pantheonsorbonne.ufr27.miage.dao.TeamResponseDao;
import fr.pantheonsorbonne.ufr27.miage.dto.GameIsFinished;
import fr.pantheonsorbonne.ufr27.miage.dto.JoinGameRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.TeamResponseDto;
import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import fr.pantheonsorbonne.ufr27.miage.exception.JoinRequestNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.exception.PlayerNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.model.TeamResponse;
import fr.pantheonsorbonne.ufr27.miage.service.GameCreationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class GameCreationGateway {

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    GameCreationService gameService;

    @Inject
    TeamResponseDao teamResponseDao;

    private final ObjectMapper mapper = new ObjectMapper();

    public void handlePlayerRequest(JoinGameRequest body, Exchange exchange) throws Exception {

            gameService.validateNewRequest(body.playerId());
            gameService.createNewRequest(body.playerId());
            gameService.updateToMatchmaking(body.playerId());
    }

    public void storeTeamAndForwardToQuestions(TeamResponseDto team, Exchange exchange) {
        teamResponseDao.persist(new TeamResponse(team));
        exchange.getMessage().setHeader("theme", team.theme());
        exchange.getMessage().setHeader("difficulty", team.difficulty());
        exchange.getMessage().setHeader("id", team.id());
    }

    public void combineTeamAndQuestions(List<QuestionDTO> questionsResponse, Exchange exchange) {
        String teamId = exchange.getMessage().getHeader("id", String.class);
        Optional<TeamResponse> teamOpt = teamResponseDao.findById(teamId);

        if (teamOpt.isPresent()) {
            TeamResponse team = teamOpt.get();
            List<String> playerIds = team.getPlayers().stream()
                    .map(String::valueOf)
                    .toList();

            Map<String, Object> gameData = Map.of(
                    "playerIds", playerIds,
                    "difficulty", team.getDifficulty(),
                    "category", team.getTheme(),
                    "totalQuestions", questionsResponse.size(),
                    "questions", questionsResponse
            );

            exchange.getMessage().setBody(gameData);
            team.getPlayers().forEach(playerId -> gameService.updateToInGame(playerId));
        }
    }

    public void handleUpdateToFinished(GameIsFinished data, Exchange exchange) {
        Optional<TeamResponse> teamOpt = teamResponseDao.findById(data.teamId());
        if (teamOpt.isPresent()) {
            TeamResponse team = teamOpt.get();
            team.getPlayers().forEach(playerId -> gameService.updateToFinished(playerId));
        }
    }
    public void publishJoinRequest(JoinGameRequest request) throws Exception {
        try {
            gameService.validateNewRequest(request.playerId());
            producerTemplate.sendBody(
                    "direct:CreationPartieService",
                    mapper.writeValueAsString(request)
            );
        } catch (DuplicateRequestException | PlayerNotFoundException e) {
            throw e;
        }
    }



    public void publishCancelRequest(long playerId) throws  Exception {
        try {
            gameService.validateCancelRequest(playerId);
            producerTemplate.sendBody(
                    "sjms2:M1.CancelMatchmakingService",
                    mapper.writeValueAsString(playerId)
            );
            gameService.cancelRequest(playerId);
        } catch (JoinRequestNotFoundException | PlayerNotFoundException e) {
            throw e;
        }
    }

}