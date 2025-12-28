package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specialites")
public class Specialite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String nom;
    
    private String description;
    
    @OneToMany(mappedBy = "specialite", cascade = CascadeType.ALL)
    private List<Cours> cours = new ArrayList<>();
    
    public Specialite() {}
    
    public Specialite(String nom, String description) {
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
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}






