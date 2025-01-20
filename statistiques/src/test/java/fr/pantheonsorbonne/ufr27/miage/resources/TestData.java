package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;

public record TestData(StatistiquesJoueur statsJoueur, StatistiquesParTheme statsTheme) {

    public static TestData createDefault() {
        // Configuration des StatistiquesJoueur par défaut
        StatistiquesJoueur statsJoueur = new StatistiquesJoueur();
        statsJoueur.setUserId("user1");
        statsJoueur.setPartiesJouees(5);
        statsJoueur.setPartiesGagnees(2);
        statsJoueur.setQuestionsBonnes(20);
        statsJoueur.setQuestionsTotales(25);
        statsJoueur.setPourcentageVictoire(40.0);

        // Configuration des StatistiquesParTheme par défaut
        StatistiquesParTheme statsTheme = new StatistiquesParTheme();
        statsTheme.setUserId("user1");
        statsTheme.setTheme("Histoire");
        statsTheme.setPartiesJouees(3);
        statsTheme.setPartiesGagnees(1);
        statsTheme.setQuestionsBonnes(10);
        statsTheme.setQuestionsTotales(12);

        return new TestData(statsJoueur, statsTheme);
    }
}
