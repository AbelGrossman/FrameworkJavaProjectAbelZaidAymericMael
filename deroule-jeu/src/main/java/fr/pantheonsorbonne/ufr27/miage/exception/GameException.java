package fr.pantheonsorbonne.ufr27.miage.exception;

public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class GameNotInitializedException extends GameException {
        public GameNotInitializedException() {
            super("Game not initialized");
        }
    }

    public static class NoCurrentQuestionException extends GameException {
        public NoCurrentQuestionException(String playerId) {
            super("No current question to process answer for player: " + playerId);
        }
    }

    public static class PlayerNotFoundException extends GameException {
        public PlayerNotFoundException(String playerId) {
            super("Player not found: " + playerId);
        }
    }

    public static class PlayerNotAllowedException extends GameException {
        public PlayerNotAllowedException(String playerId) {
            super("Player " + playerId + " is not allowed to access the questions");
        }
    }

    public static class GameNotFoundException extends GameException {
        public GameNotFoundException(Long gameId) {
            super("Game not found with id: " + gameId);
        }
    }

    public static class NoResultsFoundException extends GameException {
        public NoResultsFoundException(Long gameId) {
            super("No results found for game " + gameId);
        }
    }
}