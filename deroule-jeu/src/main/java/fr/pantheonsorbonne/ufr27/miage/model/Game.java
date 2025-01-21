package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gameId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Player> players;
    private int totalQuestions;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;
    @ElementCollection
    private List<Integer> ranks;

    public Game() {
        // Default constructor
    }

    public Game(String gameId, List<Player> players, int totalQuestions, List<Question> questions) {
        this.gameId = gameId;
        this.players = players;
        this.totalQuestions = totalQuestions;
        this.questions = questions;
        this.ranks = List.of(0, 0, 0, 0, 0, 0); // Initialize ranks with zeros
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Integer> getRanks() {
        return ranks;
    }

    public void setRanks(List<Integer> ranks) {
        this.ranks = ranks;
    }

    public Question getCurrentQuestion(int index) {
        if (questions != null && index < questions.size()) {
            return questions.get(index);
        }
        return null;
    }
}