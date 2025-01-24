package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Game;
import fr.pantheonsorbonne.ufr27.miage.model.PlayerResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GameDAOImpl implements GameDAO {

    private static final Logger logger = LoggerFactory.getLogger(GameDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Game game) {
        try {
            if (game.getId() == null) {
                entityManager.persist(game);
            } else {
                entityManager.merge(game);
            }
            entityManager.flush(); // Force flush to see any errors
            logger.info("Game saved with ID: {}", game.getId());
        } catch (Exception e) {
            logger.error("Error saving game: ", e);
            throw e;
        }
    }

    @Override
    public Optional<Game> findById(Long id) {
        Game game = entityManager.find(Game.class, id);
        return game == null ? Optional.empty() : Optional.of(game);
    }

    @Override
    @Transactional
    public void savePlayerResults(String playerId, long gameId, int score, long averageResponseTime, int rank, String category, int totalQuestions) {
        try {
            PlayerResult playerResult = new PlayerResult(playerId, gameId, score, averageResponseTime, rank, category, totalQuestions);
            entityManager.persist(playerResult);
            logger.info("Player results saved for player: {} in game: {}", playerId, gameId);
        } catch (Exception e) {
            logger.error("Error saving player results: ", e);
            throw e;
        }
    }
}