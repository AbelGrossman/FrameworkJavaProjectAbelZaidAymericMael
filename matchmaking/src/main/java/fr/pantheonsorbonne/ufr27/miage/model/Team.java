package fr.pantheonsorbonne.ufr27.miage.model;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
    @Column(name = "players", nullable = false)
    private List<UserWithMmr> players;

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    public Team() {
    }
    
    public Team(String theme, List<UserWithMmr> players, String difficulty) {
        this.theme = theme;
        this.players = players;
        this.difficulty = difficulty;
    }

    public String getTheme() {
        return theme;
    }

    public List<UserWithMmr> getPlayers() {
        return players;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Long getId() {
        return id;
    }
}
