package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;
import java.util.Optional;
import fr.pantheonsorbonne.ufr27.miage.model.Game;
import fr.pantheonsorbonne.ufr27.miage.model.PlayerResult;

public interface GameDAO {
    void save(Game game);
    Optional<Game> findById(Long id);
    void savePlayerResults(String playerId, long gameId, int score, long averageResponseTime, int rank, String category, int totalQuestions);
    List<PlayerResult> findPlayerResultsByGameId(Long gameId);
    String findTeamIdByGameId(Long gameId);
    boolean playerResultExists(String playerId, Long gameId);
    void updatePlayerResult(PlayerResult playerResult);
}