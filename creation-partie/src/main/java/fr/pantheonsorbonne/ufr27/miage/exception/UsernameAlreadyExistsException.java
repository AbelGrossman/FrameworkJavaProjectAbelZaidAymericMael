package fr.pantheonsorbonne.ufr27.miage.exception;

public class UsernameAlreadyExistsException extends Exception {
    public UsernameAlreadyExistsException(String username) {
        super("The username '" + username + "' is already taken.");
    }
}
