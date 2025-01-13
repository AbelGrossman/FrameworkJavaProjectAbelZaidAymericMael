package fr.pantheonsorbonne.ufr27.miage.camel;

import org.apache.camel.builder.RouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LobbyRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:createLobby")
            .to("jms:queue:lobbyQueue");
    }
}
