package com.iit.formation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateProfileDTO {
    
    @NotBlank(message = "Le nom est requis")
    private String nom;
    
    @NotBlank(message = "Le prénom est requis")
    private String prenom;
    
    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;
    
    private String username; // Non modifiable mais affiché
    
    public UpdateProfileDTO() {}
    
    public UpdateProfileDTO(String nom, String prenom, String email, String username) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.username = username;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}

