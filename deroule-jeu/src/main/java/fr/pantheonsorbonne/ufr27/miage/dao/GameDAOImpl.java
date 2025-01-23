package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Game;
// import fr.pantheonsorbonne.ufr27.miage.model.PlayerResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.util.List;
//import java.util.Optional;

@ApplicationScoped
public class GameDAOImpl implements GameDAO {

    private static final Logger logger = LoggerFactory.getLogger(GameDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Game game) {
        try {
            entityManager.persist(game);
            entityManager.flush(); // Force flush to see any errors
            logger.info("Game saved with ID: {}", game.getId());
        } catch (Exception e) {
            logger.error("Error saving game: ", e);
            throw e;
        }
    }

    // @Override
    // public Optional<Game> findById(Long id) {
    // Game game = entityManager.find(Game.class, id);
    // return Optional.ofNullable(game);
    // }

    // @Override
    // @Transactional
    // public void update(Game game) {
    // entityManager.merge(game);
    // logger.info("Game updated with ID: {}", game.getId());
    // }

    // @Override
    // @Transactional
    // public void delete(Long id) {
    // Game game = entityManager.find(Game.class, id);
    // if (game != null) {
    // entityManager.remove(game);
    // logger.info("Game deleted with ID: {}", id);
    // }
    // }

    // @Override
    // public Optional<Game> findByTheme(String theme) {
    // List<Game> games = entityManager.createQuery("SELECT g FROM Game g WHERE
    // g.theme = :theme", Game.class)
    // .setParameter("theme", theme)
    // .getResultList();
    // return games.isEmpty() ? Optional.empty() : Optional.of(games.get(0));
    // }

    // @Override
    // public List<Game> findAll() {
    // return entityManager.createQuery("SELECT g FROM Game g",
    // Game.class).getResultList();
    // }

    // @Override
    // @Transactional
    // public void savePlayerResults(String playerId, String gameId, int score, long
    // averageResponseTime) {
    // PlayerResult playerResult = new PlayerResult(playerId, gameId, score,
    // averageResponseTime);
    // entityManager.persist(playerResult);
    // logger.info("Player results saved for player ID: {} in game ID: {}",
    // playerId, gameId);
    // }
}