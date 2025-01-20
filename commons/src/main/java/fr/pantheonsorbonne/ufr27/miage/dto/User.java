package fr.pantheonsorbonne.ufr27.miage.dto;

public class User {
    private final String id;
    private String theme;
    private int mmr;

    public User(String id, String theme, int mmr) {
        this.id = id;
        this.theme = theme;
        this.mmr = mmr;
    }

    public String getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public int getMmr() {
        return mmr;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setMmr(int mmr) {
        this.mmr = mmr;
    }
}
