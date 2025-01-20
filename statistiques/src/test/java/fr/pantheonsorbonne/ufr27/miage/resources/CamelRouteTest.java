package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.camel.StatistiquesGateway;
import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CamelRouteTest {

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    StatistiquesGateway statistiquesGateway;

    @Inject
    StatistiquesDAO statistiquesDAO;

    @Test
    @Transactional
    void testStatistiquesUpdateRoute() {
        // Création de données de test
        PartieDetails testPartieDetails = new PartieDetails();
        testPartieDetails.setUserId("user1");
        testPartieDetails.setTheme("science");
        testPartieDetails.setNbQuestions(10);
        testPartieDetails.setNbBonnesReponses(7);
        testPartieDetails.setRangPartie(1);

        producerTemplate.sendBody("direct:statistiquesUpdate", testPartieDetails);

        // Vérification des mises à jour dans la base
        StatistiquesJoueur updatedStats = statistiquesDAO.getStatistiquesJoueur("user1");
        StatistiquesParTheme updatedThemeStats = statistiquesDAO.getStatistiquesParTheme("user1", "science");
        assertNotNull(updatedStats, "StatistiquesJoueur devrait être créé ou mis à jour.");
        assertEquals(1, updatedStats.getPartiesJouees(), "Le nombre de parties jouées devrait être 1.");
        assertEquals(1, updatedStats.getPartiesGagnees(), "Le nombre de parties gagnées devrait être 1.");
        assertEquals(7, updatedStats.getQuestionsBonnes(), "Le nombre de questions bonnes devrait être 7.");
        assertEquals(10, updatedStats.getQuestionsTotales(), "Le nombre total de questions devrait être 10.");
    }
}
