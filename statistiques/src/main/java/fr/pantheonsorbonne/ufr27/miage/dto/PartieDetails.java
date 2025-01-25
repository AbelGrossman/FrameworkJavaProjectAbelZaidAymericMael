package fr.pantheonsorbonne.ufr27.miage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.pantheonsorbonne.ufr27.miage.service.StatistiquesServiceImpl;

public class PartieDetails {

    @JsonProperty("playerId")
    private String playerId;

    @JsonProperty("score")
    private int scorePartie;

    @JsonProperty("gameId")
    private String gameId;

    @JsonProperty("averageResponseTime")
    private long tempsRepMoyen;

    @JsonProperty("category")
    private String theme;

    @JsonProperty("rank")
    private int rangPartie;

    @JsonProperty("totalQuestions")
    private int nbQuestions;

    public Long getPlayerId() {
        StatistiquesServiceImpl statistiquesService = new StatistiquesServiceImpl();
        return statistiquesService.convertPlayerId(playerId);
    }

    public void setPlayerId(String playerId) {
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

    public long getTempsRepMoyen() {
        return tempsRepMoyen;
    }

    public void setTempsRepMoyen(long tempsRepMoyen) {
        this.tempsRepMoyen = tempsRepMoyen;
    }
}
