package com.iit.formation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "formateur_id", nullable = false)
    private Formateur formateur;
    
    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification type;
    
    @Column(nullable = false)
    private String message;
    
    @Column(nullable = false)
    private boolean lue = false;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;
    
    public Notification() {
        this.dateCreation = LocalDateTime.now();
    }
    
    public Notification(Formateur formateur, Cours cours, Etudiant etudiant, TypeNotification type, String message) {
        this.formateur = formateur;
        this.cours = cours;
        this.etudiant = etudiant;
        this.type = type;
        this.message = message;
        this.dateCreation = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Formateur getFormateur() {
        return formateur;
    }
    
    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }
    
    public Cours getCours() {
        return cours;
    }
    
    public void setCours(Cours cours) {
        this.cours = cours;
    }
    
    public Etudiant getEtudiant() {
        return etudiant;
    }
    
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    
    public TypeNotification getType() {
        return type;
    }
    
    public void setType(TypeNotification type) {
        this.type = type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isLue() {
        return lue;
    }
    
    public void setLue(boolean lue) {
        this.lue = lue;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public enum TypeNotification {
        INSCRIPTION,
        DESINSCRIPTION
    }
}

