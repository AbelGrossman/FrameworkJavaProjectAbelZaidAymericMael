package fr.pantheonsorbonne.ufr27.miage.dao;

import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

public interface StatistiquesDAO {

    StatistiquesJoueur getStatistiquesJoueur(String userId);

    StatistiquesJoueur createOrUpdateStatistiquesJoueur(
            String userId, int partiesJouees, int partiesGagnees, int questionsBonnes, int questionsTotales);

    StatistiquesParTheme getStatistiquesParTheme(String userId, String theme);

    StatistiquesParTheme createOrUpdateStatistiquesParTheme(
            String userId, String theme, int partiesJouees, int partiesGagnees, int questionsBonnes, int questionsTotales);
}
