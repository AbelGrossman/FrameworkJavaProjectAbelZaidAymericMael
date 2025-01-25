package fr.pantheonsorbonne.ufr27.miage.exception;

public class GameServiceException extends RuntimeException {
    public GameServiceException(String message) {
        super(message);
    }

    public GameServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
