package fr.pantheonsorbonne.ufr27.miage.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.gateway.GameCreationGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

@ApplicationScoped
public class GameCreationRoutes extends RouteBuilder {

    @Inject
    GameCreationGateway gateway;

    @Override
    public void configure() {
        // Route vers MatchMaking
        from("sjms2:M1.CreationPartieService")
                .log("Nouvelle demande de joueur reçue : ${body}")
                .process(exchange -> {
                    exchange.getMessage().setHeader("theme", "science");
                    exchange.getMessage().setHeader("difficulty", "medium");
                })
                .log("Envoi du message de test au service Questions avec category: ${header.theme}, difficulty: ${header.difficulty}")
                .to("sjms2:fetchQuestions");
                /*.bean(gateway, "handlePlayerRequest")
                .to("sjms2:M1.MatchmakingService");*/



        // Route de réception du MatchMaking vers Questions
        from("sjms2:M1.CreationPartieService")
                .log("Réponse du MatchMaking reçue : ${body}")
                .bean(gateway, "storeTeamAndForwardToQuestions")
                .marshal().json()
                .to("sjms2:fetchQuestions");

        // Questions -> GameExecution
        from("sjms2:fetchQuestionsResponses")
                .log("Questions reçues : ${body}")
                .bean(gateway, "combineTeamAndQuestions")
                .to("sjms2:DerouleJeuService");
    }
}