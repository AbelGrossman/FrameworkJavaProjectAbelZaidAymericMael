package fr.pantheonsorbonne.ufr27.miage.camel;

import org.apache.camel.builder.RouteBuilder;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.resources.RankedUserProcessor;
import fr.pantheonsorbonne.ufr27.miage.resources.UserProcessor;
import fr.pantheonsorbonne.ufr27.miage.service.UserEmitter;
import jakarta.inject.Inject;

public class MatchmakingRoute extends RouteBuilder {
    
    @Inject
    UserProcessor userProcessor;
    RankedUserProcessor rankedUserProcessor;

    @Override
    public void configure() {
        from("sjms2:M1.MatchmakingService")
            .log("User received from Creation Service")
            .unmarshal().json(User.class)
            .bean(userProcessor, "processNewUser");

        from("direct:userMmrRequest").marshal().json().to("sjms2:M1.StatisiquesService");

        from("sjms2:M1.MatchmakingService").unmarshal().json(User.class).bean(rankedUserProcessor, "processRankedUser");
        
        from("direct:newTeam").marshal().json().to("sjms2:M1.CreationPartieService");

    }


}
