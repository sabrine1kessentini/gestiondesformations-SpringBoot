package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "etudiants")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Etudiant extends Utilisateur {
    
    @NotBlank
    @Column(unique = true)
    private String matricule;
    
    @Column(name = "date_inscription")
    private LocalDate dateInscription;
    
    @ManyToOne
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;
    
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();
    
    public Etudiant() {
        super();
        this.dateInscription = LocalDate.now();
    }
    
    public Etudiant(String matricule, String username, String password, String email, String nom, String prenom) {
        super(username, password, email, nom, prenom);
        this.matricule = matricule;
        this.dateInscription = LocalDate.now();
        this.addRole(Utilisateur.Role.ETUDIANT);
    }
    
    // Getters and Setters
    public String getMatricule() {
        return matricule;
    }
    
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    
    public LocalDate getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public Groupe getGroupe() {
        return groupe;
    }
    
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }
    
    public List<Inscription> getInscriptions() {
        return inscriptions;
    }
    
    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
    
    public List<Note> getNotes() {
        return notes;
    }
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    public double getMoyenneGenerale() {
        if (notes == null || notes.isEmpty()) {
            return 0.0;
        }
        return notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);
    }
}






