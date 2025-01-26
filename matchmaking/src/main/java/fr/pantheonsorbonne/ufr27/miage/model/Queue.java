package fr.pantheonsorbonne.ufr27.miage.model;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;

import java.util.List;
import java.util.ArrayList;


public class Queue{

    private String theme;

    private List<UserWithMmr> players;

    private int allowedMmrDifference = 20;
    
    public Queue(@ConfigProperty(name = "queue.theme") String theme) {
        this.theme = theme;
        this.players = new ArrayList<>();
    }

    public String getTheme() {
        return theme;
    }

    public List<UserWithMmr> getPlayers() {
        return players;
    }

    public void addPlayer(UserWithMmr user) {
        players.add(user);
    }

    public void removePlayer(UserWithMmr user) {
        players.remove(user);
    }

    public boolean isFull() {
        return players.size() >= 50;
    }

    public void setAllowedMmrDifference(int allowedMmrDifference){
        this.allowedMmrDifference = allowedMmrDifference; 
    }

    public int getAllowedMmrDifference(){
        return this.allowedMmrDifference;
    }
}



