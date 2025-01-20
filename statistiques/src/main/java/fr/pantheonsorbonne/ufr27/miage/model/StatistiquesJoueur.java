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

    @Column(name = "partiesJouees", nullable = false)
    private Integer partiesJouees;

    @Column(name = "partiesGagnees", nullable = false)
    private Integer partiesGagnees;

    @Column(name = "questionsBonnes", nullable = false)
    private Integer questionsBonnes;

    @Column(name = "questionsTotales", nullable = false)
    private Integer questionsTotales;

    @Column(name = "pourcentageVictoire", nullable = false)
    private Double pourcentageVictoire;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPartiesJouees() {
        return partiesJouees;
    }

    public void setPartiesJouees(Integer partiesJouees) {
        this.partiesJouees = partiesJouees;
    }

    public Integer getPartiesGagnees() {
        return partiesGagnees;
    }

    public void setPartiesGagnees(Integer partiesGagnees) {
        this.partiesGagnees = partiesGagnees;
    }

    public Integer getQuestionsBonnes() {
        return questionsBonnes;
    }

    public void setQuestionsBonnes(Integer questionsBonnes) {
        this.questionsBonnes = questionsBonnes;
    }

    public Integer getQuestionsTotales() {
        return questionsTotales;
    }

    public void setQuestionsTotales(Integer questionsTotales) {
        this.questionsTotales = questionsTotales;
    }

    public Double getPourcentageVictoire() {
        return pourcentageVictoire;
    }

    public void setPourcentageVictoire(Double pourcentageVictoire) {
        this.pourcentageVictoire = pourcentageVictoire;
    }
}
