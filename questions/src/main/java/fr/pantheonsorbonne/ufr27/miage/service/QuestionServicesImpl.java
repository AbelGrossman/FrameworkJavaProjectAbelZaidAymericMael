package fr.pantheonsorbonne.ufr27.miage.service;


import fr.pantheonsorbonne.ufr27.miage.camel.gateway.OpenDataGateway;
import fr.pantheonsorbonne.ufr27.miage.camel.gateway.TheTriviaGateway;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@ApplicationScoped
public class QuestionServicesImpl implements QuestionServices {

    private static final Logger log = LoggerFactory.getLogger(QuestionServicesImpl.class);
    @Inject
    TheTriviaGateway theTriviaGateway;

    @Inject
    OpenDataGateway openDataGateway;


    @Override
    public List<QuestionDTO> askAPIQuestions(String category, String difficulty) {
        validateParameters(category, difficulty);
        category = category.toLowerCase();
        difficulty = difficulty.toLowerCase();
        return fetchQuestions(category, difficulty);
    }

    private List<QuestionDTO> fetchQuestions(String category, String difficulty) {
        int limit = 20;
        try {
            return theTriviaGateway.fetchQuestions(limit, category, difficulty);
        } catch (Exception e) {
            try {
                log.error("Migration vers OpenData suite a une erreur lors de la récupération des questions de l'API TheTrivia : {}", e.getMessage());
                category = convertCategory(category);
                return openDataGateway.fetchQuestions(limit, category, difficulty);
            } catch (Exception fallbackException) {
                throw new ServiceException("Erreur lors de la récupération des questions : " + fallbackException.getMessage(),
                        fallbackException);
            }
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

    private String convertCategory(String category) throws ServiceException {
        try {
            switch (category) {
                case "general_knowledge":
                    return "9";
                case "science":
                    return "17";
                case "sport_and_leisure":
                    return "21";
                case "geography":
                    return "22";
                case "history":
                    return "23";
                case "arts_and_literature":
                    return "25";
                default:
                    return "9";
            }
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la conversion de la catégorie : " + e.getMessage(), e);
        }
    }

}

