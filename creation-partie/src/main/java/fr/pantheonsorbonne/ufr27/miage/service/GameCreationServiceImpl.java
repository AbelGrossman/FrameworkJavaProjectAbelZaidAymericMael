package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.PlayerRequestDao;
import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import fr.pantheonsorbonne.ufr27.miage.exception.PlayerNotFoundException;
import fr.pantheonsorbonne.ufr27.miage.model.PlayerRequest;
import fr.pantheonsorbonne.ufr27.miage.model.RequestStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class GameCreationServiceImpl implements GameCreationService {

    @Inject
    PlayerRequestDao playerRequestDao;

    @Override
    @Transactional
    public void validateNewRequest(Long playerId) throws DuplicateRequestException {
        // Vérifier d'abord si le joueur existe
        if (!playerRequestDao.existsPlayer(playerId)) {
            throw new PlayerNotFoundException("Player not found with ID: " + playerId);
        }
        Optional<PlayerRequest> existingRequest = playerRequestDao.findActiveRequestByPlayerId(playerId);
        if (existingRequest.isPresent()) {
            throw new DuplicateRequestException("Player already has an active request or is in game");
        }
    }

    @Override
    @Transactional
    public void createNewRequest(Long playerId) {
        PlayerRequest newRequest = new PlayerRequest();
        newRequest.setPlayerId(playerId);
        newRequest.setStatus(RequestStatus.PENDING.name());
        newRequest.setRequestTime(LocalDateTime.now());
        playerRequestDao.persist(newRequest);
    }

    @Override
    @Transactional
    public void updateToMatchmaking(Long playerId) {
        updateRequestStatus(playerId, RequestStatus.SENT_TO_MATCHMAKING);
    }

    @Override
    @Transactional
    public void updateToInGame(Long playerId) {
        updateRequestStatus(playerId, RequestStatus.IN_GAME);
    }

    @Override
    @Transactional
    public void updateToFinished(Long playerId) {
        updateRequestStatus(playerId, RequestStatus.FINISHED);
    }

    @Override
    @Transactional
    public void cancelRequest(Long playerId) {
        updateRequestStatus(playerId, RequestStatus.CANCELLED);
    }

    @Override
    public String getPlayerStatus(Long playerId) {
        Optional<PlayerRequest> request = playerRequestDao.findActiveRequestByPlayerId(playerId);
        return request.map(PlayerRequest::getStatus).orElse(RequestStatus.NO_ACTIVE_REQUEST.name());
    }

    private void updateRequestStatus(Long playerId, RequestStatus newStatus) {
        Optional<PlayerRequest> request = playerRequestDao.findActiveRequestByPlayerId(playerId);
        request.ifPresent(playerRequest ->
                playerRequestDao.updateRequestStatus(playerRequest.getId(), newStatus.name())
        );
    }
}