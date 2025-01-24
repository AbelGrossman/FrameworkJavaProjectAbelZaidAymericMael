package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
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
        // Gestion des exceptions pour les données invalides
        onException(Exception.class)
                .handled(true)
                .setHeader("success", simple("false"))
                .setBody(simple("Error processing statistiques: ${exception.message}"))
                .log("Erreur : ${exception.message}");

        // Route d'émission
        //from("direct:sendStatistiques")
        //       .marshal().json()
        //        .to("sjms2:" + jmsPrefix + "partieStatistiques")
        //        .log("Statistiques envoyées : ${body}");

        // Route de réception
        from("direct:statistiquesUpdate")
                .unmarshal().json(PartieDetails.class)
                .process(exchange -> {
                    PartieDetails partieDetails = exchange.getIn().getBody(PartieDetails.class);

                    if (partieDetails == null || partieDetails.getPlayerId() == null || partieDetails.getTheme() == null) {
                        throw new IllegalArgumentException("Missing required data (playerId, theme, or other fields)");
                    }

                    // Appel du service pour mettre à jour les statistiques
                    statistiquesService.updateStatistiques(
                            partieDetails.getPlayerId(),
                            partieDetails.getRangPartie(),
                            partieDetails.getScorePartie(),
                            partieDetails.getNbQuestions(),
                            partieDetails.getTheme(),
                            partieDetails.getTempsRepMoyen()
                    );
                })
                .log("Statistiques mises à jour pour l'utilisateur : ${body.playerId}");

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

                    // Vérification et création des statistiques si nécessaires
                    StatistiquesParTheme statsParTheme = statistiquesService.getStatistiquesParTheme(playerId, theme);
                    if (statsParTheme == null) {
                        log.info("No stats found for playerId = {} and theme = {}. Creating new stats.", playerId, theme);
                        statistiquesService.createStatistiqueUser(playerId, theme);
                    }

                    // Préparation des informations pour la prochaine route
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

                    // Récupération des statistiques par thème
                    StatistiquesParTheme statsParTheme = statistiquesService.getStatistiquesParTheme(playerId, theme);
                    if (statsParTheme == null) {
                        throw new IllegalArgumentException("Unable to retrieve stats for playerId = " + playerId + " and theme = " + theme);
                    }

                    // Création de l'objet StatistiquesResponse
                    StatistiquesResponse response = new StatistiquesResponse();
                    response.setPlayerId(playerId);
                    response.setTheme(theme);
                    response.setMmr(statsParTheme.getMmr());

                    // Préparation de la réponse
                    exchange.getIn().setBody(response);
                })
                .log("Statistiques Response : ${body}")
                .marshal().json()
                .log("Apres Jsonnisation: ${body}")
                .to("sjms2:M1.MatchmakingServiceMmr");


    }
}
