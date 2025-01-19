package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.PlayerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlayerRequestDaoImpl implements PlayerRequestDao {

    @Inject
    EntityManager em;

    @Override
    @Transactional
    public Optional<PlayerRequest> findActiveRequestByPlayerId(Long playerId) {
        List<PlayerRequest> activeRequests = em.createQuery(
                        "SELECT r FROM PlayerRequest r WHERE r.playerId = :playerId " +
                                "AND r.status IN ('PENDING', 'SENT_TO_MATCHMAKING', 'IN_GAME')",
                        PlayerRequest.class)
                .setParameter("playerId", playerId)
                .getResultList();

        return activeRequests.isEmpty() ? Optional.empty() : Optional.of(activeRequests.get(0));
    }

    @Override
    @Transactional
    public void updateRequestStatus(Long id, String status) {
        PlayerRequest request = em.find(PlayerRequest.class, id);
        if (request != null) {
            request.setStatus(status);
            em.merge(request);
        }
    }
}
