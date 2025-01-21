package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Player;
import java.util.Optional;

public interface PlayerDao {
    Optional<Player> findByUsername(String username);
    Optional<Player> findById(Long id);
    void persist(Player player);
    boolean existsByUsername(String username);
}