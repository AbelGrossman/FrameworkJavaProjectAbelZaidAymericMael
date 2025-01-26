package fr.pantheonsorbonne.ufr27.miage.gateway;

import fr.pantheonsorbonne.ufr27.miage.dto.GameIsFinished;
import fr.pantheonsorbonne.ufr27.miage.dto.JoinGameRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.TeamResponseDto;
import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import fr.pantheonsorbonne.ufr27.miage.exception.JoinRequestNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.exception.PlayerNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.service.GameCreationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class GameCreationGateway {

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    GameCreationService gameService;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, TeamResponseDto> teamResponses = new ConcurrentHashMap<>();

    public void handlePlayerRequest(JoinGameRequest body, Exchange exchange) throws Exception {

            gameService.validateNewRequest(body.playerId());
            gameService.createNewRequest(body.playerId());
            gameService.updateToMatchmaking(body.playerId());
    }

    public void storeTeamAndForwardToQuestions(TeamResponseDto team, Exchange exchange) throws Exception {
        // Store the team response for later use
        teamResponses.put(team.id(), team);

        // Set headers for the questions service
        exchange.getMessage().setBody(null);
        exchange.getMessage().setHeader("theme", team.theme());
        exchange.getMessage().setHeader("difficulty", team.difficulty());
        exchange.getMessage().setHeader("id", team.id());
    }

    public void combineTeamAndQuestions(List<QuestionDTO> questionsResponse, Exchange exchange) throws Exception {
        // Get teamId from header

        String teamId = exchange.getMessage().getHeader("id", String.class);
        
        if (teamId == null) {
            exchange.getMessage().setBody(mapper.writeValueAsString(
                    Map.of("error", "Team ID not found in header")
            ));
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
            return;
        }

        exchange.getMessage().setHeader("teamId",teamId);
        // Get stored team data
        TeamResponseDto team = teamResponses.get(teamId);

        if (team != null) {
            // Combine team and questions data
            List<String> playerIdStrings = team.players().stream()
                                        .map(String::valueOf) // Convert each Long to String
                                        .toList();
            Map<String, Object> gameData = Map.of(
                    "playerIds", playerIdStrings,
                    "difficulty", team.difficulty(),
                    "category", team.theme(),
                    "totalQuestions", questionsResponse.size(),
                    "questions", questionsResponse
            );

            exchange.getMessage().setBody(gameData);
            team.players().forEach((playerId -> gameService.updateToInGame(playerId)));
        } else {
            exchange.getMessage().setBody(mapper.writeValueAsString(
                    Map.of("error", "Team data not found for ID: " + teamId)
            ));
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
        }
    }

    public void handleUpdateToFinished(GameIsFinished data, Exchange exchange) throws Exception {
        TeamResponseDto team = teamResponses.get(data.teamId());
        if (team != null) {
            team.players().forEach((playerId -> gameService.updateToFinished(playerId)));
            teamResponses.remove(data.teamId());

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