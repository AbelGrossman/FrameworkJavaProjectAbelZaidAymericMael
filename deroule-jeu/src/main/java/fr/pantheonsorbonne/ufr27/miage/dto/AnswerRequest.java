package fr.pantheonsorbonne.ufr27.miage.dto;

public record AnswerRequest(
    String playerId,
    String answer,
    long startTime
) {}