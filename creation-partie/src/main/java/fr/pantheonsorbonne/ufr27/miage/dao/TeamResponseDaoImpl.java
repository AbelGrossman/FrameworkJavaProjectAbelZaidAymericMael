package fr.pantheonsorbonne.ufr27.miage.dao;


import fr.pantheonsorbonne.ufr27.miage.model.TeamResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Optional;

// DAO Implementation
@ApplicationScoped
public class TeamResponseDaoImpl implements TeamResponseDao {

    @Inject
    EntityManager em;

    @Override
    @Transactional
    public Optional<TeamResponse> findById(String id) {
        TeamResponse response = em.find(TeamResponse.class, id);
        return Optional.ofNullable(response);
    }

    @Override
    @Transactional
    public void persist(TeamResponse teamResponse) {
        em.persist(teamResponse);
    }

    @Override
    @Transactional
    public void remove(String id) {
        TeamResponse response = em.find(TeamResponse.class, id);
        if (response != null) {
            em.remove(response);
        }
    }
}