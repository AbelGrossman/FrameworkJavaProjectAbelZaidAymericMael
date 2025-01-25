package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.GameDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.PlayerResultsRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.exception.GameException;
import fr.pantheonsorbonne.ufr27.miage.gateway.GameCompletionGateway;
import fr.pantheonsorbonne.ufr27.miage.model.Game;
import fr.pantheonsorbonne.ufr27.miage.model.Player;
import fr.pantheonsorbonne.ufr27.miage.model.PlayerResult;
import fr.pantheonsorbonne.ufr27.miage.model.Question;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    private static final int QUESTION_TIMER_SECONDS = 30;
    private static final int ANSWER_DISPLAY_SECONDS = 10;

    private Game currentGame;
    private Long currentGameId;
    private AtomicInteger currentQuestionIndex = new AtomicInteger(0);
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> currentTimer;
    private Map<String, Boolean> playerAnswered = new ConcurrentHashMap<>();
    private Map<String, List<Long>> playerResponseTimes = new ConcurrentHashMap<>();

    private long questionStartTime;
    private boolean isShowingAnswer = false;
    private long gameId;

    @Inject
    GameDAO gameDAO;

    @Inject
    GameCompletionGateway gameCompletionGateway;

    @Override
    @Transactional
    public Long initializeGame(List<String> playerIds, String category, String difficulty, int totalQuestions,
            List<QuestionDTO> questions, String teamId) {
        try {
            logger.info("Initializing game with category: {}, difficulty: {}, totalQuestions: {}", category, difficulty, totalQuestions);
            logger.info("Player IDs: {}", playerIds);
            logger.info("Questions size: {}", questions.size());

            if (playerIds == null || playerIds.size() != 6) {
                throw new IllegalArgumentException("Game must have exactly 6 players.");
            }

            if (totalQuestions <= 0) {
                throw new IllegalArgumentException("Total questions must be greater than 0.");
            }

            if (questions == null || questions.size() != totalQuestions) {
                throw new IllegalArgumentException("Number of questions does not match totalQuestions.");
            }

            List<Player> players = retrievePlayers(playerIds);
            logger.info("Created players: {}", players.size());

            List<Question> questionEntities = questions.stream()
                    .map(q -> new Question(q.type(), q.difficulty(), q.category(),
                            q.question(), q.correct_answer(), q.incorrect_answers()))
                    .collect(Collectors.toList());
            logger.info("Mapped questions: {}", questionEntities.size());

            currentGame = new Game(category, difficulty, players, totalQuestions, questionEntities, teamId);
            logger.info("Game created, saving to database...");

            gameDAO.save(currentGame);
            currentGameId = currentGame.getId();
            gameId = currentGameId;
            logger.info("Game saved with ID: {}", currentGameId);

            currentQuestionIndex.set(0);
            scheduler = Executors.newSingleThreadScheduledExecutor();
            resetPlayerAnsweredStatus();
            questionStartTime = System.currentTimeMillis();
            startQuestionTimer();

            return currentGameId;
        } catch (Exception e) {
            logger.error("Error initializing game:", e);
            throw e;
        }
    }

    @Override
    public long getCurrentGameId() {
        return gameId;
    }

    private List<Player> retrievePlayers(List<String> playerIds) {
        return playerIds.stream()
                .map(id -> new Player(id, "Player " + id))
                .collect(Collectors.toList());
    }

    private void resetPlayerAnsweredStatus() {
        playerAnswered.clear();
        currentGame.getPlayers().forEach(player -> playerAnswered.put(player.getPlayerId(), false));
        questionStartTime = System.currentTimeMillis();
    }

    private void startQuestionTimer() {
        if (currentTimer != null) {
            currentTimer.cancel(false);
        }

        resetPlayerAnsweredStatus();
        isShowingAnswer = false;

        currentTimer = scheduler.schedule(() -> {
            logger.info("Question timer expired. Showing correct answer.");
            showCorrectAnswer();
        }, QUESTION_TIMER_SECONDS, TimeUnit.SECONDS);
    }

    private void showCorrectAnswer() {
        isShowingAnswer = true;
        questionStartTime = System.currentTimeMillis();

        currentTimer = scheduler.schedule(() -> {
            isShowingAnswer = false;
            nextQuestion();
        }, ANSWER_DISPLAY_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public int processAnswer(String playerId, String answer, long responseTime) {
        if (currentGame == null) {
            throw new GameException.GameNotInitializedException();
        }

        if (playerAnswered.get(playerId)) {
            logger.warn("Player {} already answered current question", playerId);
            return getPlayerById(currentGame, playerId).getScore();
        }

        int index = currentQuestionIndex.get();
        if (index >= currentGame.getQuestions().size()) {
            logger.error("No current question to process answer for player: {}", playerId);
            throw new GameException.NoCurrentQuestionException(playerId);
        }

        Question currentQuestion = currentGame.getQuestions().get(index);
        Player player = getPlayerById(currentGame, playerId);

        if (player == null) {
            logger.error("Player {} not found", playerId);
            throw new GameException.PlayerNotFoundException(playerId);
        }

        playerResponseTimes.computeIfAbsent(playerId, k -> new ArrayList<>()).add(responseTime);

        List<Long> times = playerResponseTimes.get(playerId);
        double averageTime = times.stream().mapToLong(Long::valueOf).average().orElse(0.0);
        player.setAverageResponseTime((long) averageTime);

        boolean isCorrect = checkAnswer(answer, currentQuestion);
        if (isCorrect) {
            player.setScore(player.getScore() + 1);
            logger.info("Player {} answered correctly. New score: {}", playerId, player.getScore());
        }

        playerAnswered.put(playerId, true);

        if (allPlayersAnswered()) {
            if (currentTimer != null) {
                currentTimer.cancel(false);
            }
            showCorrectAnswer();
        }

        return player.getScore();
    }

    private Player getPlayerById(Game game, String playerId) {
        return game.getPlayers().stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    private boolean checkAnswer(String answer, Question question) {
        return question.getCorrect_answer().equalsIgnoreCase(answer);
    }

    private boolean allPlayersAnswered() {
        return !playerAnswered.containsValue(false);
    }

    private void nextQuestion() {
        int nextIndex = currentQuestionIndex.incrementAndGet();
        if (nextIndex < currentGame.getTotalQuestions()) {
            startQuestionTimer();
        } else {
            finishGame(currentGameId);
        }
    }

    @Override
    public Map<String, Object> getCurrentGameState(String playerId) {
        Map<String, Object> gameState = new HashMap<>();
        gameState.put("currentQuestionIndex", currentQuestionIndex.get());

        long remainingTime;
        if (isShowingAnswer) {
            long elapsedTime = System.currentTimeMillis() - questionStartTime;
            remainingTime = (ANSWER_DISPLAY_SECONDS * 1000) - elapsedTime;
        } else {
            long elapsedTime = System.currentTimeMillis() - questionStartTime;
            remainingTime = (QUESTION_TIMER_SECONDS * 1000) - elapsedTime;
        }

        gameState.put("remainingTime", Math.max(0, remainingTime));
        gameState.put("hasAnswered", playerAnswered.getOrDefault(playerId, false));
        gameState.put("isShowingAnswer", isShowingAnswer);

        if (isShowingAnswer && currentQuestionIndex.get() < currentGame.getQuestions().size()) {
            Question currentQuestion = currentGame.getQuestions().get(currentQuestionIndex.get());
            gameState.put("correctAnswer", currentQuestion.getCorrect_answer());
        }

        return gameState;
    }

    @Override
    public List<QuestionDTO> getQuestionsForGame(String playerId) {
        if (currentGame == null) {
            logger.error("Game not initialized");
            throw new GameException.GameNotInitializedException();
        }

        if (!isPlayerAllowed(playerId)) {
            logger.error("Player {} is not allowed to access the questions", playerId);
            throw new GameException.PlayerNotAllowedException(playerId);
        }

        return currentGame.getQuestions().stream()
                .map(q -> new QuestionDTO(q.getType(), q.getDifficulty(), q.getCategory(),
                        q.getQuestion(), q.getCorrect_answer(), q.getIncorrect_answers()))
                .collect(Collectors.toList());
    }

    private boolean isPlayerAllowed(String playerId) {
        return currentGame.getPlayers().stream()
                .anyMatch(player -> player.getPlayerId().equals(playerId));
    }

    @Override
    @Transactional
    public void finishGame(Long gameId) {
        Game game = gameDAO.findById(gameId)
                .orElseThrow(() -> new GameException.GameNotFoundException(gameId));

        game.setOver(true);
        gameDAO.save(game);

        if (scheduler != null) {
            scheduler.shutdown();
        }

        logger.info("Game {} finished successfully", gameId);
    }

    @Override
    public int getPlayerScore(String playerId) {
        Player player = getPlayerById(currentGame, playerId);
        if (player == null) {
            throw new GameException.PlayerNotFoundException(playerId);
        }
        return player.getScore();
    }

    @Override
    public void incrementScore(String playerId) {
        Player player = getPlayerById(currentGame, playerId);
        if (player == null) {
            throw new GameException.PlayerNotFoundException(playerId);
        }
        player.setScore(player.getScore() + 1);
    }

    @Override
    @Transactional
    public void savePlayerResult(String playerId, Long gameId, int score, long averageResponseTime, int rank,
            String category, int totalQuestions) {
        if (!gameDAO.playerResultExists(playerId, gameId)) {
            gameDAO.savePlayerResults(playerId, gameId, score, averageResponseTime, rank, category, totalQuestions);
        }
    }

    @Override
    @Transactional
    public Map<String, Integer> getGameRankings(Long gameId) {
        List<PlayerResult> results = gameDAO.findPlayerResultsByGameId(gameId);

        if (results.isEmpty()) {
            throw new GameException.NoResultsFoundException(gameId);
        }

        List<PlayerResult> rankedPlayers = results.stream()
                .sorted((p1, p2) -> {
                    int scoreCompare = Integer.compare(p2.getScore(), p1.getScore());
                    return scoreCompare != 0 ? scoreCompare
                            : Long.compare(p1.getAverageResponseTime(), p2.getAverageResponseTime());
                })
                .collect(Collectors.toList());

        for (int i = 0; i < rankedPlayers.size(); i++) {
            PlayerResult player = rankedPlayers.get(i);
            player.setRank(i + 1);
            gameDAO.updatePlayerResult(player);
        }

        Map<String, Integer> rankings = new HashMap<>();
        rankedPlayers.forEach(player -> rankings.put(player.getPlayerId(), player.getRank()));

        return rankings;
    }

    @Override
    public void sendToGameCompletionGateway(Long gameId) {
        gameCompletionGateway.handleGameCompletion(gameId, getPlayerResultsDTO(gameId), getTeamIdByGameId(gameId));
    }

    @Override
    public List<PlayerResultsRequest> getPlayerResultsDTO(Long gameId) {
        List<PlayerResult> results = gameDAO.findPlayerResultsByGameId(gameId);
        return results.stream()
                .map(playerResult -> new PlayerResultsRequest(
                        convertPlayerIDtoString(playerResult),
                        playerResult.getScore(),
                        playerResult.getGameId(),
                        playerResult.getAverageResponseTime(),
                        playerResult.getCategory(),
                        playerResult.getTotalQuestions(),
                        playerResult.getRank()))
                .collect(Collectors.toList());
    }

    private String convertPlayerIDtoString(PlayerResult playerResultsRequest) {
        return playerResultsRequest.getPlayerId();
    }

    @Override
    public String getTeamIdByGameId(Long gameId) {
        return gameDAO.findTeamIdByGameId(gameId);
    }
}