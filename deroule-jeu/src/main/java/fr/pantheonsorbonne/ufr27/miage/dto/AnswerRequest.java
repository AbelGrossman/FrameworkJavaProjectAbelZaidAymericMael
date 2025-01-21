package fr.pantheonsorbonne.ufr27.miage.dto;

public class AnswerRequest {
    private String playerId;
    private String answer;
    private long startTime;

    // Getters and Setters

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}