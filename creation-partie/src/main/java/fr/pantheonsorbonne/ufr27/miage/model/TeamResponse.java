package fr.pantheonsorbonne.ufr27.miage.model;


import fr.pantheonsorbonne.ufr27.miage.dto.TeamResponseDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TeamResponse")
public class TeamResponse {
    @Id
    private String id;
    private String theme;
    private String difficulty;

    @ElementCollection
    @CollectionTable(
            name = "team_response_players", // Changed table name
            joinColumns = @JoinColumn(name = "team_id")
    )
    @Column(name = "player_id")
    private List<Long> players = new ArrayList<>();

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
