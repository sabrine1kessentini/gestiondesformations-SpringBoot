package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"etudiant_id", "cours_id"})
})
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    private Double valeur;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;
    
    @Column(name = "date_attribution")
    private LocalDateTime dateAttribution;
    
    private String commentaire;
    
    public Note() {
        this.dateAttribution = LocalDateTime.now();
    }
    
    public Note(Double valeur, Etudiant etudiant, Cours cours) {
        this.valeur = valeur;
        this.etudiant = etudiant;
        this.cours = cours;
        this.dateAttribution = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getValeur() {
        return valeur;
    }
    
    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }
    
    public Etudiant getEtudiant() {
        return etudiant;
    }
    
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    
    public Cours getCours() {
        return cours;
    }
    
    public void setCours(Cours cours) {
        this.cours = cours;
    }
    
    public LocalDateTime getDateAttribution() {
        return dateAttribution;
    }
    
    public void setDateAttribution(LocalDateTime dateAttribution) {
        this.dateAttribution = dateAttribution;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}






