package fr.pantheonsorbonne.ufr27.miage.exception;

public class GameDAOException extends RuntimeException {
    public GameDAOException(String message) {
        super(message);
    }

    public GameDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
