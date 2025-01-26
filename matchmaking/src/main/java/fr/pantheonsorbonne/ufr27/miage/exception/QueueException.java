package fr.pantheonsorbonne.ufr27.miage.exception;

public class QueueException extends RuntimeException {
    public QueueException(String message) {
        super(message);
    }

    public QueueException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class QueueNotFoundException extends QueueException {
        public QueueNotFoundException(String theme) {
            super("Queue not found for theme: " + theme);
        }
    }

    public static class PlayerAlreadyInQueueException extends QueueException {
        public PlayerAlreadyInQueueException(Long userId) {
            super("Player with ID " + userId + " is already in the queue.");
        }
    }

    public static class NotEnoughPlayersException extends QueueException {
        public NotEnoughPlayersException(String theme) {
            super("Not enough players to form teams for theme: " + theme);
        }
    }
}

