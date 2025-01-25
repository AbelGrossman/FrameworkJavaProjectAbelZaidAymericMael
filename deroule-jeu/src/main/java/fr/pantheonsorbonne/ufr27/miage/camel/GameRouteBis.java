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
        from("sjms2:startGame")
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
                    exchange.setProperty("statistics", mapper.writeValueAsString(statisticsData));

                    // Creation-partie data
                    String teamId = gameService.getTeamIdByGameId(gameId);
                    Map<String, Object> creationPartieData = Map.of(
                            "teamId", teamId,
                            "isOver", 1);
                    exchange.setProperty("creationPartie", mapper.writeValueAsString(creationPartieData));
                    logger.info("Preparing to send game completion data for game ID: {}", gameId);
                })
                .multicast()
                .to("direct:sendToStatistics", "direct:sendToCreationPartie");

        from("direct:sendToStatistics")
                .process(exchange -> {
                    String statistics = exchange.getProperty("statistics", String.class);
                    exchange.getIn().setBody(statistics);
                    logger.info("Sending statistics data: {}", statistics);
                })
                .to("sjms2:statistiques");

        from("direct:sendToCreationPartie")
                .process(exchange -> {
                    String creationPartie = exchange.getProperty("creationPartie", String.class);
                    exchange.getIn().setBody(creationPartie);
                    logger.info("Sending creation-partie data: {}", creationPartie);
                })
                .to("sjms2:creation-partie");
    }
}