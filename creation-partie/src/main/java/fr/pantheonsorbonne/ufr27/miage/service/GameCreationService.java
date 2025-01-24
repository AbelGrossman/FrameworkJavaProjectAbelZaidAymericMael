package fr.pantheonsorbonne.ufr27.miage.service;
import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import fr.pantheonsorbonne.ufr27.miage.exception.JoinRequestNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.exception.PlayerNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.model.RequestStatus;

public interface GameCreationService {
    void validateNewRequest(Long playerId) throws PlayerNotFoundException, DuplicateRequestException;
    void validateCancelRequest(Long playerId) throws PlayerNotFoundException, JoinRequestNotFoundException;
    void createNewRequest(Long playerId);
    void updateToMatchmaking(Long playerId);
    void updateToInGame(Long playerId);
    void updateToFinished(Long playerId);
    void cancelRequest(Long playerId);
    String getPlayerStatus(Long playerId);
}