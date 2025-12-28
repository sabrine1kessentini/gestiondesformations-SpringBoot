package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String username;
    
    @NotBlank
    private String password;
    
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    
    @NotBlank
    private String nom;
    
    @NotBlank
    private String prenom;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "utilisateur_roles", joinColumns = @JoinColumn(name = "utilisateur_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();
    
    private boolean active = true;
    
    public Utilisateur() {}
    
    public Utilisateur(String username, String password, String email, String nom, String prenom) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public void addRole(Role role) {
        this.roles.add(role);
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public enum Role {
        ADMIN, FORMATEUR, ETUDIANT
    }
}






