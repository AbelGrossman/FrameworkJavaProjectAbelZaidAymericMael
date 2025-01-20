package fr.pantheonsorbonne.ufr27.miage.dto;

public class StatistiquesRequest {
    private String userId;
    private int rangPartie;
    private int scoreMoyen;
    private int nbQuestions;
    private double tempsRepMoyen;
    private int rang;
    private String theme;

    // Getters et setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRangPartie() {
        return rangPartie;
    }

    public void setRangPartie(int rangPartie) {
        this.rangPartie = rangPartie;
    }

    public int getScoreMoyen() {
        return scoreMoyen;
    }

    public void setScoreMoyen(int scoreMoyen) {
        this.scoreMoyen = scoreMoyen;
    }

    public int getNbQuestions() {
        return nbQuestions;
    }

    public void setNbQuestions(int nbQuestions) {
        this.nbQuestions = nbQuestions;
    }

    public double gettempsRepMoyen() {
        return tempsRepMoyen;
    }

    public void settempsRepMoyen(double tempsRepMoyen) {
        this.tempsRepMoyen = tempsRepMoyen;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
