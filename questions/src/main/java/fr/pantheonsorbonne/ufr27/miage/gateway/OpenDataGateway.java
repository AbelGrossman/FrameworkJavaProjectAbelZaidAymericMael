package fr.pantheonsorbonne.ufr27.miage.gateway;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.*;
import fr.pantheonsorbonne.ufr27.miage.exception.APIException;
import fr.pantheonsorbonne.ufr27.miage.map.QuestionMapper;
import fr.pantheonsorbonne.ufr27.miage.service.OpenDataRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;


@ApplicationScoped
public class OpenDataGateway {

    @Inject
    @RestClient
    OpenDataRestClient openDataRestClient;

    @Inject
    QuestionMapper questionMapper;


    public List<QuestionDTO> fetchQuestions(int limit, String category, String difficulty) {
        try {
            String jsonResponse = openDataRestClient.getQuestions(limit, category, difficulty);

            List<OpenDataDTO> openDataDTOs = new ObjectMapper()
                    .readValue(jsonResponse, new TypeReference<>() {
                    });

            return openDataDTOs.stream()
                    .map(questionMapper::mapOpenDataToQuestionDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new APIException("Erreur lors de la récupération des questions : " + e.getMessage(), e);
        }
    }
}
