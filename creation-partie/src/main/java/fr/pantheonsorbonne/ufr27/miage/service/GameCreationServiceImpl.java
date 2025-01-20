package fr.pantheonsorbonne.ufr27.miage.service;


import fr.pantheonsorbonne.ufr27.miage.dao.PlayerRequestDao;
import fr.pantheonsorbonne.ufr27.miage.exception.DuplicateRequestException;
import fr.pantheonsorbonne.ufr27.miage.model.PlayerRequest;
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
    public void validateNewRequest(Long playerId) {
        Optional<PlayerRequest> existingRequest = playerRequestDao.findActiveRequestByPlayerId(playerId);

        if (existingRequest.isPresent()) {
            PlayerRequest request = existingRequest.get();
            String status = request.getStatus();

            switch (status) {
                case "PENDING":
                case "SENT_TO_MATCHMAKING":
                case "IN_GAME":
                    throw new DuplicateRequestException("Player already has an active request or is in game");
                default:
                    break;
            }
        }
    }

    @Override
    @Transactional
    public void createNewRequest(Long playerId) {
        PlayerRequest newRequest = new PlayerRequest();
        newRequest.setPlayerId(playerId);
        newRequest.setStatus("PENDING");
        newRequest.setRequestTime(LocalDateTime.now());
        playerRequestDao.persist(newRequest);
    }

    @Override
    public void cancelRequest(Long playerId) {
        Optional<PlayerRequest> request = playerRequestDao.findActiveRequestByPlayerId(playerId);
        request.ifPresent(playerRequest -> playerRequestDao.updateRequestStatus(playerRequest.getId(), "CANCELLED"));
    }

    @Override
    public String getPlayerStatus(Long playerId) {
        Optional<PlayerRequest> request = playerRequestDao.findActiveRequestByPlayerId(playerId);
        return request.map(PlayerRequest::getStatus).orElse("NO_ACTIVE_REQUEST");
    }
}