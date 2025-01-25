package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
class CamelRoutesTest {

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    StatistiquesDAO statistiquesDAO;

//    @Test
//    void testStatistiquesUpdate_ValidData() {
//        // Arrange
//        PartieDetails validPartieDetails = new PartieDetails();
//        validPartieDetails.setPlayerId(1);
//        validPartieDetails.setRangPartie(1);
//        validPartieDetails.setScorePartie(18);
//        validPartieDetails.setNbQuestions(30);
//        validPartieDetails.setTheme("science");
//        validPartieDetails.setTempsRepMoyen(15);
//
//        // Act
//        producerTemplate.sendBody("direct:statistiquesUpdate", validPartieDetails);
//
//        // Assert
//        StatistiquesJoueur updatedStats = statistiquesDAO.getStatistiquesJoueur(1L);
//        StatistiquesParTheme updatedThemeStats = statistiquesDAO.getStatistiquesParTheme(1L, "science");
//
//        assertNotNull(updatedStats, "StatistiquesJoueur devrait être créé ou mis à jour.");
//        assertEquals(1, updatedStats.getNbPartie(), "Le nombre de parties devrait être 1.");
//        assertEquals(6, updatedStats.getScoreMoyen(), "Le score moyen devrait être 6.");
//        assertEquals(15.5, updatedStats.getTempsRepMoyen(), "Le temps de réponse moyen devrait être 15.5.");
//
//        assertNotNull(updatedThemeStats, "StatistiquesParTheme devrait être créé ou mis à jour.");
//        assertEquals("science", updatedThemeStats.getTheme(), "Le thème devrait être 'science'.");
//        assertEquals(1, updatedThemeStats.getNbPartie(), "Le nombre de parties pour ce thème devrait être 1.");
//    }

    @Test
    void testCreateStatistiqueUser() {

        // Préparation des données à envoyer à la route Camel sous forme de JSON
        String jsonRequest = "{ \"playerId\": 3, \"theme\": \"Math\" }";

        // Envoi du message JSON à la route Camel
        //ProducerTemplate producerTemplate = context.createProducerTemplate();
        Exchange exchange = producerTemplate.request("sjms2:M1.StatistiquesService", ex -> {
            ex.getIn().setBody(jsonRequest);
        });

        // Vérification que la réponse n'est pas null et contient les données attendues
        StatistiquesParTheme statsParTheme = exchange.getIn().getBody(StatistiquesParTheme.class);

        // Assert
        StatistiquesJoueur newupdatedStats = statistiquesDAO.getStatistiquesJoueur(3L);
        StatistiquesParTheme newstatsParTheme = statistiquesDAO.getStatistiquesParTheme(3L, "Math");

        assertNotNull(newupdatedStats, "Les statistiques par user devraient être créées.");
        assertNotNull(newstatsParTheme, "Les statistiques par thème devraient être créées.");
        assertNotNull(newstatsParTheme.getMmr(), "Le MMR ne devrait pas être null.");


//        // Arrange
//        PartieDetails validPartieDetails = new PartieDetails();
//        validPartieDetails.setPlayerId(1L);
//        validPartieDetails.setTheme("science");
//
//        // Act
//        // Envoi d'un message via Camel pour créer les statistiques pour l'utilisateur et le thème
//        producerTemplate.sendBody("sjms2:M1.StatistiquesService", validPartieDetails);
//
//        // Assert
//        StatistiquesJoueur createdStats = statistiquesDAO.getStatistiquesJoueur(validPartieDetails.getPlayerId());
//        StatistiquesParTheme createdThemeStats = statistiquesDAO.getStatistiquesParTheme(validPartieDetails.getPlayerId(), validPartieDetails.getTheme());
//
//        // Vérification des statistiques globales pour le joueur
//        assertNotNull(createdStats, "Les statistiques du joueur devraient être créées.");
//        assertEquals(0, createdStats.getNbPartie(), "Le nombre de parties pour le joueur devrait être 0.");
//        assertEquals(0, createdStats.getNbVictoires(), "Le nombre de victoires pour le joueur devrait être 0.");
//        assertEquals(0.0, createdStats.getScoreMoyen(), "Le score moyen pour le joueur devrait être 0.0.");
//        assertEquals(0.0, createdStats.getTempsRepMoyen(), "Le temps de réponse moyen pour le joueur devrait être 0.0.");
//        assertEquals(0, createdStats.getMmr(), "Le MMR pour le joueur devrait être 0.");
//
//        // Vérification des statistiques par thème pour le joueur
//        assertNotNull(createdThemeStats, "Les statistiques par thème pour le joueur devraient être créées.");
//        assertEquals(validPartieDetails.getTheme(), createdThemeStats.getTheme(), "Le thème devrait être 'science'.");
//        assertEquals(0, createdThemeStats.getNbPartie(), "Le nombre de parties pour ce thème devrait être 0.");
//        assertEquals(0, createdThemeStats.getNbVictoires(), "Le nombre de victoires pour ce thème devrait être 0.");
//        assertEquals(0.0, createdThemeStats.getScoreMoyen(), "Le score moyen pour ce thème devrait être 0.0.");
//        assertEquals(0.0, createdThemeStats.getTempsRepMoyen(), "Le temps de réponse moyen pour ce thème devrait être 0.0.");
    }

    @Test
    @Transactional
    public void testStatistiquesUpdateList() {
        String jsonInput = "[{\"playerId\":\"12345\",\"score\":25,\"gameId\":67890,\"averageResponseTime\":56,\"category\":\"General Knowledge\",\"totalQuestions\":25,\"rank\":1},{\"playerId\":\"67890\",\"score\":80,\"gameId\":67890,\"averageResponseTime\":72,\"category\":\"General Knowledge\",\"totalQuestions\":10,\"rank\":2}]";
        String jsonInput2 = "[{\"playerId\":\"12345\",\"score\":0,\"gameId\":67990,\"averageResponseTime\":44,\"category\":\"Feur\",\"totalQuestions\":25,\"rank\":5},{\"playerId\":\"69894\",\"score\":80,\"gameId\":67890,\"averageResponseTime\":72,\"category\":\"General Knowledge\",\"totalQuestions\":10,\"rank\":2}]";
        Exchange exchange = producerTemplate.request("direct:statistiquesUpdate", ex -> {
            ex.getIn().setBody(jsonInput);
        });

        // Vérifier que l'échange s'est bien passé
        assertNull(exchange.getException());

        // Vérifications pour le premier joueur
        StatistiquesJoueur statsJoueur1 = statistiquesDAO.getStatistiquesJoueur(12345L);
        assertNotNull(statsJoueur1);
        assertEquals(1, statsJoueur1.getNbPartie());
        //assertEquals(10.0, statsJoueur1.getScoreMoyen(), 0.01); // 100 / 10 * 10
        assertEquals(56, statsJoueur1.getTempsRepMoyen(), 0.01);

        StatistiquesParTheme themeStats1 = statistiquesDAO.getStatistiquesParTheme(12345L, "General Knowledge");
        assertNotNull(themeStats1);
        assertEquals(1, themeStats1.getNbPartie());
        //assertEquals(10.0, themeStats1.getScoreMoyen(), 0.01);

        // Vérifications pour le deuxième joueur
        StatistiquesJoueur statsJoueur2 = statistiquesDAO.getStatistiquesJoueur(67890L);
        assertNotNull(statsJoueur2);
        assertEquals(1, statsJoueur2.getNbPartie());
        //assertEquals(8.0, statsJoueur2.getScoreMoyen(), 0.01); // 80 / 10 * 10
        assertEquals(72, statsJoueur2.getTempsRepMoyen(), 0.01);

        StatistiquesParTheme themeStats2 = statistiquesDAO.getStatistiquesParTheme(67890L, "General Knowledge");
        assertNotNull(themeStats2);
        assertEquals(1, themeStats2.getNbPartie());
        //assertEquals(8.0, themeStats2.getScoreMoyen(), 0.01);

        Exchange exchange2 = producerTemplate.request("direct:statistiquesUpdate", ex -> {
            ex.getIn().setBody(jsonInput2);
        });
        // Vérifier que l'échange s'est bien passé
        assertNull(exchange.getException());
    }

    @Test
    @Transactional
    void testStatistiquesUpdate_MultipleUpdatesAndNewUser() {
        //String jsonP1U1 = "{\"playerId\": 1, \"scorePartie\": \"18\", \"tempsRepMoyen\": \"15\", \"theme\": \"science\", \"nbQuestions\": \"30\", \"rangPartie\": \"1\"}";
        String jsonP1U1 =  "{ \"score\": 18, \"average_response_time\": 15, \"id\": 1, \"game_id\": \"game123\", \"player_id\": \"1\", \"category\": \"science\", \"rank\": 1, \"total_questions\": 30 }";

        //String jsonP2U1 = "{\"playerId\": 1, \"scorePartie\": \"24\", \"tempsRepMoyen\": \"12\", \"theme\": \"science\", \"nbQuestions\": \"30\", \"rangPartie\": \"2\"}";
        //String jsonP1U2 = "{\"playerId\": 2, \"scorePartie\": \"21\", \"tempsRepMoyen\": \"14\", \"theme\": \"histoire\", \"nbQuestions\": \"30\", \"rangPartie\": \"1\"}";

        String jsonP2U1 = "{ \"score\": 24, \"average_response_time\": 12, \"id\": 2, \"game_id\": \"game456\", \"player_id\": \"1\", \"category\": \"science\", \"rank\": 2, \"total_questions\": 30 }";
        String jsonP1U2 = "{ \"score\": 21, \"average_response_time\": 14, \"id\": 3, \"game_id\": \"game789\", \"player_id\": \"2\", \"category\": \"histoire\", \"rank\": 1, \"total_questions\": 30 }";

        // Première partie pour user1
//        PartieDetails user1FirstGame = new PartieDetails();
//        user1FirstGame.setPlayerId(1L);
//        user1FirstGame.setRangPartie(1);
//        user1FirstGame.setScorePartie(18);
//        user1FirstGame.setNbQuestions(30);
//        user1FirstGame.setTheme("science");
//        user1FirstGame.setTempsRepMoyen(15.5);

        //producerTemplate.sendBody("direct:statistiquesUpdate", user1FirstGame);
        // Envoyer jsonP1U1
        Exchange exchangeP1U1 = producerTemplate.request("direct:statistiquesUpdate", ex -> {
            ex.getIn().setBody(jsonP1U1);
        });
        System.out.println("Response for P1U1: " + exchangeP1U1.getMessage().getBody());
        Exchange exchangeP2U1 = producerTemplate.request("direct:statistiquesUpdate", ex -> {
            ex.getIn().setBody(jsonP2U1);
        });
        System.out.println("Response for P2U1: " + exchangeP2U1.getMessage().getBody());
        Exchange exchangeP1U2 = producerTemplate.request("direct:statistiquesUpdate", ex -> {
            ex.getIn().setBody(jsonP1U2);
        });
        System.out.println("Response for P1U1: " + exchangeP1U2.getMessage().getBody());

        // Vérifications pour user1
        StatistiquesJoueur updatedStatsUser1 = statistiquesDAO.getStatistiquesJoueur(1L);
        StatistiquesParTheme updatedThemeStatsUser1 = statistiquesDAO.getStatistiquesParTheme(1L, "science");

        assertNotNull(updatedStatsUser1, "StatistiquesJoueur pour user1 devrait être mis à jour.");
        assertEquals(2, updatedStatsUser1.getNbPartie(), "Le nombre de parties pour user1 devrait être 2.");
        assertEquals(7, updatedStatsUser1.getScoreMoyen(), "Le score moyen pour user1 devrait être 7."); // (18 + 24) / (30 + 30)
        assertEquals(13, updatedStatsUser1.getTempsRepMoyen(), 0.01, "Le temps de réponse moyen pour user1 devrait être 13.");

        assertNotNull(updatedThemeStatsUser1, "StatistiquesParTheme pour user1 devrait être mis à jour.");
        assertEquals("science", updatedThemeStatsUser1.getTheme(), "Le thème pour user1 devrait être 'science'.");
        assertEquals(2, updatedThemeStatsUser1.getNbPartie(), "Le nombre de parties pour ce thème pour user1 devrait être 2.");

        // Vérifications pour user2
        StatistiquesJoueur updatedStatsUser2 = statistiquesDAO.getStatistiquesJoueur(2L);
        StatistiquesParTheme updatedThemeStatsUser2 = statistiquesDAO.getStatistiquesParTheme(2L, "histoire");

        assertNotNull(updatedStatsUser2, "StatistiquesJoueur pour user2 devrait être créé.");
        assertEquals(1, updatedStatsUser2.getNbPartie(), "Le nombre de parties pour user2 devrait être 1.");
        assertEquals(7, updatedStatsUser2.getScoreMoyen(), "Le score moyen pour user2 devrait être 7."); // 21 / 30
        assertEquals(14.0, updatedStatsUser2.getTempsRepMoyen(), "Le temps de réponse moyen pour user2 devrait être 14.0.");

        assertNotNull(updatedThemeStatsUser2, "StatistiquesParTheme pour user2 devrait être créé.");
        assertEquals("histoire", updatedThemeStatsUser2.getTheme(), "Le thème pour user2 devrait être 'histoire'.");
        assertEquals(1, updatedThemeStatsUser2.getNbPartie(), "Le nombre de parties pour ce thème pour user2 devrait être 1.");
    }


//    @Test
//    void testStatistiquesUpdate_MissingUserId() {
//        // Arrange
//        PartieDetails invalidPartieDetails = new PartieDetails();
//        invalidPartieDetails.setPlayerId(null); // Manquant pour déclencher l'exception
//        invalidPartieDetails.setTheme("science"); // Les autres champs sont valides
//
//        // Act & Assert
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            producerTemplate.sendBody("direct:statistiquesUpdate", invalidPartieDetails);
//        });
//
//        assertEquals("Missing required data (userId, theme, or other fields)", exception.getMessage());
//    }

//    @Test
//    void testStatistiquesUpdate_ExceptionHandling() {
//        // Arrange
//        PartieDetails partieDetails = new PartieDetails();
//        partieDetails.setPlayerId(123L);
//        partieDetails.setRangPartie(2);
//        partieDetails.setScorePartie(70);
//        partieDetails.setNbQuestions(10);
//        partieDetails.setTheme("history");
//        partieDetails.setTempsRepMoyen(12.0);
//
//        // Simuler une exception dans StatistiquesGateway
//        //statistiquesGateway.setThrowException(true);
//
//        // Act & Assert
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            producerTemplate.sendBody("direct:statistiquesUpdate", partieDetails);
//        });
//
//        assertEquals("Service unavailable", exception.getMessage());
//
//        // Remettre StatistiquesGateway dans son état normal
//        //statistiquesGateway.setThrowException(false);
//    }
}
