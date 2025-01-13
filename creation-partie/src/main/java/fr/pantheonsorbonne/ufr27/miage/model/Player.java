package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String playerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String level;

    // Relation inverse avec Lobby
    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    // Constructeurs
    public Player() {
    }

    public Player(String playerId, String name, String level, Lobby lobby) {
        this.playerId = playerId;
        this.name = name;
        this.level = level;
        this.lobby = lobby;
    }

    // Getters et Setters
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", playerId='" + playerId + '\'' +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", lobby=" + (lobby != null ? lobby.getId() : "null") +
                '}';
    }
}
