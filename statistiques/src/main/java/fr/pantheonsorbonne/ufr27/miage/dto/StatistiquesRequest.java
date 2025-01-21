package fr.pantheonsorbonne.ufr27.miage.dto;

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

    // Classe interne pour la réponse JSON
    public static class StatistiquesResponse {
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
}
