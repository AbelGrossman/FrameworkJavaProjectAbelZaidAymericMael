package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
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

    private String teamId;

    public Game() {
    }

    public Game(String category, String difficulty, List<Player> players, int totalQuestions,
                List<Question> questions, String teamId) {
        this.category = category;
        this.difficulty = difficulty;
        this.players = new ArrayList<>(players);  // Create mutable list
        this.totalQuestions = totalQuestions;
        this.questions = new ArrayList<>(questions); // Create mutable list
        this.ranks = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0)); // Create mutable list
        this.teamId = teamId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}