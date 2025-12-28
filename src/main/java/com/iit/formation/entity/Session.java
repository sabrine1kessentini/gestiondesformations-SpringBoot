package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String nom; // Ex: "S1 2025-2026"
    
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    
    @Column(name = "date_fin")
    private LocalDate dateFin;
    
    @Enumerated(EnumType.STRING)
    private TypeSession type; // SEMESTRE, ANNEE
    
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Cours> cours = new ArrayList<>();
    
    public Session() {}
    
    public Session(String nom, LocalDate dateDebut, LocalDate dateFin, TypeSession type) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public TypeSession getType() {
        return type;
    }
    
    public void setType(TypeSession type) {
        this.type = type;
    }
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
    
    public enum TypeSession {
        SEMESTRE, ANNEE
    }
}






