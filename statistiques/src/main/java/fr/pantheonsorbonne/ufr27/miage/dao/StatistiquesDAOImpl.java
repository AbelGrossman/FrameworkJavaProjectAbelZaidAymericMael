package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StatistiquesDAOImpl implements StatistiquesDAO {

    @PersistenceContext(name = "mysql")
    EntityManager em;

    @Override
    public StatistiquesJoueur getStatistiquesJoueur(Long playerId) {
        try {
            return em.createQuery(
                            "SELECT s FROM StatistiquesJoueur s WHERE s.playerId = :playerId", StatistiquesJoueur.class)
                    .setParameter("playerId", playerId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public StatistiquesJoueur createOrUpdateStatistiquesJoueur(
            Long playerId, int nbVictoire, int mmr, double scoreMoyen, double tempsRepMoyen, int nbPartie) {
        StatistiquesJoueur stats = getStatistiquesJoueur(playerId);

        if (stats == null) {
            stats = new StatistiquesJoueur();
            stats.setPlayerId(playerId);
            stats.setNbVictoires(nbVictoire);
            stats.setMmr(mmr);
            stats.setScoreMoyen(scoreMoyen);
            stats.setTempsRepMoyen(tempsRepMoyen);
            stats.setNbPartie(nbPartie);
            em.persist(stats);
        } else {
            stats.setNbVictoires(nbVictoire);
            stats.setMmr(mmr);
            stats.setScoreMoyen(scoreMoyen);
            stats.setTempsRepMoyen(tempsRepMoyen);
            stats.setNbPartie(nbPartie);
            em.merge(stats);
        }

        return stats;
    }

    @Override
    public StatistiquesParTheme getStatistiquesParTheme(Long playerId, String theme) {
        try {
            return em.createQuery(
                            "SELECT s FROM StatistiquesParTheme s WHERE s.playerId = :playerId AND s.theme = :theme",
                            StatistiquesParTheme.class)
                    .setParameter("playerId", playerId)
                    .setParameter("theme", theme)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public StatistiquesParTheme createOrUpdateStatistiquesParTheme(
            Long playerId, String theme, int nbVictoire, int mmr, double scoreMoyen, double tempsRepMoyen, int nbPartie) {
        StatistiquesParTheme stats = getStatistiquesParTheme(playerId, theme);

        if (stats == null) {
            stats = new StatistiquesParTheme();
            stats.setPlayerId(playerId);
            stats.setTheme(theme);
            stats.setNbVictoires(nbVictoire);
            stats.setMmr(mmr);
            stats.setScoreMoyen(scoreMoyen);
            stats.setTempsRepMoyen(tempsRepMoyen);
            stats.setNbPartie(nbPartie);
            em.persist(stats);
        } else {
            stats.setNbVictoires(nbVictoire);
            stats.setMmr(mmr);
            stats.setScoreMoyen(scoreMoyen);
            stats.setTempsRepMoyen(tempsRepMoyen);
            stats.setNbPartie(nbPartie);
            em.merge(stats);
        }

        return stats;
    }
}
