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
    public void updateStatistiques(Long playerId, int rangPartie, int scorePartie, int nbQuestions, String theme, double tempsRepMoyen) {
        // Récupération des statistiques actuelles
        StatistiquesJoueur statsJoueur = statistiquesDAO.getStatistiquesJoueur(playerId);

        int nbVictoires = (rangPartie == 1) ? 1 : 0;
        double scoreMoyen = ((double) scorePartie /nbQuestions)*10;
        double nouveauTempsRepMoyen = tempsRepMoyen;
        int newNbPartie = 1;
        // Calcul du MMR
        int mmr = calculateMMR(rangPartie);

        if (statsJoueur != null) {
            if(statsJoueur.getNbPartie() != 0) {
                newNbPartie = statsJoueur.getNbPartie() + 1;
                nbVictoires += statsJoueur.getNbVictoires();
                scoreMoyen = ((statsJoueur.getScoreMoyen() * statsJoueur.getNbPartie()) + scoreMoyen) / newNbPartie;
                ;
                nouveauTempsRepMoyen = (statsJoueur.getTempsRepMoyen() + tempsRepMoyen) / 2.0;
                mmr += statsJoueur.getMmr();
            }
        }

        // Mise à jour des statistiques globales
        statistiquesDAO.createOrUpdateStatistiquesJoueur(
                playerId,
                nbVictoires,
                mmr,
                scoreMoyen,
                nouveauTempsRepMoyen,
                newNbPartie
        );

        // Mise à jour des statistiques par thème
        statistiquesDAO.createOrUpdateStatistiquesParTheme(
                playerId,
                theme,
                nbVictoires,
                mmr,
                scoreMoyen,
                nouveauTempsRepMoyen,
                newNbPartie
        );
    }

    public void createStatistiqueUser(Long playerId, String theme) {
        // Vérification si les statistiques globales existent déjà
        StatistiquesJoueur existingStats = statistiquesDAO.getStatistiquesJoueur(playerId);
        System.out.println("Existing Stats: " + existingStats);
        // Création des statistiques globales si elles n'existent pas

        int initialNbVictoires = 0;
        int initialMMR = 0;
        double initialScoreMoyen = 0.0;
        double initialTempsRepMoyen = 0.0;
        int initialNbPartie = 0;
        if (existingStats == null) {

            statistiquesDAO.createOrUpdateStatistiquesJoueur(
                    playerId,
                    initialNbVictoires,
                    initialMMR,
                    initialScoreMoyen,
                    initialTempsRepMoyen,
                    initialNbPartie
            );
            System.out.println("Created global stats for player " + playerId);
        }

        // Création des statistiques par thème
        statistiquesDAO.createOrUpdateStatistiquesParTheme(
                playerId,
                theme,
                initialNbVictoires,
                initialMMR,
                initialScoreMoyen,
                initialTempsRepMoyen,
                initialNbPartie
        );
        System.out.println("Created statistics for theme: " + theme); // Log pour création par thème
    }

    private int calculateMMR(int rangPartie) {
        int mmr;
        int maxPoints = 50;
        int minPoints = -40;
        int numberOfPlayers = 6; // Exemple : nombre total de joueurs dans la partie

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
    public StatistiquesJoueur getStatistiquesJoueur(Long playerId) {
        return statistiquesDAO.getStatistiquesJoueur(playerId);
    }

    @Override
    public StatistiquesParTheme getStatistiquesParTheme(Long playerId, String theme) {
        return statistiquesDAO.getStatistiquesParTheme(playerId, theme);
    }
}
