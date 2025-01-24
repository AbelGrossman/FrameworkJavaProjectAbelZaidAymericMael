package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.Optional;

import fr.pantheonsorbonne.ufr27.miage.model.Game;

// import java.util.List;
// import java.util.Optional;

public interface GameDAO {
    void save(Game game);

    
    Optional<Game> findById(Long id);
    // void update(Game game);
    // void delete(Long id);
    // Optional<Game> findByTheme(String theme);
    // List<Game> findAll();
    void savePlayerResults(String playerId, long gameId, int score, long averageResponseTime, int rank, String category, int totalQuestions);
}