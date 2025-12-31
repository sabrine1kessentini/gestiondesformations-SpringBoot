package com.iit.formation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité pour stocker les emails simulés (mock)
 */
@Entity
@Table(name = "mock_emails")
public class MockEmail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fromEmail;
    
    @Column(nullable = false)
    private String toEmail;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;
    
    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi;
    
    @Column(name = "est_lu", nullable = false)
    private boolean estLu;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEmail typeEmail;
    
    // Informations contextuelles
    @Column(name = "etudiant_nom")
    private String etudiantNom;
    
    @Column(name = "cours_titre")
    private String coursTitre;
    
    @Column(name = "cours_code")
    private String coursCode;
    
    public MockEmail() {
        this.dateEnvoi = LocalDateTime.now();
        this.estLu = false;
    }
    
    public MockEmail(String fromEmail, String toEmail, String subject, String body, TypeEmail typeEmail) {
        this();
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
        this.typeEmail = typeEmail;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFromEmail() {
        return fromEmail;
    }
    
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
    
    public String getToEmail() {
        return toEmail;
    }
    
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }
    
    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
    
    public boolean isEstLu() {
        return estLu;
    }
    
    public void setEstLu(boolean estLu) {
        this.estLu = estLu;
    }
    
    public TypeEmail getTypeEmail() {
        return typeEmail;
    }
    
    public void setTypeEmail(TypeEmail typeEmail) {
        this.typeEmail = typeEmail;
    }
    
    public String getEtudiantNom() {
        return etudiantNom;
    }
    
    public void setEtudiantNom(String etudiantNom) {
        this.etudiantNom = etudiantNom;
    }
    
    public String getCoursTitre() {
        return coursTitre;
    }
    
    public void setCoursTitre(String coursTitre) {
        this.coursTitre = coursTitre;
    }
    
    public String getCoursCode() {
        return coursCode;
    }
    
    public void setCoursCode(String coursCode) {
        this.coursCode = coursCode;
    }
    
    public enum TypeEmail {
        INSCRIPTION_ETUDIANT,
        INSCRIPTION_FORMATEUR,
        DESINSCRIPTION_ETUDIANT,
        DESINSCRIPTION_FORMATEUR
    }
}

