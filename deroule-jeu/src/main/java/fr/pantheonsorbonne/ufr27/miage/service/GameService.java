package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.GameDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.QuestionDTO;
import fr.pantheonsorbonne.ufr27.miage.model.Game;
import fr.pantheonsorbonne.ufr27.miage.model.Player;
import fr.pantheonsorbonne.ufr27.miage.model.Question;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private Game currentGame;
    private AtomicInteger currentQuestionIndex = new AtomicInteger(0);

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
                .map(q -> new Question(q.getType(), q.getDifficulty(), q.getCategory(), q.getQuestion(), q.getCorrect_answer(), q.getIncorrect_answers()))
                .collect(Collectors.toList());
        currentGame = new Game(gameId, players, totalQuestions, questionEntities);
        logger.info("Game initialized with ID: {} and {} questions", gameId, totalQuestions);
        return gameId;
    }

    private List<Player> retrievePlayers(List<String> playerIds) {
        // Implement logic to retrieve player details from the database or another service
        // For now, we'll create dummy players
        return playerIds.stream().map(id -> new Player(id, "Player " + id)).toList();
    }

    public int processAnswer(String playerId, String answer) {
        if (currentGame == null) {
            logger.error("Game not initialized");
            throw new RuntimeException("Game not initialized");
        }

        Question currentQuestion = currentGame.getCurrentQuestion(currentQuestionIndex.get());
        if (currentQuestion == null) {
            logger.error("No current question to process answer for player: {}", playerId);
            throw new RuntimeException("No current question to process answer");
        }

        Player player = getPlayerById(currentGame, playerId);
        if (player != null) {
            boolean isCorrect = checkAnswer(answer, currentQuestion);
            if (isCorrect) {
                player.setScore(player.getScore() + 1);
            }
        }

        logger.info("Player {} answered: {}", playerId, answer);
        return player != null ? player.getScore() : 0;
    }

    private Player getPlayerById(Game game, String playerId) {
        for (Player player : game.getPlayers()) {
            if (player.getPlayerId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

    private boolean checkAnswer(String answer, Question question) {
        return question.getCorrect_answer().equalsIgnoreCase(answer);
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
                .map(q -> new QuestionDTO(q.getType(), q.getDifficulty(), q.getCategory(), q.getQuestion(), q.getCorrect_answer(), q.getIncorrect_answers()))
                .collect(Collectors.toList());
    }

    private boolean isPlayerAllowed(String playerId) {
        return currentGame.getPlayers().stream().anyMatch(player -> player.getPlayerId().equals(playerId));
    }

    public QuestionDTO getCurrentQuestion(String playerId) {
        if (currentGame == null) {
            logger.error("Game not initialized");
            throw new RuntimeException("Game not initialized");
        }

        if (!isPlayerAllowed(playerId)) {
            logger.error("Player {} is not allowed to access the questions", playerId);
            throw new RuntimeException("Player is not allowed to access the questions");
        }

        int index = currentQuestionIndex.get();
        if (index < currentGame.getQuestions().size()) {
            Question question = currentGame.getQuestions().get(index);
            return new QuestionDTO(question.getType(), question.getDifficulty(), question.getCategory(), question.getQuestion(), question.getCorrect_answer(), question.getIncorrect_answers());
        } else {
            return null;
        }
    }

    public void nextQuestion() {
        currentQuestionIndex.incrementAndGet();
    }

    // public boolean isGameOver() {
    //     return currentQuestionIndex.get() >= currentGame.getTotalQuestions();
    // }

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
    }
}