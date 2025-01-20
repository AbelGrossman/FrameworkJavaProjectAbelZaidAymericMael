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
    public void updateStatistiques(String userId, int rangPartie, int scorePartie, int nbQuestions, String theme, double tempsRepMoyen) {
        // Récupération des statistiques actuelles
        StatistiquesJoueur statsJoueur = statistiquesDAO.getStatistiquesJoueur(userId);



        int nbVictoires = (rangPartie == 1) ? 1 : 0;
        double scoreMoyen = ((double) scorePartie /nbQuestions)*10;
        double nouveauTempsRepMoyen = tempsRepMoyen;
        int newNbPartie = 1;

        if (statsJoueur != null) {
            newNbPartie = statsJoueur.getNbPartie() + 1;
            nbVictoires += statsJoueur.getNbVictoires();
            scoreMoyen = ((statsJoueur.getScoreMoyen() * statsJoueur.getNbPartie()) + scoreMoyen) / newNbPartie;;
            nouveauTempsRepMoyen = (statsJoueur.getTempsRepMoyen() + tempsRepMoyen) / 2.0;


        }

        // Calcul du MMR
        int mmr = calculateMMR(rangPartie);
        if (statsJoueur != null) {
            mmr += statsJoueur.getMmr();
        }

        // Mise à jour des statistiques globales
        statistiquesDAO.createOrUpdateStatistiquesJoueur(
                userId,
                nbVictoires,
                mmr,
                scoreMoyen,
                nouveauTempsRepMoyen,
                newNbPartie
        );

        // Mise à jour des statistiques par thème
        statistiquesDAO.createOrUpdateStatistiquesParTheme(
                userId,
                theme,
                nbVictoires,
                mmr,
                scoreMoyen,
                nouveauTempsRepMoyen,
                newNbPartie
        );
    }

    private int calculateMMR(int rangPartie) {
        int mmr;
        int maxPoints = 50;
        int minPoints = -40;
        int numberOfPlayers = 5; // Exemple : nombre total de joueurs dans la partie

        if (rangPartie == 1) {
            mmr = maxPoints;
        } else if (rangPartie == numberOfPlayers) {
            mmr = minPoints;
        } else {
            double range = maxPoints - minPoints;
            double step = range / (numberOfPlayers - 1);
            mmr = (int) (maxPoints - step * (rangPartie - 1));
        }
        return mmr;
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
