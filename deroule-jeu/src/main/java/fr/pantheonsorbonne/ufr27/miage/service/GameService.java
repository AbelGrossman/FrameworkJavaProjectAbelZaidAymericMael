package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.GameDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.PlayerResultsRequest;
import fr.pantheonsorbonne.ufr27.miage.model.Game;
import fr.pantheonsorbonne.ufr27.miage.model.Player;
import fr.pantheonsorbonne.ufr27.miage.model.Question;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private static final int QUESTION_TIMER_SECONDS = 30;
    private static final int ANSWER_DISPLAY_SECONDS = 10;

    private Game currentGame;
    private AtomicInteger currentQuestionIndex = new AtomicInteger(0);
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> currentTimer;
    private Map<String, Boolean> playerAnswered = new ConcurrentHashMap<>();
    private long questionStartTime;
    private boolean isShowingAnswer = false;

    @Inject
    GameDAO gameDAO;

    public String initializeGame(List<String> playerIds, int totalQuestions, List<QuestionDTO> questions) {
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
        String gameId = UUID.randomUUID().toString();
        List<Question> questionEntities = questions.stream()
                .map(q -> new Question(q.getType(), q.getDifficulty(), q.getCategory(),
                        q.getQuestion(), q.getCorrect_answer(), q.getIncorrect_answers()))
                .collect(Collectors.toList());

        currentGame = new Game(gameId, players, totalQuestions, questionEntities);

        // Initialize timer components
        scheduler = Executors.newSingleThreadScheduledExecutor();
        resetPlayerAnsweredStatus();
        startQuestionTimer();

        logger.info("Game initialized with ID: {} and {} questions", gameId, totalQuestions);
        return gameId;
    }

    private List<Player> retrievePlayers(List<String> playerIds) {
        return playerIds.stream()
                .map(id -> new Player(id, "Player " + id))
                .collect(Collectors.toList());
    }

    private void resetPlayerAnsweredStatus() {
        playerAnswered.clear();
        currentGame.getPlayers().forEach(player ->
                playerAnswered.put(player.getPlayerId(), false));
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
        questionStartTime = System.currentTimeMillis(); // Reset timer for answer display

        // Schedule next question after answer display
        currentTimer = scheduler.schedule(() -> {
            isShowingAnswer = false;
            nextQuestion();
        }, ANSWER_DISPLAY_SECONDS, TimeUnit.SECONDS);
    }

    public int processAnswer(String playerId, String answer) {
        if (currentGame == null) {
            throw new RuntimeException("Game not initialized");
        }

        if (playerAnswered.get(playerId)) {
            logger.warn("Player {} already answered current question", playerId);
            return getPlayerById(currentGame, playerId).getScore();
        }

        int index = currentQuestionIndex.get();
        if (index >= currentGame.getQuestions().size()) {
            logger.error("No current question to process answer for player: {}", playerId);
            throw new RuntimeException("No current question to process answer");
        }

        Question currentQuestion = currentGame.getQuestions().get(index);
        Player player = getPlayerById(currentGame, playerId);

        if (player == null) {
            logger.error("Player {} not found", playerId);
            throw new RuntimeException("Player not found");
        }

        boolean isCorrect = checkAnswer(answer, currentQuestion);
        if (isCorrect) {
            player.setScore(player.getScore() + 1);
            logger.info("Player {} answered correctly. New score: {}", playerId, player.getScore());
        }

        // Mark player as answered
        playerAnswered.put(playerId, true);

        // Check if all players have answered
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
            finishGame();
        }
    }

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

    public List<QuestionDTO> getQuestionsForGame(String playerId) {
        if (currentGame == null) {
            logger.error("Game not initialized");
            throw new RuntimeException("Game not initialized");
        }

        if (!isPlayerAllowed(playerId)) {
            logger.error("Player {} is not allowed to access the questions", playerId);
            throw new RuntimeException("Player is not allowed to access the questions");
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

    public void finishGame() {
        if (currentGame == null) {
            logger.error("Game not initialized");
            throw new RuntimeException("Game not initialized");
        }

        List<Player> players = currentGame.getPlayers();
        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        List<Integer> ranks = currentGame.getRanks();
        for (int i = 0; i < players.size(); i++) {
            ranks.set(i, i + 1);
        }

        logger.info("Game finished. Final ranks: {}", currentGame.getRanks());

        // Save the game to the database
        gameDAO.save(currentGame);

        // Save player results to another microservice
        for (Player player : players) {
            PlayerResultsRequest playerResultsRequest = new PlayerResultsRequest();
            playerResultsRequest.setPlayerId(player.getPlayerId());
            playerResultsRequest.setScore(player.getScore());
            playerResultsRequest.setAverageResponseTime("1.1s"); // Placeholder for average response time
            playerResultsRequest.setCategory(currentGame.getQuestions().get(0).getCategory());
            playerResultsRequest.setTotalQuestions(currentGame.getTotalQuestions());
            playerResultsRequest.setRank(ranks.get(players.indexOf(player)));

            // Send player results to another microservice
            sendPlayerResults(playerResultsRequest);
        }

        // Cleanup
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    private void sendPlayerResults(PlayerResultsRequest playerResultsRequest) {
        // Implement logic to send player results to another microservice
        logger.info("Sending player results: {}", playerResultsRequest);
    }
}