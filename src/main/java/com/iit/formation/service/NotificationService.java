package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Formateur;
import com.iit.formation.entity.Notification;
import com.iit.formation.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    /**
     * Crée une notification d'inscription pour le formateur
     */
    public void creerNotificationInscription(Formateur formateur, Cours cours, Etudiant etudiant) {
        String message = String.format("L'étudiant %s %s s'est inscrit à votre cours %s (%s)",
                etudiant.getPrenom(), etudiant.getNom(), cours.getTitre(), cours.getCode());
        
        Notification notification = new Notification(formateur, cours, etudiant, 
                Notification.TypeNotification.INSCRIPTION, message);
        notificationRepository.save(notification);
    }
    
    /**
     * Crée une notification de désinscription pour le formateur
     */
    public void creerNotificationDesinscription(Formateur formateur, Cours cours, Etudiant etudiant) {
        String message = String.format("L'étudiant %s %s s'est désinscrit de votre cours %s (%s)",
                etudiant.getPrenom(), etudiant.getNom(), cours.getTitre(), cours.getCode());
        
        Notification notification = new Notification(formateur, cours, etudiant, 
                Notification.TypeNotification.DESINSCRIPTION, message);
        notificationRepository.save(notification);
    }
    
    /**
     * Récupère toutes les notifications d'un formateur
     */
    public List<Notification> getNotificationsByFormateur(Long formateurId) {
        return notificationRepository.findByFormateurIdOrderByDateCreationDesc(formateurId);
    }
    
    /**
     * Récupère les notifications non lues d'un formateur
     */
    public List<Notification> getNotificationsNonLues(Long formateurId) {
        return notificationRepository.findByFormateurIdAndLueFalseOrderByDateCreationDesc(formateurId);
    }
    
    /**
     * Compte les notifications non lues d'un formateur
     */
    public long countNotificationsNonLues(Long formateurId) {
        return notificationRepository.countByFormateurIdAndLueFalse(formateurId);
    }
    
    /**
     * Marque une notification comme lue
     */
    public void marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setLue(true);
        notificationRepository.save(notification);
    }
    
    /**
     * Marque toutes les notifications d'un formateur comme lues
     */
    public void marquerToutesCommeLues(Long formateurId) {
        List<Notification> notifications = notificationRepository.findByFormateurIdAndLueFalseOrderByDateCreationDesc(formateurId);
        notifications.forEach(notification -> notification.setLue(true));
        notificationRepository.saveAll(notifications);
    }
}

