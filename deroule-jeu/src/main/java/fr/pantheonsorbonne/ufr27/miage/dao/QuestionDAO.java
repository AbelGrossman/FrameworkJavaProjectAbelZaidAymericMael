package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Game;
import fr.pantheonsorbonne.ufr27.miage.model.Question;

import java.util.List;

public interface QuestionDAO {
    void saveQuestion(Question question, Game game);
    List<Question> findQuestionsForGame(Long gameId);
}
