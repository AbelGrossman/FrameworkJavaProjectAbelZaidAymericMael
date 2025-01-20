package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
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
                //.unmarshal().json(PartieDetails.class)
                .process(exchange -> {
                    PartieDetails partieDetails = exchange.getIn().getBody(PartieDetails.class);

                    if (partieDetails == null || partieDetails.getUserId() == null || partieDetails.getTheme() == null) {
                        throw new IllegalArgumentException("Missing required data (userId, theme, or other fields)");
                    }

                    // Appel du service pour mettre à jour les statistiques
                    statistiquesService.updateStatistiques(
                            partieDetails.getUserId(),
                            partieDetails.getRangPartie(),
                            partieDetails.getScorePartie(),
                            partieDetails.getNbQuestions(),
                            partieDetails.getTheme(),
                            partieDetails.getTempsRepMoyen()
                    );
                })
                .log("Statistiques mises à jour pour l'utilisateur : ${body.userId}");
    }
}
