package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

public interface StatistiquesService {

    void updateStatistiques(String userId, int rangPartie, int scorePartie, int nbQuestions, String theme, double tempsRepMoyen);

    StatistiquesJoueur getStatistiquesJoueur(String userId);

    StatistiquesParTheme getStatistiquesParTheme(String userId, String theme);
}
