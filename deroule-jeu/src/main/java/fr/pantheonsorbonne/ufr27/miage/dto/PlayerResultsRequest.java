package fr.pantheonsorbonne.ufr27.miage.dto;

public record PlayerResultsRequest(
    String playerId,
    int score,
    String averageResponseTime,
    String category,
    int totalQuestions,
    int rank
) {}