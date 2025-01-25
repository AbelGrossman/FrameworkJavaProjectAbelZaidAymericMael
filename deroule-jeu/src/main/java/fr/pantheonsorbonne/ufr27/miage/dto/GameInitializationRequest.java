package fr.pantheonsorbonne.ufr27.miage.dto;

import java.util.List;

public record GameInitializationRequest(
    List<String> playerIds,
    String category,
    String difficulty,
    int totalQuestions,
    List<QuestionDTO> questions
) {}