package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
import fr.pantheonsorbonne.ufr27.miage.dto.StatistiquesRequest;
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
                .log("User received from Creation Service")
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
                        statsParTheme = statistiquesService.getStatistiquesParTheme(playerId, theme);
                    }

                    // Préparation des informations pour la suite
                    exchange.getIn().setHeader("playerId", playerId);
                    exchange.getIn().setHeader("theme", theme);
                    exchange.getIn().setHeader("mmr", statsParTheme.getMmr());
                    exchange.getIn().setBody(statsParTheme);
                })
                .log("Statistiques par thème : ${body}")
                .marshal().json()
                .to("sjms2:M1.MatchmakingService"); // Vous pouvez définir une destination ou un traitement suivant.

    }
}
