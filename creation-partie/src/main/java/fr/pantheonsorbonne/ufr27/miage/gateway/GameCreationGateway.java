package fr.pantheonsorbonne.ufr27.miage.gateway;

import fr.pantheonsorbonne.ufr27.miage.dto.JoinGameRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.TeamResponseDto;
import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import fr.pantheonsorbonne.ufr27.miage.service.GameCreationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class GameCreationGateway {

    @Inject
    GameCreationService gameService;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, TeamResponseDto> teamResponses = new ConcurrentHashMap<>();

    public void handlePlayerRequest(String body, Exchange exchange) throws Exception {
        JoinGameRequest request = mapper.readValue(body, JoinGameRequest.class);
        try {
            gameService.validateNewRequest(request.playerId());
            gameService.createNewRequest(request.playerId());
            gameService.updateToMatchmaking(request.playerId());

            exchange.getMessage().setBody(request);
        } catch (DuplicateRequestException e) {
            exchange.getMessage().setBody(mapper.writeValueAsString(Map.of("error", e.getMessage())));
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 409);
        }
    }

    public void storeTeamAndForwardToQuestions(TeamResponseDto team, Exchange exchange) throws Exception {
        // Store the team response for later use
        teamResponses.put(team.teamId(), team);

        // Set headers for the questions service
        exchange.getMessage().setHeader("category", team.theme());
        exchange.getMessage().setHeader("difficulty", team.difficulty());
        exchange.getMessage().setHeader("teamId", team.teamId());
    }

    @SuppressWarnings("unchecked")
    public void combineTeamAndQuestions(Map<String, Object> questionsResponse, Exchange exchange) throws Exception {
        // Get teamId from header
        String teamId = exchange.getMessage().getHeader("teamId", String.class);

        if (teamId == null) {
            exchange.getMessage().setBody(mapper.writeValueAsString(
                    Map.of("error", "Team ID not found in header")
            ));
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
            return;
        }

        // Get stored team data
        TeamResponseDto team = teamResponses.get(teamId);

        if (team != null) {
            // Combine team and questions data
            Map<String, Object> gameData = Map.of(
                    "team", team,
                    "questions", questionsResponse.get("questions")
            );

            exchange.getMessage().setBody(mapper.writeValueAsString(gameData));
            teamResponses.remove(teamId);
        } else {
            exchange.getMessage().setBody(mapper.writeValueAsString(
                    Map.of("error", "Team data not found for ID: " + teamId)
            ));
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
        }
    }
}