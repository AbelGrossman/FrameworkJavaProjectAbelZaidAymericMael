package fr.pantheonsorbonne.ufr27.miage.gateway;


import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import fr.pantheonsorbonne.ufr27.miage.service.GameCreationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class GameCreationGateway {

    @Inject
    GameCreationService gameService;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, String> teamResponses = new ConcurrentHashMap<>();

    public void handlePlayerRequest(String body, Exchange exchange) throws Exception {
        Map<String, Object> request = mapper.readValue(body, Map.class);

        try {
            gameService.validateNewRequest(Long.valueOf((Integer) request.get("playerId")));

            // Envoie seulement playerId, theme et MMR au MatchMaking
            Map<String, Object> matchmakingRequest = Map.of(
                    "playerId", request.get("playerId"),
                    "theme", request.get("theme"),
                    "mmr", request.get("mmr")
            );

            exchange.getMessage().setBody(mapper.writeValueAsString(matchmakingRequest));
        } catch (DuplicateRequestException e) {
            exchange.getMessage().setBody(mapper.writeValueAsString(Map.of("error", e.getMessage())));
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 409);
        }
    }

    public void storeTeamAndForwardToQuestions(String body, Exchange exchange) throws Exception {
        Map<String, Object> matchmakingResponse = mapper.readValue(body, Map.class);
        String groupId = (String) matchmakingResponse.get("groupId");

        // Store complete matchmaking response
        teamResponses.put(groupId, body);

        // Forward theme and difficulty to Questions service
        Map<String, Object> questionsRequest = Map.of(
                "theme", matchmakingResponse.get("theme"),
                "difficulty", matchmakingResponse.get("averageLevel"),
                "groupId", groupId
        );

        exchange.getMessage().setBody(mapper.writeValueAsString(questionsRequest));
    }

    public void combineTeamAndQuestions(String body, Exchange exchange) throws Exception {
        Map<String, Object> questionsResponse = mapper.readValue(body, Map.class);
        String groupId = (String) questionsResponse.get("groupId");

        // Get stored team data
        String teamData = teamResponses.get(groupId);
        Map<String, Object> matchmakingData = mapper.readValue(teamData, Map.class);

        // Combine team and questions
        Map<String, Object> gameData = Map.of(
                "team", matchmakingData,
                "questions", questionsResponse.get("questions")
        );

        exchange.getMessage().setBody(mapper.writeValueAsString(gameData));
        teamResponses.remove(groupId);
    }
}