package fr.pantheonsorbonne.ufr27.miage.model;
import fr.pantheonsorbonne.ufr27.miage.dto.User; 
import java.util.List;
import java.util.ArrayList;

public class Queue {
    private String theme;
    private List<User> players;

    public Queue(String theme) {
        this.theme = theme;
        this.players = new ArrayList<>();
    }

    public String getTheme() {
        return theme;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void addPlayer(User user) {
        players.add(user);
    }

    public void removePlayer(User user) {
        players.remove(user);
    }

    public boolean isFull() {
        return players.size() >= 50;
    }
}

