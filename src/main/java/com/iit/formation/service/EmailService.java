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
            if (cours.getFormateur() != null) {
                System.out.println("Notification formateur: " + cours.getFormateur().getEmail() + 
                        " - Nouvel étudiant inscrit: " + etudiant.getPrenom() + " " + etudiant.getNom());
            }
            return;
        }
        
        // Email à l'étudiant
        SimpleMailMessage messageEtudiant = new SimpleMailMessage();
        messageEtudiant.setTo(etudiant.getEmail());
        messageEtudiant.setSubject("Inscription au cours: " + cours.getTitre());
        messageEtudiant.setText("Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                "Vous avez été inscrit(e) au cours: " + cours.getTitre() + " (" + cours.getCode() + ").\n\n" +
                (cours.getFormateur() != null ? 
                    "Formateur: " + cours.getFormateur().getPrenom() + " " + cours.getFormateur().getNom() + "\n" : "") +
                (cours.getDescription() != null && !cours.getDescription().isEmpty() ? 
                    "Description: " + cours.getDescription() + "\n" : "") +
                "\nNous vous souhaitons une excellente formation !\n\n" +
                "Cordialement,\nL'équipe de gestion de formation");
        mailSender.send(messageEtudiant);
        
        // Email au formateur pour notification
        if (cours.getFormateur() != null && cours.getFormateur().getEmail() != null) {
            SimpleMailMessage messageFormateur = new SimpleMailMessage();
            messageFormateur.setTo(cours.getFormateur().getEmail());
            messageFormateur.setSubject("Notification: Nouvel étudiant inscrit au cours " + cours.getTitre());
            messageFormateur.setText("Bonjour " + cours.getFormateur().getPrenom() + " " + cours.getFormateur().getNom() + ",\n\n" +
                    "Un nouvel étudiant s'est inscrit à votre cours.\n\n" +
                    "Étudiant: " + etudiant.getPrenom() + " " + etudiant.getNom() + "\n" +
                    "Email: " + etudiant.getEmail() + "\n" +
                    "Cours: " + cours.getTitre() + " (" + cours.getCode() + ")\n" +
                    "Date d'inscription: " + java.time.LocalDateTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")) + "\n\n" +
                    "Cordialement,\nL'équipe de gestion de formation");
            mailSender.send(messageFormateur);
        }
    }
    
    public void envoyerEmailDesinscription(Etudiant etudiant, Cours cours, Formateur formateur) {
        if (mailSender == null) {
            System.out.println("Email simulé - Désinscription: " + etudiant.getEmail() + 
                    " désinscrit du cours " + cours.getTitre());
            if (formateur != null && formateur.getEmail() != null) {
                System.out.println("Notification formateur: " + formateur.getEmail() + 
                        " - Étudiant désinscrit: " + etudiant.getPrenom() + " " + etudiant.getNom());
            }
            return;
        }
        
        // Email à l'étudiant
        SimpleMailMessage messageEtudiant = new SimpleMailMessage();
        messageEtudiant.setTo(etudiant.getEmail());
        messageEtudiant.setSubject("Annulation d'inscription: " + cours.getTitre());
        messageEtudiant.setText("Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                "Votre inscription au cours " + cours.getTitre() + " (" + cours.getCode() + ") a été annulée.\n\n" +
                "Si vous souhaitez vous réinscrire à ce cours, vous pouvez le faire depuis votre espace étudiant.\n\n" +
                "Cordialement,\nL'équipe de gestion de formation");
        mailSender.send(messageEtudiant);
        
        // Email au formateur
        if (formateur != null && formateur.getEmail() != null) {
            SimpleMailMessage messageFormateur = new SimpleMailMessage();
            messageFormateur.setTo(formateur.getEmail());
            messageFormateur.setSubject("Notification: Désinscription d'un étudiant - " + cours.getTitre());
            messageFormateur.setText("Bonjour " + formateur.getPrenom() + " " + formateur.getNom() + ",\n\n" +
                    "Un étudiant s'est désinscrit de votre cours.\n\n" +
                    "Étudiant: " + etudiant.getPrenom() + " " + etudiant.getNom() + "\n" +
                    "Email: " + etudiant.getEmail() + "\n" +
                    "Cours: " + cours.getTitre() + " (" + cours.getCode() + ")\n" +
                    "Date de désinscription: " + java.time.LocalDateTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")) + "\n\n" +
                    "Cordialement,\nL'équipe de gestion de formation");
            mailSender.send(messageFormateur);
        }
    }
}






