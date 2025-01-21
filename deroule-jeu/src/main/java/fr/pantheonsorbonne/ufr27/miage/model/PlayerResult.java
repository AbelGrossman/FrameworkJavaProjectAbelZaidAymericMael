package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "player_results")
public class PlayerResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "game_id")
    private String gameId;

    @Column(name = "score")
    private int score;

    @Column(name = "average_response_time")
    private long averageResponseTime;

    // Constructors
    public PlayerResult() {
    }

    public PlayerResult(String playerId, String gameId, int score, long averageResponseTime) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.score = score;
        this.averageResponseTime = averageResponseTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(long averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }
}