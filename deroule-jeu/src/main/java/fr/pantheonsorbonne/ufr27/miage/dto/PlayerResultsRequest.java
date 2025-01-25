package fr.pantheonsorbonne.ufr27.miage.dto;

public record PlayerResultsRequest(
    String playerId,
    int score,
    long gameId,
    long averageResponseTime,
    String category,
    int totalQuestions,
    int rank
) {}