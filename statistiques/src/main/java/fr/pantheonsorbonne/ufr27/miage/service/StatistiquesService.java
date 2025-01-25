package fr.pantheonsorbonne.ufr27.miage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

public interface StatistiquesService {

    void updateStatistiques(Long playerId, int rangPartie, int scorePartie, int nbQuestions, String theme, long tempsRepMoyen);

    void createStatistiqueUser(Long playerId, String theme);

    StatistiquesJoueur getStatistiquesJoueur(Long playerId);

    StatistiquesParTheme getStatistiquesParTheme(Long playerId, String theme);

    void processAndUpdateStatistiques(String jsonInput) throws JsonProcessingException;
}
