package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.JoinGameRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.TeamResponseDto;
import fr.pantheonsorbonne.ufr27.miage.gateway.GameCreationGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.List;


@ApplicationScoped
public class GameCreationRoutes extends RouteBuilder {

    @Inject
    GameCreationGateway gateway;

    @Override
    public void configure() {
        // Direct -> MatchMaking
        from("direct:CreationPartieService")
                .log("Nouvelle demande de joueur reçue : ${body}").unmarshal().json(JoinGameRequest.class)
                .bean(gateway, "handlePlayerRequest")
                .marshal().json()
                .to("sjms2:M1.MatchmakingService");

        // MatchMaking -> Questions
        from("sjms2:M1.CreationPartieService")
                .log("Réponse du MatchMaking reçue : ${body}")
                .unmarshal().json(JsonLibrary.Jackson, TeamResponseDto.class)
                .bean(gateway, "storeTeamAndForwardToQuestions")
                .log("Current headers: ${headers}")
                .to("sjms2:fetchQuestions");

        // Questions -> GameExecution
        from("sjms2:fetchQuestionsResponses")
                .log("Questions reçues : ${body}")
                .unmarshal().json(JsonLibrary.Jackson, List.class)
                .bean(gateway, "combineTeamAndQuestions")
                .marshal().json()
                .log("Final Data sent : ${body}")
                .to("sjms2:DerouleJeuService");
    }
}