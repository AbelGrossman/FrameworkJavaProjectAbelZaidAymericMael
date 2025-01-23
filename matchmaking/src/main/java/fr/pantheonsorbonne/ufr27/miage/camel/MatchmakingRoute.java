 package fr.pantheonsorbonne.ufr27.miage.camel;

 import org.apache.camel.builder.RouteBuilder;

 import fr.pantheonsorbonne.ufr27.miage.dto.UserWithoutMmrRequest;
 import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
 import fr.pantheonsorbonne.ufr27.miage.resources.RankedUserProcessor;
 import fr.pantheonsorbonne.ufr27.miage.resources.UserProcessor;
 import jakarta.inject.Inject;
 import org.apache.camel.model.dataformat.JsonLibrary;

 public class MatchmakingRoute extends RouteBuilder {

     @Inject
     UserProcessor userProcessor;

     @Inject
     RankedUserProcessor rankedUserProcessor;

     @Override
     public void configure() {
         from("sjms2:M1.MatchmakingService")
             .log("User received from Creation Service : ${body}")
             .unmarshal().json(JsonLibrary.Jackson,UserWithoutMmrRequest.class)
             .bean("UserProcessor", "processNewUser");

         from("direct:userMmrRequest").log("User send to Statistique : ${body}").marshal().json().to("sjms2:M1.StatistiquesService");

         from("sjms2:M1.MatchmakingServiceMmr").log("User received from Statistique : ${body}")
         .unmarshal().json(UserWithMmr.class)
         .bean("RankedUserProcessor", "processRankedUser");

         from("direct:newTeam").marshal().json().log("User sent to creation : ${body}").to("sjms2:M1.CreationPartieService");

     }


 }
