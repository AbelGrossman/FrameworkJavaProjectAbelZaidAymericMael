package fr.pantheonsorbonne.ufr27.miage.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.TeamResponseDto;
import fr.pantheonsorbonne.ufr27.miage.gateway.GameCreationGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

@ApplicationScoped
public class GameCreationRoutes extends RouteBuilder {

    @Inject
    GameCreationGateway gateway;

    @Override
    public void configure() {
        // Route vers MatchMaking
        from("direct:CreationPartieService")
                .log("Nouvelle demande de joueur reçue : ${body}")
                .bean(gateway, "handlePlayerRequest")
                .marshal().json()
                .to("sjms2:M1.MatchmakingService");

        // Route de réception du MatchMaking vers Questions
        from("sjms2:M1.CreationPartieService")
                .log("Réponse du MatchMaking reçue : ${body}")
                .unmarshal().json(JsonLibrary.Jackson, TeamResponseDto.class)
                .bean(gateway, "storeTeamAndForwardToQuestions")
                .marshal().json()
                .to("sjms2:fetchQuestions");

        // Questions -> GameExecution
        from("sjms2:fetchQuestionsResponses")
                .log("Questions reçues : ${body}")
                .unmarshal().json()
                .bean(gateway, "combineTeamAndQuestions")
                .marshal().json()
                .to("sjms2:DerouleJeuService");
    }
}