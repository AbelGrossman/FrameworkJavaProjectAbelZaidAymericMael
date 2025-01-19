package fr.pantheonsorbonne.ufr27.miage.service;

public interface GameCreationService {
    void validateNewRequest(Long playerId);
    void cancelRequest(Long playerId);
    String getPlayerStatus(Long playerId);
}