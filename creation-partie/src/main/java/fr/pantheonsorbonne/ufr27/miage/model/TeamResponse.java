package fr.pantheonsorbonne.ufr27.miage.model;


import fr.pantheonsorbonne.ufr27.miage.dto.TeamResponseDto;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class TeamResponse {
    @Id
    private String id;
    private String theme;
    private String difficulty;

    @ElementCollection
    private List<Long> players;

    public TeamResponse() {}

    public TeamResponse(TeamResponseDto dto) {
        this.id = dto.id();
        this.theme = dto.theme();
        this.difficulty = dto.difficulty();
        this.players = dto.players();
    }

    public TeamResponseDto toDto() {
        return new TeamResponseDto(id, theme, players, difficulty);
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public List<Long> getPlayers() { return players; }
    public void setPlayers(List<Long> players) { this.players = players; }
}
