package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import java.util.Optional;


@ApplicationScoped
public class PlayerDaoImpl implements PlayerDao {

    @Inject
    EntityManager em;

    @Override
    public Optional<Player> findByUsername(String username) {
        try {
            Player player = em.createQuery(
                            "SELECT p FROM Player p WHERE p.username = :username", Player.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(player);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Player> findById(Long id) {
        Player player = em.find(Player.class, id);
        return Optional.ofNullable(player);
    }

    @Override
    @Transactional
    public void persist(Player player) {
        em.persist(player);
    }

    @Override
    public boolean existsByUsername(String username) {
        Long count = em.createQuery(
                        "SELECT COUNT(p) FROM Player p WHERE p.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }
}