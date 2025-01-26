package fr.pantheonsorbonne.ufr27.miage.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.GameInitializationRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.PlayerResultsRequest;
import fr.pantheonsorbonne.ufr27.miage.service.GameService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GameRouteBis extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(GameRouteBis.class);

    @Inject
    GameService gameService;

    @Override
    public void configure() throws Exception {
        from("sjms2:DerouleJeuService")
                .unmarshal().json(JsonLibrary.Jackson, GameInitializationRequest.class)
                .process(exchange -> {
                    GameInitializationRequest gameRequest = exchange.getIn().getBody(GameInitializationRequest.class);
                    String teamId = exchange.getIn().getHeader("teamId", String.class);
                    if (teamId == null) {
                        throw new IllegalArgumentException("teamId header is required");
                    }
                    Long gameId = gameService.initializeGame(
                            gameRequest.playerIds(),
                            gameRequest.category(),
                            gameRequest.difficulty(),
                            gameRequest.totalQuestions(),
                            gameRequest.questions(),
                            teamId);
                    exchange.getIn().setHeader("gameId", gameId);
                    logger.info("Game initialized with ID: {}", gameId);
                })
                .log("Game initialized with ID: ${header.gameId}");

        from("direct:endGame")
                .process(exchange -> {
                    Long gameId = exchange.getIn().getHeader("gameId", Long.class);
                    List<PlayerResultsRequest> playerResults = exchange.getIn().getBody(List.class);

                    ObjectMapper mapper = new ObjectMapper();

                    // Statistics data
                    Map<String, Object> statisticsData = Map.of("playerResults", playerResults);
                    exchange.setProperty("statistics", statisticsData);

                    // Creation-partie data
                    String teamId = gameService.getTeamIdByGameId(gameId);
                    Map<String, Object> creationPartieData = Map.of(
                            "teamId", teamId,
                            "isOver", 1);
                    exchange.setProperty("creationPartie", creationPartieData);
                    logger.info("Preparing to send game completion data for game ID: {}", gameId);
                })
                .multicast()
                .to("direct:sendToStatistics", "direct:sendToCreationPartie");

        from("direct:sendToStatistics")
        .process(exchange -> {
            Map<String, Object> statisticsData = exchange.getProperty("statistics", Map.class);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(statisticsData.get("playerResults")); // Extract and serialize playerResults
            exchange.getIn().setBody(json);
            logger.info("Sending statistics data: {}", json);
        })
        .to("sjms2:statistiquesUpdate");
            

        from("direct:sendToCreationPartie")
        .process(exchange -> {
            Map<String, Object> creationPartie = exchange.getProperty("creationPartie", Map.class);

            // Serialize the creationPartie map into JSON using Jackson
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(creationPartie);

            // Set the serialized JSON as the message body
            exchange.getIn().setBody(json);
            logger.info("Sending creation-partie data: {}", json);
        })
        .to("sjms2:DerouleJeuServiceFinished");

    }
}