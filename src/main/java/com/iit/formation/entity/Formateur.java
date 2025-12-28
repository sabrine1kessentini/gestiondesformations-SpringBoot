package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formateurs")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Formateur extends Utilisateur {
    
    @NotBlank
    private String specialite;
    
    @OneToMany(mappedBy = "formateur", cascade = CascadeType.ALL)
    private List<Cours> cours = new ArrayList<>();
    
    public Formateur() {
        super();
    }
    
    public Formateur(String username, String password, String email, String nom, String prenom, String specialite) {
        super(username, password, email, nom, prenom);
        this.specialite = specialite;
        this.addRole(Utilisateur.Role.FORMATEUR);
    }
    
    // Getters and Setters
    public String getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    public List<Cours> getCours() {
        return cours;
    }
    
    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}






