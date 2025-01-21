// package fr.pantheonsorbonne.ufr27.miage.camel;

// import org.apache.camel.builder.RouteBuilder;

// import fr.pantheonsorbonne.ufr27.miage.dto.MatchmakingRequest;
// import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
// import fr.pantheonsorbonne.ufr27.miage.resources.RankedUserProcessor;
// import fr.pantheonsorbonne.ufr27.miage.resources.UserProcessor;
// import jakarta.inject.Inject;

// public class MatchmakingRoute extends RouteBuilder {
    
//     @Inject
//     UserProcessor userProcessor;

//     @Inject
//     RankedUserProcessor rankedUserProcessor;

//     @Override
//     public void configure() {
//         from("sjms2:M1.MatchmakingService")
//             .log("User received from Creation Service")
//             .unmarshal().json(MatchmakingRequest.class)
//             .bean(userProcessor, "processNewUser");

//         from("direct:userMmrRequest").marshal().json().to("sjms2:M1.StatistiquesService");

//         from("sjms2:M1.MatchmakingService")
//         .unmarshal().json(UserWithMmr.class)
//         .bean("RankedUserProcessor", "processRankedUser");
            
//         from("direct:newTeam").marshal().json().to("sjms2:M1.CreationPartieService");

//     }


// }
