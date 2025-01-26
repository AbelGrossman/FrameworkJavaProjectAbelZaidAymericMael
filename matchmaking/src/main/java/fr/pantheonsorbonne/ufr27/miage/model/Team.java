package fr.pantheonsorbonne.ufr27.miage.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "theme", nullable = false)
    private String theme;

    @ElementCollection
    @CollectionTable(
            name = "team_game_players", // Changed table name
            joinColumns = @JoinColumn(name = "team_id")
    )
    @Column(name = "player_id", nullable = false) // Changed column name
    private List<Long> players = new ArrayList<>(); // Initialize list

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    public Team() {
    }
    
    public Team(String theme, List<Long> players, String difficulty) {
        this.theme = theme;
        this.players = players;
        this.difficulty = difficulty;
    }

    public String getTheme() {
        return theme;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Long getId() {
        return id;
    }
}
