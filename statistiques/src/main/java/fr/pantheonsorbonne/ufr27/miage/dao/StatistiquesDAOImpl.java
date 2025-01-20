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
            String userId, int partiesJouees, int partiesGagnees, int questionsBonnes, int questionsTotales) {
        StatistiquesJoueur stats = getStatistiquesJoueur(userId);

        if (stats == null) {
            stats = new StatistiquesJoueur();
            stats.setUserId(userId);
            stats.setPartiesJouees(partiesJouees);
            stats.setPartiesGagnees(partiesGagnees);
            stats.setQuestionsBonnes(questionsBonnes);
            stats.setQuestionsTotales(questionsTotales);
            stats.setPourcentageVictoire(
                    (partiesJouees == 0) ? 0.0 : (double) partiesGagnees / partiesJouees * 100);
            em.persist(stats);
        } else {
            stats.setPartiesJouees(stats.getPartiesJouees() + partiesJouees);
            stats.setPartiesGagnees(stats.getPartiesGagnees() + partiesGagnees);
            stats.setQuestionsBonnes(stats.getQuestionsBonnes() + questionsBonnes);
            stats.setQuestionsTotales(stats.getQuestionsTotales() + questionsTotales);
            stats.setPourcentageVictoire(
                    (partiesJouees == 0) ? 0.0 : (double) partiesGagnees / partiesJouees * 100);
            //stats.setPourcentageVictoire(
            //        (stats.getPartiesJouees() == 0) ? 0.0 : (double) stats.getPartiesGagnees() / stats.getPartiesJouees() * 100);
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
            String userId, String theme, int partiesJouees, int partiesGagnees, int questionsBonnes, int questionsTotales) {
        StatistiquesParTheme stats = getStatistiquesParTheme(userId, theme);

        if (stats == null) {
            stats = new StatistiquesParTheme();
            stats.setUserId(userId);
            stats.setTheme(theme);
            stats.setPartiesJouees(partiesJouees);
            stats.setPartiesGagnees(partiesGagnees);
            stats.setQuestionsBonnes(questionsBonnes);
            stats.setQuestionsTotales(questionsTotales);
            em.persist(stats);
        } else {
            stats.setPartiesJouees(stats.getPartiesJouees() + partiesJouees);
            stats.setPartiesGagnees(stats.getPartiesGagnees() + partiesGagnees);
            stats.setQuestionsBonnes(stats.getQuestionsBonnes() + questionsBonnes);
            stats.setQuestionsTotales(stats.getQuestionsTotales() + questionsTotales);
            em.merge(stats);
        }

        return stats;
    }
}
