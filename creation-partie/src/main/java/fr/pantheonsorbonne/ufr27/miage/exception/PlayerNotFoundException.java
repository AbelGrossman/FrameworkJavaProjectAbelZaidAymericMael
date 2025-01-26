package fr.pantheonsorbonne.ufr27.miage.exception;

public class PlayerNotFoundException extends Exception  {
    public PlayerNotFoundException(Long playerId) {
        super("Player not found with ID: " + playerId);
    }
}