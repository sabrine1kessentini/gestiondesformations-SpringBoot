package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Formateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    @Autowired
    private MockMailService mockMailService;
    
    @Value("${spring.mail.username:}")
    private String mailUsername;
    
    @Value("${spring.mail.from:noreply@gestion-formation.com}")
    private String mailFrom;
    
    /**
     * Vérifie si le système est en mode simulation
     */
    public boolean estModeSimulation() {
        return mailSender == null || mailUsername == null || mailUsername.isEmpty();
    }
    
    public void envoyerEmailInscription(Etudiant etudiant, Cours cours) {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("📧 ENVOI D'EMAIL D'INSCRIPTION");
        logger.info("Étudiant: {} {} ({})", etudiant.getPrenom(), etudiant.getNom(), etudiant.getEmail());
        logger.info("Cours: {} ({})", cours.getTitre(), cours.getCode());
        
        // Vérifier que l'email de l'étudiant est valide
        if (etudiant.getEmail() == null || etudiant.getEmail().isEmpty()) {
            logger.error("❌ ERREUR: L'email de l'étudiant est vide ou null !");
            logger.error("❌ Impossible d'envoyer l'email. Vérifiez les informations personnelles de l'étudiant.");
            System.err.println("❌ ERREUR: L'email de l'étudiant est vide ou null !");
            System.err.println("❌ Veuillez vérifier les informations personnelles de l'étudiant dans son profil.");
            return;
        }
        
        logger.info("📧 Email de destination: {}", etudiant.getEmail());
        
        if (mailSender == null || mailUsername == null || mailUsername.isEmpty()) {
            logger.warn("JavaMailSender non configuré - Mode simulation (MockMailService) activé");
            logger.warn("Pour activer l'envoi réel d'emails, configurez MAIL_USERNAME et MAIL_PASSWORD");
            // Utiliser MockMailService pour simuler l'envoi
            mockMailService.envoyerEmailInscription(etudiant, cours);
            return;
        }
        
        try {
            // Email à l'étudiant inscrit au cours
            SimpleMailMessage messageEtudiant = new SimpleMailMessage();
            messageEtudiant.setFrom(mailFrom);
            messageEtudiant.setTo(etudiant.getEmail()); // Email de l'étudiant depuis ses informations personnelles
            messageEtudiant.setSubject("Inscription au cours: " + cours.getTitre());
            
            String messageText = "Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                    "Vous avez été inscrit(e) au cours: " + cours.getTitre() + " (" + cours.getCode() + ").\n\n" +
                    (cours.getFormateur() != null ? 
                        "Formateur: " + cours.getFormateur().getPrenom() + " " + cours.getFormateur().getNom() + "\n" : "") +
                    (cours.getDescription() != null && !cours.getDescription().isEmpty() ? 
                        "Description: " + cours.getDescription() + "\n" : "") +
                    "\nNous vous souhaitons une excellente formation !\n\n" +
                    "Cordialement,\nL'équipe de gestion de formation";
            
            messageEtudiant.setText(messageText);
            
            logger.info("📤 Envoi de l'email à l'étudiant: {}", etudiant.getEmail());
            mailSender.send(messageEtudiant);
            logger.info("✅ Email d'inscription ENVOYÉ avec succès à l'étudiant: {}", etudiant.getEmail());
            System.out.println("✅ EMAIL ENVOYÉ à: " + etudiant.getEmail());
            
            // Email au formateur pour notification
            if (cours.getFormateur() != null && cours.getFormateur().getEmail() != null) {
                SimpleMailMessage messageFormateur = new SimpleMailMessage();
                messageFormateur.setFrom(mailFrom);
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
                logger.info("✅ Email de notification envoyé avec succès au formateur: {}", cours.getFormateur().getEmail());
            }
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'envoi de l'email d'inscription: {}", e.getMessage(), e);
            logger.error("Stack trace:", e);
            System.err.println("❌ ERREUR ENVOI EMAIL: " + e.getMessage());
            e.printStackTrace();
            // Ne pas lancer d'exception pour ne pas bloquer l'inscription
            // throw new RuntimeException("Erreur lors de l'envoi de l'email: " + e.getMessage(), e);
        }
    }
    
    public void envoyerEmailDesinscription(Etudiant etudiant, Cours cours, Formateur formateur) {
        logger.info("Tentative d'envoi d'email de désinscription pour l'étudiant: {} du cours: {}", 
                etudiant.getEmail(), cours.getTitre());
        
        if (mailSender == null || mailUsername == null || mailUsername.isEmpty()) {
            logger.warn("JavaMailSender non configuré - Mode simulation (MockMailService) activé");
            // Utiliser MockMailService pour simuler l'envoi
            mockMailService.envoyerEmailDesinscription(etudiant, cours, formateur);
            return;
        }
        
        try {
            // Email à l'étudiant
            SimpleMailMessage messageEtudiant = new SimpleMailMessage();
            messageEtudiant.setFrom(mailFrom);
            messageEtudiant.setTo(etudiant.getEmail());
            messageEtudiant.setSubject("Annulation d'inscription: " + cours.getTitre());
            messageEtudiant.setText("Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                    "Votre inscription au cours " + cours.getTitre() + " (" + cours.getCode() + ") a été annulée.\n\n" +
                    "Si vous souhaitez vous réinscrire à ce cours, vous pouvez le faire depuis votre espace étudiant.\n\n" +
                    "Cordialement,\nL'équipe de gestion de formation");
            
            mailSender.send(messageEtudiant);
            logger.info("✅ Email de désinscription envoyé avec succès à: {}", etudiant.getEmail());
            
            // Email au formateur
            if (formateur != null && formateur.getEmail() != null) {
                SimpleMailMessage messageFormateur = new SimpleMailMessage();
                messageFormateur.setFrom(mailFrom);
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
                logger.info("✅ Email de notification de désinscription envoyé avec succès au formateur: {}", formateur.getEmail());
            }
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'envoi de l'email de désinscription: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email: " + e.getMessage(), e);
        }
    }
}






