package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dto.PlayerResultsRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import java.util.List;
import java.util.Map;

public interface GameService {
    Long initializeGame(List<String> playerIds, String category, String difficulty, int totalQuestions,
            List<QuestionDTO> questions, String teamId);

    long getCurrentGameId();

    int processAnswer(String playerId, String answer, long responseTime);

    Map<String, Object> getCurrentGameState(String playerId);

    List<QuestionDTO> getQuestionsForGame(String playerId);

    void finishGame(Long gameId);

    int getPlayerScore(String playerId);

    void incrementScore(String playerId);

    void savePlayerResult(String playerId, Long gameId, int score, long averageResponseTime, int rank,
            String category, int totalQuestions);

    Map<String, Integer> getGameRankings(Long gameId);

    List<PlayerResultsRequest> getPlayerResultsDTO(Long gameId);

    void sendGameCompletionStatus(Long gameId);

    String getTeamIdByGameId(Long gameId);

    void sendToGameCompletionGateway(Long gameId );
}