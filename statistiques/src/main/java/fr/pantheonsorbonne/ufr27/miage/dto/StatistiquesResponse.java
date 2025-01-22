package fr.pantheonsorbonne.ufr27.miage.dto;


import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StatistiquesResponse {
    private Long playerId;
    private String theme;
    private Integer mmr;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Integer getMmr() {
        return mmr;
    }

    public void setMmr(Integer mmr) {
        this.mmr = mmr;
    }
}