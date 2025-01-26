 package fr.pantheonsorbonne.ufr27.miage.camel;

 import org.apache.camel.builder.RouteBuilder;

 import fr.pantheonsorbonne.ufr27.miage.dto.UserWithoutMmrRequest;
import fr.pantheonsorbonne.ufr27.miage.gateway.RankedUserGateway;
import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import jakarta.inject.Inject;

 public class MatchmakingRoute extends RouteBuilder {

     @Inject
     RankedUserGateway rankedUserProcessor;

     @Override
     public void configure() {
         from("sjms2:M1.MatchmakingService")
             .log("User received from Creation Service : ${body}")
             .unmarshal().json(UserWithoutMmrRequest.class)
             .to("direct:userMmrRequest");

         from("direct:userMmrRequest").log("User send to Statistique : ${body}").marshal().json().to("sjms2:M1.StatistiquesService");

         from("sjms2:M1.MatchmakingServiceMmr").log("User received from Statistique : ${body}")
         .unmarshal().json(UserWithMmr.class)
         .bean("RankedUserGateway", "processRankedUser");

         from("direct:newTeam").marshal().json().log("Team sent to creation : ${body}").to("sjms2:M1.CreationPartieService");

         from("sjms2:M1.CancelMatchmakingService")
            .log("User canceled matchmaking : ${body}")
            .unmarshal().json(Long.class)
            .bean("CanceledMatchmakingGateway", "processCanceledMatchmaking");

        }

 }
