package fr.pantheonsorbonne.ufr27.miage.service;


import fr.pantheonsorbonne.ufr27.miage.camel.gateway.TheTriviaGateway;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.exception.ServiceException;
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
        category = category.toLowerCase();
        difficulty = difficulty.toLowerCase();
        return fetchQuestions(category, difficulty);
    }

    private List<QuestionDTO> fetchQuestions(String category, String difficulty) {
        try {
            int limit = 20;
            return theTriviaGateway.fetchQuestions(limit, category, difficulty);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des questions : " + e.getMessage(), e);
        }
    }

    private void validateParameters(String category, String difficulty) {
        if (category == null || category.isEmpty()) {
            throw new ServiceException("La catégorie ne peut pas être nulle ou vide.");
        }
        if (difficulty == null || difficulty.isEmpty()) {
            throw new ServiceException("La difficulté ne peut pas être nulle ou vide.");
        }
    }

}

