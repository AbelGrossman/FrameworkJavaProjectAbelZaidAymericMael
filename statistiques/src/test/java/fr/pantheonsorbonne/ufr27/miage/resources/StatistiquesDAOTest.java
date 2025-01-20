package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class StatistiquesJoueurDAOTest {

    @Inject
    StatistiquesDAO statistiquesDAO;

    @Test
    @Transactional
    void testCreateOrUpdateStatistiquesJoueur() {
        String userId = "user1";
        int partiesJouees = 8;
        int partiesGagnees = 5;
        int bonnesReponses = 30;
        int questionsTotales = 21;

        // Création ou mise à jour des statistiques
        StatistiquesJoueur stats = statistiquesDAO.createOrUpdateStatistiquesJoueur(userId, partiesJouees, partiesGagnees, bonnesReponses, questionsTotales);

        // Vérifications
        assertNotNull(stats);
        assertEquals(userId, stats.getUserId());
        assertEquals(partiesJouees, stats.getPartiesJouees());
        assertEquals(partiesGagnees, stats.getPartiesGagnees());
        assertEquals(bonnesReponses, stats.getQuestionsBonnes());
    }

    @Test
    @Transactional
    void testGetStatistiquesJoueur() {
        String userId = "user1";

        // Préparation des données
        //statistiquesDAO.createOrUpdateStatistiquesJoueur(userId, 8, 5, 30,20);

        // Récupération des statistiques
        StatistiquesJoueur stats = statistiquesDAO.getStatistiquesJoueur(userId);

        // Vérifications
        assertNotNull(stats);
        assertEquals("user1", stats.getUserId());
        assertEquals(8, stats.getPartiesJouees());
        assertEquals(5, stats.getPartiesGagnees());
        assertEquals(30, stats.getQuestionsBonnes());
    }
}
