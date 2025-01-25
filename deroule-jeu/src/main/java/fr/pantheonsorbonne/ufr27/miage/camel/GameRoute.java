package fr.pantheonsorbonne.ufr27.miage.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.GameInitializationRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.PlayerResultsRequest;
import fr.pantheonsorbonne.ufr27.miage.service.GameService;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRoute extends RouteBuilder {

    @Inject
    GameService gameService;

    @Override
    public void configure() throws Exception {
        //Route reçu par creation-partie (Zeid) et pour qu'on initialise la partie
        from("sjms2:startGame")
                .unmarshal().json(JsonLibrary.Jackson, GameInitializationRequest.class)
                .process(exchange -> {
                    String inputJson = exchange.getIn().getBody(String.class);
                    ObjectMapper mapper = new ObjectMapper();
                    GameInitializationRequest gameRequest = mapper.readValue(inputJson, GameInitializationRequest.class);

                    GameService gameService = new GameService();
                    Long gameId = gameService.initializeGame(
                            gameRequest.playerIds(),
                            gameRequest.category(),
                            gameRequest.difficulty(),
                            gameRequest.totalQuestions(),
                            gameRequest.questions()
                    );

                    exchange.getIn().setHeader("gameId", gameId);
                })
                .log("Jeu initialisé avec l'ID : ${header.gameId}")
                .to("direct:endGame");

        // Route pour la fin de la partie
        /*
        Le soucis étant que, il faut que durant la partie, a travers GameService l'interface ou autre,
        Je ne sais pas comment cela fonctionne, mais le Camel doit savoir que la partie est finie
         */

        from("direct:endGame")
                .log("Partie terminée")
                .log("Envoie des statistiques")
                .log("Réutilisation de gameId : ${header.gameId}")
                .process(exchange -> {
                    //Logique pour qu'on envoie a Mael
                    Long gameId = exchange.getIn().getHeader("gameId", Long.class);
                    List<PlayerResultsRequest> playerResults = gameService.getPlayerResultsDTO(gameId);
                    Map<String, Object> statisticsData = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    String statisticsJson = mapper.writeValueAsString(statisticsData);
                    exchange.setProperty("statistics", statisticsJson);

                    //Logique pour qu'on envoie a Zeid
                    Map<String, Object> creationPartiePayload = new HashMap<>();
                    // creationPartiePayload.put("teamId", INT); // Je n'ai pas trouvé ou il apparaît
                    creationPartiePayload.put("isOver", 1); // On peut le récupérer autrement ?
                    String creationPartieJson = mapper.writeValueAsString(creationPartiePayload);
                    exchange.setProperty("creationPartie", creationPartieJson);

                    exchange.getIn().setBody(playerResults);
                })
                .multicast()
                    .to("direct:sendToStatistics", "direct:sendToCreationPartie")
                .end();

        // Route pour envoyé vers Maêl (statistiques)
        /*
        Le seul problème étant : comment on peut récupérer le "gameId" pour qu'il reconnaisse la bonne partie ?
        problème réglé ! On transmet le gameId dans les headers
         */
        from("direct:sendToStatistics")
                .process(exchange -> {
                    String statisticsJson = exchange.getProperty("statistics", String.class);
                    exchange.getIn().setBody(statisticsJson);
                })
                .log("Envoi à statistiques : ${body}")
                .marshal().json()
                .to("sjms2:statistiques") ;// URL de "statistiques"

        from("direct:sendToCreationPartie")
                .process(exchange -> {
                    String creationPartieJson = exchange.getProperty("creationPartiePayload", String.class);
                    exchange.getIn().setBody(creationPartieJson);
                })
                .log("Envoi à creation-partie : ${body}")
                .marshal().json()
                .to("sjms2:creation-partie");
        }

}

// BROUILLON DE LA ROUTE

 /*  --> reçoit de creation-partie : "{
   "playerIds": ["1", "2", "3", "4", "5", "6"],
   "difficulty": "easy",
   "category": "General Knowledge",
   "totalQuestions": 1,
   "questions": [
     {
       "type": "multiple",
       "difficulty": "easy",
       "category": "General Knowledge",
       "question": "What is the capital of France?",
       "correct_answer": "Paris",
       "incorrect_answers": ["London", "Berlin", "Madrid"]
     }
   ]
 }"


 *Quand la partie est finie (isOver=1)

   Envoie à statistiques : "{


   "playerResults": [
     {
       "playerId": "1",
       "score": 1,
       "averageResponseTime": 1000,
       "rank": 1,
          "gameId": 1,
      "category": "General     Knowledge",
         "totalQuestions": 1
     },
     {
       "playerId": "2",
       "score": 4,
       "averageResponseTime": 1333,
       "rank": 4
     }                                          --> etc pour chaque joueur de la partie
   ]


  Envoie à creation-partie : "{
   "teamId": 1,
   "isOver": 1
 }"
*/


