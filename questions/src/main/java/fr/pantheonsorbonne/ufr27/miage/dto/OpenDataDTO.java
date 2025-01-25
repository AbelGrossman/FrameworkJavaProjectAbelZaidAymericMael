package fr.pantheonsorbonne.ufr27.miage.dto;

import java.util.List;

public record OpenDataDTO(String difficulty,
                          String category,
                          String question,
                          String correct_answer,
                          List<String> incorrect_answers) {}
