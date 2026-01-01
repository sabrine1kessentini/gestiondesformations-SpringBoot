package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Formateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    public void envoyerEmailInscription(Etudiant etudiant, Cours cours) {
        if (mailSender == null) {
            System.out.println("Email simulé - Inscription: " + etudiant.getEmail() + 
                    " inscrit au cours " + cours.getTitre());
            return;
        }
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(etudiant.getEmail());
        message.setSubject("Inscription au cours: " + cours.getTitre());
        message.setText("Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                "Vous avez été inscrit(e) au cours: " + cours.getTitre() + " (" + cours.getCode() + ").\n\n" +
                "Formateur: " + cours.getFormateur().getPrenom() + " " + cours.getFormateur().getNom() + "\n" +
                "Description: " + cours.getDescription() + "\n\n" +
                "Cordialement,\nL'équipe de gestion de formation");
        
        mailSender.send(message);
    }
    
    public void envoyerEmailDesinscription(Etudiant etudiant, Cours cours, Formateur formateur) {
        if (mailSender == null) {
            System.out.println("Email simulé - Désinscription: " + etudiant.getEmail() + 
                    " désinscrit du cours " + cours.getTitre());
            System.out.println("Notification formateur: " + formateur.getEmail());
            return;
        }
        
        // Email à l'étudiant
        SimpleMailMessage messageEtudiant = new SimpleMailMessage();
        messageEtudiant.setTo(etudiant.getEmail());
        messageEtudiant.setSubject("Annulation d'inscription: " + cours.getTitre());
        messageEtudiant.setText("Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                "Votre inscription au cours " + cours.getTitre() + " a été annulée.\n\n" +
                "Cordialement,\nL'équipe de gestion de formation");
        mailSender.send(messageEtudiant);
        
        // Email au formateur
        SimpleMailMessage messageFormateur = new SimpleMailMessage();
        messageFormateur.setTo(formateur.getEmail());
        messageFormateur.setSubject("Notification: Désinscription d'un étudiant");
        messageFormateur.setText("Bonjour " + formateur.getPrenom() + " " + formateur.getNom() + ",\n\n" +
                "L'étudiant " + etudiant.getPrenom() + " " + etudiant.getNom() + 
                " s'est désinscrit du cours " + cours.getTitre() + ".\n\n" +
                "Cordialement,\nL'équipe de gestion de formation");
        mailSender.send(messageFormateur);
    }
}






