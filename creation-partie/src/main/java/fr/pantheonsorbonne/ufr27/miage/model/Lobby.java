package fr.pantheonsorbonne.ufr27.miage.model;


import java.util.List;
import jakarta.persistence.*;

@Entity
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String theme;
    private String level;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players;

    // Constructeurs, getters, setters
    public Lobby() {}

    public Lobby(String theme, String level, List<Player> players) {
        this.theme = theme;
        this.level = level;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
