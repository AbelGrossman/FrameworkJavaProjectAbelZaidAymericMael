package fr.pantheonsorbonne.ufr27.miage.dao;


import fr.pantheonsorbonne.ufr27.miage.model.Game;
import fr.pantheonsorbonne.ufr27.miage.model.Question;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class QuestionDAOImpl implements QuestionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveQuestion(Question question, Game game) {
        question.setGame(game);
        entityManager.persist(question);
    }

    @Override
    public List<Question> findQuestionsForGame(Long gameId) {
        return entityManager.createQuery(
                        "SELECT q FROM Question q WHERE q.game.id = :gameId ORDER BY q.id",
                        Question.class)
                .setParameter("gameId", gameId)
                .getResultList();
    }
}