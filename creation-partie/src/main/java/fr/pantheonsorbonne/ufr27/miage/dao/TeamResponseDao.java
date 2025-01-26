package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.TeamResponse;

import java.util.Optional;

// DAO Interface
public interface TeamResponseDao {
    Optional<TeamResponse> findById(String id);
    void persist(TeamResponse teamResponse);
    void remove(String id);
}
