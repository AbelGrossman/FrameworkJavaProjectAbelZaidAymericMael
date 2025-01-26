package fr.pantheonsorbonne.ufr27.miage.exception;

public class DuplicateRequestException extends Exception  {
    public DuplicateRequestException() {
        super("Player already has an active request or is in game");
    }
}