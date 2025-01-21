package fr.pantheonsorbonne.ufr27.miage.dto;

public class PartieDetails {

    private Long playerId;
    private int rangPartie;
    private int scorePartie;
    private int nbQuestions;
    private String theme;
    private double tempsRepMoyen;

    // Getters et Setters
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public int getRangPartie() {
        return rangPartie;
    }

    public void setRangPartie(int rangPartie) {
        this.rangPartie = rangPartie;
    }

    public int getScorePartie() {
        return scorePartie;
    }

    public void setScorePartie(int scorePartie) {
        this.scorePartie = scorePartie;
    }

    public int getNbQuestions() {
        return nbQuestions;
    }

    public void setNbQuestions(int nbQuestions) {
        this.nbQuestions = nbQuestions;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public double getTempsRepMoyen() {
        return tempsRepMoyen;
    }

    public void setTempsRepMoyen(double tempsRepMoyen) {
        this.tempsRepMoyen = tempsRepMoyen;
    }
}
