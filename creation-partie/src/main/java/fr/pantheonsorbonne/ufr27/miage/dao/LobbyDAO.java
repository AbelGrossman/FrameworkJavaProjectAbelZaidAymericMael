package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Lobby;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class LobbyDAO {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveLobby(Lobby lobby) {
        em.persist(lobby);
    }

    public Lobby findLobbyById(Long id) {
        return em.find(Lobby.class, id);
    }
}
