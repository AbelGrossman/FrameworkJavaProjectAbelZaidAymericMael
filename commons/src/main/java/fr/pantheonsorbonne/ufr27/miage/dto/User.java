package fr.pantheonsorbonne.ufr27.miage.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;
    
    @Column(name="theme", nullable = false)
    private String theme;

    @Column(name="mmr", nullable = false)
    private int mmr;

    public User() {
    }

    public User(Long id, String theme) {
        this.id = id;
        this.theme = theme;
    }

    public User(Long id, String theme, int mmr) {
        this(id, theme);
        this.mmr= mmr;
    }

    public Long getId() {
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
