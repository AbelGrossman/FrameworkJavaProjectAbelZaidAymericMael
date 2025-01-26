package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.StatistiquesRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.StatistiquesResponse;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;
import fr.pantheonsorbonne.ufr27.miage.service.StatistiquesService;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.jmsPrefix")
    String jmsPrefix;

    @Inject
    StatistiquesService statistiquesService;

    @Inject
    StatistiquesRequest statistiquesRequest;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .setHeader("success", simple("false"))
                .setBody(simple("Error processing statistiques: ${exception.message}"))
                .log("Erreur : ${exception.message}");

        from("sjms2:statistiquesUpdate")
                .log("Received JSON: ${body}")
                .process(exchange -> {
                    String jsonInput = exchange.getIn().getBody(String.class);
                    statistiquesService.processAndUpdateStatistiques(jsonInput);
                })
                .log("Statistiques mises à jour");

        from("sjms2:M1.StatistiquesService")
                .log("Request received for user and theme verification")
                .unmarshal().json(StatistiquesRequest.class)
                .process(exchange -> {
                    StatistiquesRequest request = exchange.getIn().getBody(StatistiquesRequest.class);

                    if (request.getPlayerId() == null || request.getTheme() == null) {
                        throw new IllegalArgumentException("Missing required data (playerId or theme)");
                    }

                    Long playerId = request.getPlayerId();
                    String theme = request.getTheme();

                    StatistiquesParTheme statsParTheme = statistiquesService.getStatistiquesParTheme(playerId, theme);
                    if (statsParTheme == null) {
                        log.info("No stats found for playerId = {} and theme = {}. Creating new stats.", playerId, theme);
                        statistiquesService.createStatistiqueUser(playerId, theme);
                    }

                    exchange.getIn().setHeader("playerId", playerId);
                    exchange.getIn().setHeader("theme", theme);
                })
                .log("User and theme verified or created. Passing to the next route.")
                .to("direct:getMMR");

        from("direct:getMMR")
                .log("Processing MMR retrieval")
                .process(exchange -> {
                    Long playerId = exchange.getIn().getHeader("playerId", Long.class);
                    String theme = exchange.getIn().getHeader("theme", String.class);

                    StatistiquesParTheme statsParTheme = statistiquesService.getStatistiquesParTheme(playerId, theme);
                    if (statsParTheme == null) {
                        throw new IllegalArgumentException("Unable to retrieve stats for playerId = " + playerId + " and theme = " + theme);
                    }

                    StatistiquesResponse response = new StatistiquesResponse(playerId, theme, statsParTheme.getMmr());

                    exchange.getIn().setBody(response);
                })
                .log("Statistiques Response : ${body}")
                .marshal().json()
                .log("Apres Jsonnisation: ${body}")
                .to("sjms2:M1.MatchmakingServiceMmr");


    }
}
