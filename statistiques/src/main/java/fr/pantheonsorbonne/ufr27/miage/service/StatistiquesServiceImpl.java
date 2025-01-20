package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StatistiquesServiceImpl implements StatistiquesService {

    @Inject
    StatistiquesDAO statistiquesDAO;

    @Override
    public void updateStatistiques(String userId, int rangPartie, int nbBonnesReponses, int nbQuestions, String theme) {
        // Mise à jour des statistiques globales
        StatistiquesJoueur statsJoueur = statistiquesDAO.getStatistiquesJoueur(userId);
        int partiesJouees = 1;
        int partiesGagnees = (rangPartie == 1) ? 1 : 0;

        if (statsJoueur == null) {
            statsJoueur = statistiquesDAO.createOrUpdateStatistiquesJoueur(userId, partiesJouees, partiesGagnees, nbBonnesReponses, nbQuestions);
        } else {
            statsJoueur = statistiquesDAO.createOrUpdateStatistiquesJoueur(
                    userId,
                    partiesJouees,
                    partiesGagnees,
                    nbBonnesReponses,
                    nbQuestions
            );
        }

        // Mise à jour des statistiques par thème
        StatistiquesParTheme statsTheme = statistiquesDAO.getStatistiquesParTheme(userId, theme);
        if (statsTheme == null) {
            statsTheme = statistiquesDAO.createOrUpdateStatistiquesParTheme(
                    userId,
                    theme,
                    partiesJouees,
                    partiesGagnees,
                    nbBonnesReponses,
                    nbQuestions
            );
        } else {
            statsTheme = statistiquesDAO.createOrUpdateStatistiquesParTheme(
                    userId,
                    theme,
                    partiesJouees,
                    partiesGagnees,
                    nbBonnesReponses,
                    nbQuestions
            );
        }
    }

    @Override
    public StatistiquesJoueur getStatistiquesJoueur(String userId) {
        return statistiquesDAO.getStatistiquesJoueur(userId);
    }

    @Override
    public StatistiquesParTheme getStatistiquesParTheme(String userId, String theme) {
        return statistiquesDAO.getStatistiquesParTheme(userId, theme);
    }
}
