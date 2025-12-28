package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groupes")
public class Groupe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String nom;
    
    private String description;
    
    @OneToMany(mappedBy = "groupe", cascade = CascadeType.ALL)
    private List<Etudiant> etudiants = new ArrayList<>();
    
    @ManyToMany(mappedBy = "groupes")
    private List<Cours> cours = new ArrayList<>();
    
    public Groupe() {}
    
    public Groupe(String nom, String description) {
        this.nom = nom;
        this.description = description;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<Etudiant> getEtudiants() {
        return etudiants;
    }
    
    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}






