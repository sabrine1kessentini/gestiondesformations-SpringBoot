package com.iit.formation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cours")
public class Cours {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String code;
    
    @NotBlank
    private String titre;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "formateur_id", nullable = false)
    private Formateur formateur;
    
    @ManyToOne
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;
    
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    
    @ManyToMany
    @JoinTable(
        name = "cours_groupes",
        joinColumns = @JoinColumn(name = "cours_id"),
        inverseJoinColumns = @JoinColumn(name = "groupe_id")
    )
    private List<Groupe> groupes = new ArrayList<>();
    
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();
    
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seance> seances = new ArrayList<>();
    
    public Cours() {}
    
    public Cours(String code, String titre, String description, Formateur formateur) {
        this.code = code;
        this.titre = titre;
        this.description = description;
        this.formateur = formateur;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Formateur getFormateur() {
        return formateur;
    }
    
    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }
    
    public Specialite getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }
    
    public Session getSession() {
        return session;
    }
    
    public void setSession(Session session) {
        this.session = session;
    }
    
    public List<Groupe> getGroupes() {
        return groupes;
    }
    
    public void setGroupes(List<Groupe> groupes) {
        this.groupes = groupes;
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
    
    public List<Seance> getSeances() {
        return seances;
    }
    
    public void setSeances(List<Seance> seances) {
        this.seances = seances;
    }
    
    public int getNombreInscrits() {
        return inscriptions != null ? inscriptions.size() : 0;
    }
    
    public double getTauxReussite() {
        if (notes == null || notes.isEmpty()) {
            return 0.0;
        }
        long notesReussies = notes.stream()
                .filter(n -> n.getValeur() >= 10.0)
                .count();
        return (double) notesReussies / notes.size() * 100.0;
    }
}






