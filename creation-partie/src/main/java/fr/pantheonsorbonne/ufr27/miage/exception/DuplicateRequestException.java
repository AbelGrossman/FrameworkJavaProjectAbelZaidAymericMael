package fr.pantheonsorbonne.ufr27.miage.exception;

public class DuplicateRequestException extends Exception  {
    public DuplicateRequestException(String message) {
        super(message);
    }
}