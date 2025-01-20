package fr.pantheonsorbonne.ufr27.miage.service;


import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
class APITest {

    @Inject
    CamelContext camelContext;

    @Inject
    QuestionServicesImpl questionServices;

    @Test
    void testResourcesAPI() throws Exception {
        String[] categories = {"sport_and_leisure", "society_and_culture", "history", "geography", "science", "arts_and_literature", "general_knowledge", "film_and_tv"};
        String[] difficulties = {"easy", "medium", "hard"};

        for (String category : categories) {
            for (String difficulty : difficulties) {
                Exchange exchange = camelContext.createProducerTemplate().request("direct:fetchQuestions", e -> {
                    e.getIn().setHeader("category", category);
                    e.getIn().setHeader("difficulty", difficulty);
                });

                List<QuestionDTO> result = exchange.getMessage().getBody(List.class);
                System.out.println("Category: " + category + " Difficulty: " + difficulty + " Size: " + result.size());

                if (result.size() < 20) {
                    System.out.println("Category: " + category + " Difficulty: " + difficulty + " Size: " + result.size());
                }

                assertNotNull(result, "Le résultat ne doit pas être null");
                assertFalse(result.isEmpty(), "Le résultat doit contenir des questions");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Erreur de temporisation dans le test", e);
                }
            }
        }
    }

        @Test
    void testAskAPITheTriviaQuestionsSuccess() {
        String category = "sport_and_leisure";
        String difficulty = "easy";

        List<QuestionDTO> result = questionServices.askAPIQuestions(category, difficulty);
        System.out.println(result);

        assertNotNull(result, "Le résultat ne doit pas être null");
    }

    @Test
    void testAskAPIQuestions_InvalidCategory() {
        // Arrange
        String category = null;
        String difficulty = "easy";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                questionServices.askAPIQuestions(category, difficulty)
        );
        assertEquals("La catégorie ne peut pas être nulle ou vide.", exception.getMessage());
    }

    @Test
    void testAskAPIQuestions_InvalidDifficulty() {
        // Arrange
        String category = "23";
        String difficulty = "";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                questionServices.askAPIQuestions(category, difficulty)
        );
        assertEquals("La difficulté ne peut pas être nulle ou vide.", exception.getMessage());
    }
}
