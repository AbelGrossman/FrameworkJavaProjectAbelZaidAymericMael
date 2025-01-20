package fr.pantheonsorbonne.ufr27.miage.dto;

public class StatistiquesRequest {
    private String userId;
    private int rangPartie;
    private int nbBonnesReponses;
    private int nbQuestions;
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

    public int getNbBonnesReponses() {
        return nbBonnesReponses;
    }

    public void setNbBonnesReponses(int nbBonnesReponses) {
        this.nbBonnesReponses = nbBonnesReponses;
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
}
