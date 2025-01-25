package fr.pantheonsorbonne.ufr27.miage.exception;

//C'est l'excpetion qui est lancée lorsqu'une erreur survient dans le jeu. PAS A UTILISER POUR LES ERREURS DAO OU AUTRES
public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
