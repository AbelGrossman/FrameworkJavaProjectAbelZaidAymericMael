package fr.pantheonsorbonne.ufr27.miage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statistiques_theme")
public class StatistiquesParTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStatistiquesTheme", nullable = false)
    private Integer id;

    @Column(name = "userId", nullable = false, unique = true,length = 50)
    private String userId;

    @Column(name = "theme", nullable = false, length = 45)
    private String theme;

    @Column(name = "partiesJouees", nullable = false)
    private Integer partiesJouees;

    @Column(name = "partiesGagnees", nullable = false)
    private Integer partiesGagnees;

    @Column(name = "questionsBonnes", nullable = false)
    private Integer questionsBonnes;

    @Column(name = "questionsTotales", nullable = false)
    private Integer questionsTotales;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
}
