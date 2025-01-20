package fr.pantheonsorbonne.ufr27.miage.gateway;

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
        Map<String, Object> request = mapper.readValue(body, Map.class);
        Long playerId = Long.valueOf((Integer) request.get("playerId"));

        try {
            gameService.validateNewRequest(playerId);
            gameService.createNewRequest(playerId);
            gameService.updateToMatchmaking(playerId);

            Map<String, Object> matchmakingRequest = Map.of(
                    "playerId", request.get("playerId"),
                    "theme", request.get("theme")
            );

            exchange.getMessage().setBody(mapper.writeValueAsString(matchmakingRequest));
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

        // Create request for questions service
        Map<String, Object> questionsRequest = Map.of(
                "theme", team.theme(),
                "difficulty", team.difficulty(),
                "teamId", team.teamId()
        );

        exchange.getMessage().setBody(mapper.writeValueAsString(questionsRequest));
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