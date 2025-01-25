package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

public interface StatistiquesDAO {

    StatistiquesJoueur getStatistiquesJoueur(Long playerId);

    StatistiquesJoueur createOrUpdateStatistiquesJoueur(
            Long playerId, int nbVictoire, int mmr, double scoreMoyen, long TempRepMoyen, int nbPartie);

    StatistiquesParTheme getStatistiquesParTheme(Long playerId, String theme);

    StatistiquesParTheme createOrUpdateStatistiquesParTheme(
            Long playerId, String theme, int nbVictoire, int mmr, double scoreMoyen, long TempRepMoyen, int nbPartie);
}
