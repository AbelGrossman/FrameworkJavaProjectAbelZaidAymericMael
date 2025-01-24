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
    private long gameId;

    @Column(name = "score")
    private int score;

    @Column(name = "average_response_time")
    private long averageResponseTime;

    @Column(name = "rank")
    private int rank = 0;

    @Column(name = "category")
    private String category;

    @Column(name = "total_questions")
    private int totalQuestions; 

    // Constructors
    public PlayerResult() {
    }

    public PlayerResult(String playerId, long gameId, int score, long averageResponseTime, int rank, String category, int totalQuestions) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.score = score;
        this.averageResponseTime = averageResponseTime;
        this.rank = rank;
        this.category = category;
        this.totalQuestions = totalQuestions;
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

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

}