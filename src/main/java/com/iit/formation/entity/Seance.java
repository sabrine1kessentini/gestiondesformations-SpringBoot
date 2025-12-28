package com.iit.formation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seances")
public class Seance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date_heure_debut")
    private LocalDateTime dateHeureDebut;
    
    @Column(name = "date_heure_fin")
    private LocalDateTime dateHeureFin;
    
    private String salle;
    
    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;
    
    private String type; // CM, TD, TP
    
    public Seance() {}
    
    public Seance(LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin, String salle, Cours cours) {
        this.dateHeureDebut = dateHeureDebut;
        this.dateHeureFin = dateHeureFin;
        this.salle = salle;
        this.cours = cours;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }
    
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }
    
    public LocalDateTime getDateHeureFin() {
        return dateHeureFin;
    }
    
    public void setDateHeureFin(LocalDateTime dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }
    
    public String getSalle() {
        return salle;
    }
    
    public void setSalle(String salle) {
        this.salle = salle;
    }
    
    public Cours getCours() {
        return cours;
    }
    
    public void setCours(Cours cours) {
        this.cours = cours;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}






