package fr.pantheonsorbonne.ufr27.miage.service;
import fr.pantheonsorbonne.ufr27.miage.model.RequestStatus;

public interface GameCreationService {
    void validateNewRequest(Long playerId);
    void createNewRequest(Long playerId);
    void updateToMatchmaking(Long playerId);
    void updateToInGame(Long playerId);
    void updateToFinished(Long playerId);
    void cancelRequest(Long playerId);
    String getPlayerStatus(Long playerId);
}