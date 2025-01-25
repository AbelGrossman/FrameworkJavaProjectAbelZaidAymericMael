package fr.pantheonsorbonne.ufr27.miage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StatistiquesServiceImpl implements StatistiquesService {

    @Inject
    StatistiquesDAO statistiquesDAO;

    @Inject
    ObjectMapper objectMapper;

    public void processAndUpdateStatistiques(String jsonInput) throws JsonProcessingException {
        PartieDetails[] partieDetailsArray = objectMapper.readValue(jsonInput, PartieDetails[].class);
        for (PartieDetails partieDetails : partieDetailsArray) {
            updateStatistiques(
                    partieDetails.getPlayerId(),
                    partieDetails.getRangPartie(),
                    partieDetails.getScorePartie(),
                    partieDetails.getNbQuestions(),
                    partieDetails.getTheme(),
                    partieDetails.getTempsRepMoyen()
            );
        }
    }

    @Override
    public void updateStatistiques(Long playerId, int rangPartie, int scorePartie, int nbQuestions, String theme, long tempsRepMoyen) {

        StatistiquesJoueur statsJoueur = statistiquesDAO.getStatistiquesJoueur(playerId);

        int nbVictoires = (rangPartie == 1) ? 1 : 0;
        double scoreMoyen = ((double) scorePartie /nbQuestions)*10;
        long nouveauTempsRepMoyen = tempsRepMoyen;
        int newNbPartie = 1;
        // Calcul du MMR
        int mmr = calculateMMR(rangPartie);

        if (statsJoueur != null) {
            if(statsJoueur.getNbPartie() != 0) {
                newNbPartie = statsJoueur.getNbPartie() + 1;
                nbVictoires += statsJoueur.getNbVictoires();
                scoreMoyen = ((statsJoueur.getScoreMoyen() * statsJoueur.getNbPartie()) + scoreMoyen) / newNbPartie;
                nouveauTempsRepMoyen = (long) ((statsJoueur.getTempsRepMoyen() + tempsRepMoyen) / 2.0);
                mmr += statsJoueur.getMmr();
            }
        }


        statistiquesDAO.createOrUpdateStatistiquesJoueur(
                playerId,
                nbVictoires,
                mmr,
                scoreMoyen,
                nouveauTempsRepMoyen,
                newNbPartie
        );

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
        StatistiquesJoueur existingStats = statistiquesDAO.getStatistiquesJoueur(playerId);
        System.out.println("Existing Stats: " + existingStats);

        int initialNbVictoires = 0;
        int initialMMR = 0;
        double initialScoreMoyen = 0.0;
        long initialTempsRepMoyen = 0;
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

        statistiquesDAO.createOrUpdateStatistiquesParTheme(
                playerId,
                theme,
                initialNbVictoires,
                initialMMR,
                initialScoreMoyen,
                initialTempsRepMoyen,
                initialNbPartie
        );
        System.out.println("Created statistics for theme: " + theme);
    }

    private int calculateMMR(int rangPartie) {
        int mmr;
        int maxPoints = 50;
        int minPoints = -40;
        int numberOfPlayers = 6;

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

    public Long convertPlayerId(String player_id) {
        if (player_id == null || player_id.isEmpty()) {
            throw new IllegalArgumentException("Le player_id ne peut pas être null ou vide");
        }
        try {
            return Long.valueOf(player_id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le player_id n'est pas un nombre valide : " + player_id, e);
        }
    }

}
