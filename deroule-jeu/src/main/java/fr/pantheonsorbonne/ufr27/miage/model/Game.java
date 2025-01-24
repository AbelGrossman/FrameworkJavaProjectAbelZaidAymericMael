package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String difficulty;
    private int totalQuestions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Player> players;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;

    @ElementCollection
    private List<Integer> ranks;

    @Column(columnDefinition = "boolean default false")
    private boolean isOver = false;

    public Game() {
        // Default constructor
    }

    public Game(String category, String difficulty, List<Player> players, int totalQuestions,
            List<Question> questions) {
        this.category = category;
        this.difficulty = difficulty;
        this.players = players;
        this.totalQuestions = totalQuestions;
        this.questions = questions;
        this.ranks = List.of(0, 0, 0, 0, 0, 0);
    }

    // Existing getters and setters for players
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    // New getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // Existing getters and setters
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

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }
}