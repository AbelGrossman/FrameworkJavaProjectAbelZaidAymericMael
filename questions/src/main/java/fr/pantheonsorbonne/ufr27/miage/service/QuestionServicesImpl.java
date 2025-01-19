package fr.pantheonsorbonne.ufr27.miage.service;


import fr.pantheonsorbonne.ufr27.miage.camel.gateway.TheTriviaGateway;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;


@ApplicationScoped
public class QuestionServicesImpl implements QuestionServices {

    @Inject
    TheTriviaGateway theTriviaGateway;


    @Override
    public List<QuestionDTO> askAPIQuestions(String category, String difficulty) {
        validateParameters(category, difficulty);
        return fetchQuestions(category, difficulty);
    }

    private List<QuestionDTO> fetchQuestions(String category, String difficulty) {
        try {
            return theTriviaGateway.fetchQuestions(category, difficulty);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des questions : " + e.getMessage(), e);
        }
    }

    private void validateParameters(String category, String difficulty) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("La catégorie ne peut pas être nulle ou vide.");
        }
        if (difficulty == null || difficulty.isEmpty()) {
            throw new IllegalArgumentException("La difficulté ne peut pas être nulle ou vide.");
        }
    }

}

