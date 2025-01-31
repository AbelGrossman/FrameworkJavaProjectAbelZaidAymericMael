package fr.pantheonsorbonne.ufr27.miage.gateway;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.TheTriviaDTO;
import fr.pantheonsorbonne.ufr27.miage.exception.APIException;
import fr.pantheonsorbonne.ufr27.miage.map.QuestionMapper;
import fr.pantheonsorbonne.ufr27.miage.service.TheTriviaRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TheTriviaGateway {

    @Inject
    @RestClient
    TheTriviaRestClient theTriviaRestClient;

    @Inject
    QuestionMapper questionMapper;


    public List<QuestionDTO> fetchQuestions(int limit, String category, String difficulty) {
        try {
            String jsonResponse = theTriviaRestClient.getQuestions(limit, category, difficulty);

            List<TheTriviaDTO> triviaDTOs = new ObjectMapper()
                    .readValue(jsonResponse, new TypeReference<>() {
                    });

            return triviaDTOs.stream()
                    .map(questionMapper::mapTriviaToQuestionDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new APIException("Erreur lors de la récupération des questions : " + e.getMessage(), e);
        }
    }
}
