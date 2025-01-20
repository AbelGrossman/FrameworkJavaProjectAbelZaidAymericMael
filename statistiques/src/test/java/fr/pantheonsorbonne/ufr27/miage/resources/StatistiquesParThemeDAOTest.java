package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StatistiquesParThemeDAOTest {

    @Inject
    StatistiquesDAO statistiquesDAO;

    @Test
    @Order(1)
    @Transactional
    void testCreateOrUpdateStatistiquesParTheme() {
        String userId = "user1";
        String theme = "Histoire";
        int partiesJouees = 8;
        int partiesGagnees = 20;
        int bonnesReponses = 20;
        int questionsTotales = 20;

        // Création ou mise à jour des statistiques par thème
        StatistiquesParTheme statsTheme = statistiquesDAO.createOrUpdateStatistiquesParTheme(userId, theme, partiesJouees, partiesGagnees, bonnesReponses, questionsTotales);

        // Vérifications
        assertNotNull(statsTheme);
        assertEquals(userId, statsTheme.getUserId());
        assertEquals(theme, statsTheme.getTheme());
        assertEquals(partiesJouees, statsTheme.getPartiesJouees());
        assertEquals(bonnesReponses, statsTheme.getQuestionsBonnes());
    }

    @Test
    @Order(2)
    @Transactional
    void testGetStatistiquesParTheme() {
        String userId = "user1";
        String theme = "Histoire";

        // Préparation des données
        //statistiquesDAO.createOrUpdateStatistiquesParTheme(userId, theme, 8, 20,50,400);

        // Récupération des statistiques
        StatistiquesParTheme statsTheme = statistiquesDAO.getStatistiquesParTheme(userId, theme);

        // Vérifications
        assertNotNull(statsTheme);
        assertEquals("user1", statsTheme.getUserId());
        assertEquals("Histoire", statsTheme.getTheme());
        assertEquals(8, statsTheme.getPartiesJouees());
        assertEquals(50, statsTheme.getQuestionsBonnes());
    }
}
