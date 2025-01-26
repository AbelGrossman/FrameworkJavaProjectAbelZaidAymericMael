package fr.pantheonsorbonne.ufr27.miage.dto;


import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StatistiquesRequest {

    private Long playerId;
    private String theme;

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

}