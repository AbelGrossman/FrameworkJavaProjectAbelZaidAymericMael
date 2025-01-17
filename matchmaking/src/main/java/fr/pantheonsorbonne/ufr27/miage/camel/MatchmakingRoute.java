package fr.pantheonsorbonne.ufr27.miage.camel;

import org.apache.camel.builder.RouteBuilder;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.resources.UserProcessor;
import jakarta.inject.Inject;

public class MatchmakingRoute extends RouteBuilder {
    
    @Inject
    UserProcessor userProcessor;

    @Override
    public void configure() {
        from("sjms2:M1.MatchmakingService")
            .log("User received from Creation Service")
            .unmarshal().json(User.class)
            .to("direct:newUser")
            .bean(userProcessor, "processNewUser");

        from("direct:newTeam").marshal().json().to("sjms2:M1.CreationPartieService");
    }


}
