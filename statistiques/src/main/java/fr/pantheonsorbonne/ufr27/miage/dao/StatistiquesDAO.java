package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

public interface StatistiquesDAO {

    StatistiquesJoueur getStatistiquesJoueur(String userId);

    StatistiquesJoueur createOrUpdateStatistiquesJoueur(
            String userId, int nbVictoire, int mmr, double scoreMoyen, double TempRepMoyen, int nbPartie);

    StatistiquesParTheme getStatistiquesParTheme(String userId, String theme);

    StatistiquesParTheme createOrUpdateStatistiquesParTheme(
            String userId, String theme, int nbVictoire, int mmr, double scoreMoyen, double TempRepMoyen, int nbPartie);
}
