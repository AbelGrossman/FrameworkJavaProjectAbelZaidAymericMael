package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.PlayerRequest;
import java.util.Optional;

public interface PlayerRequestDao {
    Optional<PlayerRequest> findActiveRequestByPlayerId(Long playerId);
    void updateRequestStatus(Long id, String status);
}