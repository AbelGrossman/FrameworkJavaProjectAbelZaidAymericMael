package fr.pantheonsorbonne.ufr27.miage.dto;

import java.util.List;


public record TheTriviaDTO(String category,
                           String id,
                           String correctAnswer,
                           List<String> incorrectAnswers,
                           Question question,
                           List<String> tags,
                           String type,
                           String difficulty,
                           List<String> regions,
                           boolean isNiche) {
    public record Question(String text) {}
}