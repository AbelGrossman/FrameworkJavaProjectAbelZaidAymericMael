package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statistiques")
public class StatistiquesJoueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStatistiques", nullable = false)
    private Integer id;

    @Column(name = "playerId", nullable = false, unique = true, length = 50)
    private Long playerId;

    @Column(name = "nbVictoires")
    private Integer nbVictoires;

    @Column(name = "nbPartie")
    private Integer nbPartie;

    @Column(name = "mmr")
    private Integer mmr;

    @Column(name = "scoreMoyen", nullable = false)
    private Double scoreMoyen;

    @Column(name = "tempsRepMoyen", nullable = false)
    private long tempsRepMoyen;

    // Getters and Setters
    public Integer getId() { return id;}

    public void setId(Integer id) { this.id = id;}

    public Long getPlayerId() { return playerId;}

    public void setPlayerId(Long playerId) { this.playerId = playerId;
    }

    public Integer getNbVictoires() {return nbVictoires;}

    public void setNbVictoires(Integer nbVictoires) {this.nbVictoires = nbVictoires;}


    public Double getScoreMoyen() {return scoreMoyen;}

    public void setScoreMoyen(Double scoreMoyen) {this.scoreMoyen = scoreMoyen;}

    public Integer getNbPartie() {
        return nbPartie;
    }

    public void setNbPartie(Integer nbPartie) {
        this.nbPartie = nbPartie;
    }

    public long getTempsRepMoyen() {
        return tempsRepMoyen;
    }

    public void setTempsRepMoyen(long tempsRepMoyen) {
        this.tempsRepMoyen = tempsRepMoyen;
    }
    public Integer getMmr() {
        return mmr;
    }

    public void setMmr(Integer mmr ) {
        this.mmr = mmr;
    }

}
