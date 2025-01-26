package fr.pantheonsorbonne.ufr27.miage.map;

import fr.pantheonsorbonne.ufr27.miage.dto.OpenDataDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.TheTriviaDTO;
import fr.pantheonsorbonne.ufr27.miage.exception.MapperException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuestionMapper {

    public QuestionDTO mapTriviaToQuestionDTO(TheTriviaDTO theTriviaDTO) throws MapperException {
        try {
            return new QuestionDTO(
                    theTriviaDTO.difficulty(),
                    theTriviaDTO.category(),
                    theTriviaDTO.question().text(),
                    theTriviaDTO.correctAnswer(), // Cela sera la "correct_answer"
                    theTriviaDTO.incorrectAnswers() // Cela sera "incorrect_answers"
            );
        } catch (Exception e) {
            throw new MapperException("Erreur lors du mapping de l'objet TheTriviaDTO vers QuestionDTO : " + e.getMessage(), e);
        }
    }

    public QuestionDTO mapOpenDataToQuestionDTO(OpenDataDTO openDataDTO) throws MapperException {
        try {
            return new QuestionDTO(
                    openDataDTO.difficulty(),
                    openDataDTO.category(),
                    openDataDTO.question(),
                    openDataDTO.correct_answer(),
                    openDataDTO.incorrect_answers()
            );
        } catch (Exception e) {
            throw new MapperException("Erreur lors du mapping de l'objet OpenDataDTO vers QuestionDTO : " + e.getMessage(), e);
        }
    }
}