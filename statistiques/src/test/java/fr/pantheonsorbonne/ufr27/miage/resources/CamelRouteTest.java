package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
import fr.pantheonsorbonne.ufr27.miage.camel.StatistiquesGateway;
import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
class CamelRoutesTest {

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    StatistiquesGateway statistiquesGateway;

    @Inject
    StatistiquesDAO statistiquesDAO;

    @Test
    void testStatistiquesUpdate_ValidData() {
        // Arrange
        PartieDetails validPartieDetails = new PartieDetails();
        validPartieDetails.setUserId("user1");
        validPartieDetails.setRangPartie(1);
        validPartieDetails.setScorePartie(18);
        validPartieDetails.setNbQuestions(30);
        validPartieDetails.setTheme("science");
        validPartieDetails.setTempsRepMoyen(15.5);

        // Act
        producerTemplate.sendBody("direct:statistiquesUpdate", validPartieDetails);

        // Assert
        StatistiquesJoueur updatedStats = statistiquesDAO.getStatistiquesJoueur("user1");
        StatistiquesParTheme updatedThemeStats = statistiquesDAO.getStatistiquesParTheme("user1", "science");

        assertNotNull(updatedStats, "StatistiquesJoueur devrait être créé ou mis à jour.");
        assertEquals(1, updatedStats.getNbPartie(), "Le nombre de parties devrait être 1.");
        assertEquals(6, updatedStats.getScoreMoyen(), "Le score moyen devrait être 6.");
        assertEquals(15.5, updatedStats.getTempsRepMoyen(), "Le temps de réponse moyen devrait être 15.5.");

        assertNotNull(updatedThemeStats, "StatistiquesParTheme devrait être créé ou mis à jour.");
        assertEquals("science", updatedThemeStats.getTheme(), "Le thème devrait être 'science'.");
        assertEquals(1, updatedThemeStats.getNbPartie(), "Le nombre de parties pour ce thème devrait être 1.");
    }

    @Test
    @Transactional
    void testStatistiquesUpdate_MultipleUpdatesAndNewUser() {
        // Première partie pour user1
        PartieDetails user1FirstGame = new PartieDetails();
        user1FirstGame.setUserId("user1");
        user1FirstGame.setRangPartie(1);
        user1FirstGame.setScorePartie(18);
        user1FirstGame.setNbQuestions(30);
        user1FirstGame.setTheme("science");
        user1FirstGame.setTempsRepMoyen(15.5);

        producerTemplate.sendBody("direct:statistiquesUpdate", user1FirstGame);

        // Deuxième partie pour user1
        PartieDetails user1SecondGame = new PartieDetails();
        user1SecondGame.setUserId("user1");
        user1SecondGame.setRangPartie(2);
        user1SecondGame.setScorePartie(24);
        user1SecondGame.setNbQuestions(30);
        user1SecondGame.setTheme("science");
        user1SecondGame.setTempsRepMoyen(12.0);

        producerTemplate.sendBody("direct:statistiquesUpdate", user1SecondGame);

        // Partie pour user2
        PartieDetails user2FirstGame = new PartieDetails();
        user2FirstGame.setUserId("user2");
        user2FirstGame.setRangPartie(1);
        user2FirstGame.setScorePartie(21);
        user2FirstGame.setNbQuestions(30);
        user2FirstGame.setTheme("histoire");
        user2FirstGame.setTempsRepMoyen(14.0);

        producerTemplate.sendBody("direct:statistiquesUpdate", user2FirstGame);

        // Vérifications pour user1
        StatistiquesJoueur updatedStatsUser1 = statistiquesDAO.getStatistiquesJoueur("user1");
        StatistiquesParTheme updatedThemeStatsUser1 = statistiquesDAO.getStatistiquesParTheme("user1", "science");

        assertNotNull(updatedStatsUser1, "StatistiquesJoueur pour user1 devrait être mis à jour.");
        assertEquals(2, updatedStatsUser1.getNbPartie(), "Le nombre de parties pour user1 devrait être 2.");
        assertEquals(7, updatedStatsUser1.getScoreMoyen(), "Le score moyen pour user1 devrait être 7."); // (18 + 24) / (30 + 30)
        assertEquals(13.75, updatedStatsUser1.getTempsRepMoyen(), 0.01, "Le temps de réponse moyen pour user1 devrait être 13.75.");

        assertNotNull(updatedThemeStatsUser1, "StatistiquesParTheme pour user1 devrait être mis à jour.");
        assertEquals("science", updatedThemeStatsUser1.getTheme(), "Le thème pour user1 devrait être 'science'.");
        assertEquals(2, updatedThemeStatsUser1.getNbPartie(), "Le nombre de parties pour ce thème pour user1 devrait être 2.");

        // Vérifications pour user2
        StatistiquesJoueur updatedStatsUser2 = statistiquesDAO.getStatistiquesJoueur("user2");
        StatistiquesParTheme updatedThemeStatsUser2 = statistiquesDAO.getStatistiquesParTheme("user2", "histoire");

        assertNotNull(updatedStatsUser2, "StatistiquesJoueur pour user2 devrait être créé.");
        assertEquals(1, updatedStatsUser2.getNbPartie(), "Le nombre de parties pour user2 devrait être 1.");
        assertEquals(7, updatedStatsUser2.getScoreMoyen(), "Le score moyen pour user2 devrait être 7."); // 21 / 30
        assertEquals(14.0, updatedStatsUser2.getTempsRepMoyen(), "Le temps de réponse moyen pour user2 devrait être 14.0.");

        assertNotNull(updatedThemeStatsUser2, "StatistiquesParTheme pour user2 devrait être créé.");
        assertEquals("histoire", updatedThemeStatsUser2.getTheme(), "Le thème pour user2 devrait être 'histoire'.");
        assertEquals(1, updatedThemeStatsUser2.getNbPartie(), "Le nombre de parties pour ce thème pour user2 devrait être 1.");
    }


    @Test
    void testStatistiquesUpdate_MissingUserId() {
        // Arrange
        PartieDetails invalidPartieDetails = new PartieDetails();
        invalidPartieDetails.setUserId(null); // Manquant pour déclencher l'exception
        invalidPartieDetails.setTheme("science"); // Les autres champs sont valides

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            producerTemplate.sendBody("direct:statistiquesUpdate", invalidPartieDetails);
        });

        assertEquals("Missing required data (userId, theme, or other fields)", exception.getMessage());
    }

    @Test
    void testStatistiquesUpdate_ExceptionHandling() {
        // Arrange
        PartieDetails partieDetails = new PartieDetails();
        partieDetails.setUserId("user123");
        partieDetails.setRangPartie(2);
        partieDetails.setScorePartie(70);
        partieDetails.setNbQuestions(10);
        partieDetails.setTheme("history");
        partieDetails.setTempsRepMoyen(12.0);

        // Simuler une exception dans StatistiquesGateway
        //statistiquesGateway.setThrowException(true);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            producerTemplate.sendBody("direct:statistiquesUpdate", partieDetails);
        });

        assertEquals("Service unavailable", exception.getMessage());

        // Remettre StatistiquesGateway dans son état normal
        //statistiquesGateway.setThrowException(false);
    }
}
