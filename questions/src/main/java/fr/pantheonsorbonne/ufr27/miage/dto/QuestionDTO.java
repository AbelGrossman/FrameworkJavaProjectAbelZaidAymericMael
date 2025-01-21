package fr.pantheonsorbonne.ufr27.miage.dto;

import java.util.List;

public record QuestionDTO(String difficulty,
                          String category,
                          String question,
                          String correct_answer,
                          List<String> incorrect_answers) {}


