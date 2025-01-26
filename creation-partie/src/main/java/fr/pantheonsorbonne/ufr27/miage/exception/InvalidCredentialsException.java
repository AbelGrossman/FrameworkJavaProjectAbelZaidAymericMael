package fr.pantheonsorbonne.ufr27.miage.exception;


public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Invalid username or password provided.");
    }
}

