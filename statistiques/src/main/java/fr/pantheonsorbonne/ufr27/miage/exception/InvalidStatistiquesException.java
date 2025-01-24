package fr.pantheonsorbonne.ufr27.miage.exception;

/**
 * Exception levée lorsqu'une donnée invalide est détectée dans les statistiques
 * reçues ou calculées.
 */
public class InvalidStatistiquesException extends RuntimeException {

    /**
     * Constructeur sans message.
     */
    public InvalidStatistiquesException() {
        super("Invalid statistiques data detected.");
    }

    /**
     * Constructeur avec un message personnalisé.
     *
     * @param message Le message détaillant l'exception.
     */
    public InvalidStatistiquesException(String message) {
        super(message);
    }

    /**
     * Constructeur avec une cause.
     *
     * @param cause La cause de l'exception.
     */
    public InvalidStatistiquesException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructeur avec un message et une cause.
     *
     * @param message Le message détaillant l'exception.
     * @param cause   La cause de l'exception.
     */
    public InvalidStatistiquesException(String message, Throwable cause) {
        super(message, cause);
    }
}
