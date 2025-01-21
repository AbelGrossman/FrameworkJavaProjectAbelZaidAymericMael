package fr.pantheonsorbonne.ufr27.miage.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usersWithMmr")
public class UserWithMmr {
    
    @Id
    @Column(name="id", nullable=false)
    private Long playerId;

    @Column(name="theme", nullable=false)
    private String theme;

    @Column(name="mmr", nullable=false)
    private int mmr;

    public UserWithMmr(){
    }

    public UserWithMmr(Long userId, String userTheme, int userMmr){
        this.playerId = userId;
        this.theme = userTheme;
        this.mmr = userMmr;
    }

    public Long getPlayerId(){
        return playerId;
    }

    public String getTheme(){
        return theme;
    }

    public int getMmr(){
        return mmr;
    }
    
}
