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
    public StatistiquesJoueur getStatistiquesJoueur(String userId) {
        try {
            return em.createQuery(
                            "SELECT s FROM StatistiquesJoueur s WHERE s.userId = :userId", StatistiquesJoueur.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public StatistiquesJoueur createOrUpdateStatistiquesJoueur(
            String userId, int nbVictoire, int mmr, double scoreMoyen, double tempsRepMoyen, int nbPartie) {
        StatistiquesJoueur stats = getStatistiquesJoueur(userId);

        if (stats == null) {
            stats = new StatistiquesJoueur();
            stats.setUserId(userId);
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
    public StatistiquesParTheme getStatistiquesParTheme(String userId, String theme) {
        try {
            return em.createQuery(
                            "SELECT s FROM StatistiquesParTheme s WHERE s.userId = :userId AND s.theme = :theme",
                            StatistiquesParTheme.class)
                    .setParameter("userId", userId)
                    .setParameter("theme", theme)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public StatistiquesParTheme createOrUpdateStatistiquesParTheme(
            String userId, String theme, int nbVictoire, int mmr, double scoreMoyen, double tempsRepMoyen, int nbPartie) {
        StatistiquesParTheme stats = getStatistiquesParTheme(userId, theme);

        if (stats == null) {
            stats = new StatistiquesParTheme();
            stats.setUserId(userId);
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
