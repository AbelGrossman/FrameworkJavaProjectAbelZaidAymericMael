package fr.pantheonsorbonne.ufr27.miage.model;

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
        name = "team_players", // Name of the table for the players collection
        joinColumns = @JoinColumn(name = "team_id") // Foreign key column linking to Team
    )
    @Column(name = "players", nullable = false)
    private List<Long> players;

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
