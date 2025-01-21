package fr.pantheonsorbonne.ufr27.miage.dto;

public class PlayerResultsRequest {
    private String playerId;
    private int score;
    private String averageResponseTime;
    private String category;
    private int totalQuestions;
    private int rank;

    // Getters and Setters

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(String averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}