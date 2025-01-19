package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.gateway.GameCreationGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class GameCreationRoutes extends RouteBuilder {

    @Inject
    GameCreationGateway gateway;

    @Override
    public void configure() {
        // Route vers MatchMaking
        from("sjms2:M1.CreationPartieService")
                .log("Nouvelle demande de joueur reçue : ${body}")
                .bean(gateway, "handlePlayerRequest")
                .to("sjms2:M1.MatchmakingService");

        // Route de réception du MatchMaking vers Questions
        from("sjms2:M1.CreationPartieService")
                .log("Réponse du MatchMaking reçue : ${body}")
                .bean(gateway, "storeTeamAndForwardToQuestions")
                .to("sjms2:QuestionsService");

        // Questions -> GameExecution
        from("sjms2:QuestionsService")
                .log("Questions reçues : ${body}")
                .bean(gateway, "combineTeamAndQuestions")
                .to("sjms2:DerouleJeuService");
    }
}