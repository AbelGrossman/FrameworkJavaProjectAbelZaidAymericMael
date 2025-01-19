package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;

import java.util.List;

public interface QuestionServices {

    List<QuestionDTO> askAPIQuestions(String category, String difficulty);
}
