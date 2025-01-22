package fr.pantheonsorbonne.ufr27.miage.map;

import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.TheTriviaDTO;
import fr.pantheonsorbonne.ufr27.miage.exception.MapperException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuestionMapper {

    public QuestionDTO mapToQuestionDTO(TheTriviaDTO theTriviaDTO) throws MapperException {
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
}