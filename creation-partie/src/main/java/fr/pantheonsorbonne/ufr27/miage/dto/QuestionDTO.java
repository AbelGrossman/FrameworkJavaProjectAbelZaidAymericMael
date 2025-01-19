package fr.pantheonsorbonne.ufr27.miage.dto;

import java.util.List;

public record QuestionDTO(String id, String question, List<String> choices, String correctAnswer) {}

