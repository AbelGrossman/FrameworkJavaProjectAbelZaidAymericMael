package fr.pantheonsorbonne.ufr27.miage.exception;

public class InvalidStatistiquesException extends RuntimeException {

    public InvalidStatistiquesException() {
        super("Invalid statistiques data detected.");
    }

    public InvalidStatistiquesException(String message) {
        super(message);
    }

    public InvalidStatistiquesException(Throwable cause) {
        super(cause);
    }

    public InvalidStatistiquesException(String message, Throwable cause) {
        super(message, cause);
    }
}
