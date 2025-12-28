package com.iit.formation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"etudiant_id", "cours_id"})
})
public class Inscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date_inscription")
    private LocalDateTime dateInscription;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;
    
    @Enumerated(EnumType.STRING)
    private StatutInscription statut = StatutInscription.ACTIVE;
    
    public Inscription() {
        this.dateInscription = LocalDateTime.now();
    }
    
    public Inscription(Etudiant etudiant, Cours cours) {
        this.etudiant = etudiant;
        this.cours = cours;
        this.dateInscription = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
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
    
    public StatutInscription getStatut() {
        return statut;
    }
    
    public void setStatut(StatutInscription statut) {
        this.statut = statut;
    }
    
    public enum StatutInscription {
        ACTIVE, ANNULEE
    }
}






