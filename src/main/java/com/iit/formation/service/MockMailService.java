package com.iit.formation.service;

import com.iit.formation.entity.MockEmail;
import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Formateur;
import com.iit.formation.repository.MockEmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service pour simuler l'envoi d'emails (Mock)
 * Stocke les emails dans la base de données au lieu de les envoyer réellement
 */
@Service
public class MockMailService {
    
    private static final Logger logger = LoggerFactory.getLogger(MockMailService.class);
    
    @Autowired
    private MockEmailRepository mockEmailRepository;
    
    @Value("${spring.mail.from:noreply@gestion-formation.com}")
    private String mailFrom;
    
    /**
     * Simule l'envoi d'un email d'inscription à un étudiant
     * L'email est envoyé à l'adresse email stockée dans les informations personnelles de l'étudiant
     */
    public void envoyerEmailInscription(Etudiant etudiant, Cours cours) {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("📧 MOCK EMAIL - INSCRIPTION ÉTUDIANT");
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
        
        logger.info("📧 Email de destination (depuis informations personnelles): {}", etudiant.getEmail());
        
        // Créer le contenu de l'email
        String subject = "Inscription au cours: " + cours.getTitre();
        String body = "Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                "Vous avez été inscrit(e) au cours: " + cours.getTitre() + " (" + cours.getCode() + ").\n\n" +
                (cours.getFormateur() != null ? 
                    "Formateur: " + cours.getFormateur().getPrenom() + " " + cours.getFormateur().getNom() + "\n" : "") +
                (cours.getDescription() != null && !cours.getDescription().isEmpty() ? 
                    "Description: " + cours.getDescription() + "\n" : "") +
                "\nNous vous souhaitons une excellente formation !\n\n" +
                "Cordialement,\nL'équipe de gestion de formation";
        
        // Sauvegarder l'email simulé
        MockEmail mockEmail = new MockEmail(mailFrom, etudiant.getEmail(), subject, body, 
                MockEmail.TypeEmail.INSCRIPTION_ETUDIANT);
        mockEmail.setEtudiantNom(etudiant.getPrenom() + " " + etudiant.getNom());
        mockEmail.setCoursTitre(cours.getTitre());
        mockEmail.setCoursCode(cours.getCode());
        mockEmailRepository.save(mockEmail);
        
        logger.info("✅ Email simulé sauvegardé pour: {}", etudiant.getEmail());
        
        // Afficher dans la console
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("📧 EMAIL SIMULÉ (MOCK) - INSCRIPTION");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("De: " + mailFrom);
        System.out.println("À: " + etudiant.getEmail());
        System.out.println("Sujet: " + subject);
        System.out.println("Message:");
        System.out.println(body);
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("💡 Consultez http://localhost:8080/admin/emails pour voir tous les emails simulés");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        // Envoyer aussi une notification au formateur si présent
        if (cours.getFormateur() != null && cours.getFormateur().getEmail() != null) {
            envoyerNotificationFormateurInscription(cours.getFormateur(), etudiant, cours);
        }
    }
    
    /**
     * Simule l'envoi d'une notification au formateur lors d'une inscription
     */
    private void envoyerNotificationFormateurInscription(Formateur formateur, Etudiant etudiant, Cours cours) {
        String subject = "Notification: Nouvel étudiant inscrit au cours " + cours.getTitre();
        String body = "Bonjour " + formateur.getPrenom() + " " + formateur.getNom() + ",\n\n" +
                "Un nouvel étudiant s'est inscrit à votre cours.\n\n" +
                "Étudiant: " + etudiant.getPrenom() + " " + etudiant.getNom() + "\n" +
                "Email: " + etudiant.getEmail() + "\n" +
                "Cours: " + cours.getTitre() + " (" + cours.getCode() + ")\n" +
                "Date d'inscription: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")) + "\n\n" +
                "Cordialement,\nL'équipe de gestion de formation";
        
        MockEmail mockEmail = new MockEmail(mailFrom, formateur.getEmail(), subject, body, 
                MockEmail.TypeEmail.INSCRIPTION_FORMATEUR);
        mockEmail.setEtudiantNom(etudiant.getPrenom() + " " + etudiant.getNom());
        mockEmail.setCoursTitre(cours.getTitre());
        mockEmail.setCoursCode(cours.getCode());
        mockEmailRepository.save(mockEmail);
        
        logger.info("✅ Notification formateur simulée sauvegardée pour: {}", formateur.getEmail());
    }
    
    /**
     * Simule l'envoi d'un email de désinscription à un étudiant
     */
    public void envoyerEmailDesinscription(Etudiant etudiant, Cours cours, Formateur formateur) {
        logger.info("📧 MOCK EMAIL - DÉSINSCRIPTION ÉTUDIANT");
        
        if (etudiant.getEmail() == null || etudiant.getEmail().isEmpty()) {
            logger.error("❌ L'email de l'étudiant est vide ou null !");
            return;
        }
        
        String subject = "Annulation d'inscription: " + cours.getTitre();
        String body = "Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",\n\n" +
                "Votre inscription au cours " + cours.getTitre() + " (" + cours.getCode() + ") a été annulée.\n\n" +
                "Si vous souhaitez vous réinscrire à ce cours, vous pouvez le faire depuis votre espace étudiant.\n\n" +
                "Cordialement,\nL'équipe de gestion de formation";
        
        MockEmail mockEmail = new MockEmail(mailFrom, etudiant.getEmail(), subject, body, 
                MockEmail.TypeEmail.DESINSCRIPTION_ETUDIANT);
        mockEmail.setEtudiantNom(etudiant.getPrenom() + " " + etudiant.getNom());
        mockEmail.setCoursTitre(cours.getTitre());
        mockEmail.setCoursCode(cours.getCode());
        mockEmailRepository.save(mockEmail);
        
        logger.info("✅ Email de désinscription simulé sauvegardé pour: {}", etudiant.getEmail());
        
        // Afficher dans la console
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("📧 EMAIL SIMULÉ (MOCK) - DÉSINSCRIPTION");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("De: " + mailFrom);
        System.out.println("À: " + etudiant.getEmail());
        System.out.println("Sujet: " + subject);
        System.out.println("Message:");
        System.out.println(body);
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        // Notification au formateur
        if (formateur != null && formateur.getEmail() != null) {
            envoyerNotificationFormateurDesinscription(formateur, etudiant, cours);
        }
    }
    
    /**
     * Simule l'envoi d'une notification au formateur lors d'une désinscription
     */
    private void envoyerNotificationFormateurDesinscription(Formateur formateur, Etudiant etudiant, Cours cours) {
        String subject = "Notification: Désinscription d'un étudiant - " + cours.getTitre();
        String body = "Bonjour " + formateur.getPrenom() + " " + formateur.getNom() + ",\n\n" +
                "Un étudiant s'est désinscrit de votre cours.\n\n" +
                "Étudiant: " + etudiant.getPrenom() + " " + etudiant.getNom() + "\n" +
                "Email: " + etudiant.getEmail() + "\n" +
                "Cours: " + cours.getTitre() + " (" + cours.getCode() + ")\n" +
                "Date de désinscription: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")) + "\n\n" +
                "Cordialement,\nL'équipe de gestion de formation";
        
        MockEmail mockEmail = new MockEmail(mailFrom, formateur.getEmail(), subject, body, 
                MockEmail.TypeEmail.DESINSCRIPTION_FORMATEUR);
        mockEmail.setEtudiantNom(etudiant.getPrenom() + " " + etudiant.getNom());
        mockEmail.setCoursTitre(cours.getTitre());
        mockEmail.setCoursCode(cours.getCode());
        mockEmailRepository.save(mockEmail);
        
        logger.info("✅ Notification formateur de désinscription simulée sauvegardée pour: {}", formateur.getEmail());
    }
    
    /**
     * Récupère tous les emails simulés
     */
    public List<MockEmail> getAllEmails() {
        return mockEmailRepository.findAllByOrderByDateEnvoiDesc();
    }
    
    /**
     * Récupère les emails pour un destinataire
     */
    public List<MockEmail> getEmailsByTo(String toEmail) {
        return mockEmailRepository.findByToEmailOrderByDateEnvoiDesc(toEmail);
    }
    
    /**
     * Récupère les emails non lus pour un destinataire
     */
    public List<MockEmail> getUnreadEmailsByTo(String toEmail) {
        return mockEmailRepository.findByToEmailAndEstLuFalseOrderByDateEnvoiDesc(toEmail);
    }
    
    /**
     * Marque un email comme lu
     */
    public void marquerCommeLu(Long emailId) {
        mockEmailRepository.findById(emailId).ifPresent(email -> {
            email.setEstLu(true);
            mockEmailRepository.save(email);
        });
    }
    
    /**
     * Compte les emails non lus pour un destinataire
     */
    public long countUnreadEmailsByTo(String toEmail) {
        return mockEmailRepository.countByToEmailAndEstLuFalse(toEmail);
    }
}

