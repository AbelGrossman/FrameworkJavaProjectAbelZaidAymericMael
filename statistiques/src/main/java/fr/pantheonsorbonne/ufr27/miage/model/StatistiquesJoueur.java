package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statistiques")
public class StatistiquesJoueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStatistiques", nullable = false)
    private Integer id;

    @Column(name = "userId", nullable = false, unique = true, length = 50)
    private String userId;

    @Column(name = "nbVictoires")
    private Integer nbVictoires;

    @Column(name = "nbPartie")
    private Integer nbPartie;

    @Column(name = "mmr")
    private Integer mmr;

    @Column(name = "scoreMoyen", nullable = false)
    private Double scoreMoyen;

    @Column(name = "tempsRepMoyen", nullable = false)
    private Double tempsRepMoyen;

    // Getters and Setters
    public Integer getId() { return id;}

    public void setId(Integer id) { this.id = id;}

    public String getUserId() { return userId;}

    public void setUserId(String userId) { this.userId = userId;
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

    public Double getTempsRepMoyen() {
        return tempsRepMoyen;
    }

    public void setTempsRepMoyen(Double tempsRepMoyen) {
        this.tempsRepMoyen = tempsRepMoyen;
    }
    public Integer getMmr() {
        return mmr;
    }

    public void setMmr(Integer mmr ) {
        this.mmr = mmr;
    }

}
