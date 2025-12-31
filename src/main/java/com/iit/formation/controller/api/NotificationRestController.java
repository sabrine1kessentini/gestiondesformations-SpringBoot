package com.iit.formation.controller.api;

import com.iit.formation.entity.Notification;
import com.iit.formation.service.FormateurService;
import com.iit.formation.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formateur/notifications")
public class NotificationRestController {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private FormateurService formateurService;
    
    @GetMapping
    public ResponseEntity<?> getNotifications() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                com.iit.formation.entity.Formateur formateur = formateurService.getFormateurByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
                
                List<Notification> notifications = notificationService.getNotificationsByFormateur(formateur.getId());
                long nombreNonLues = notificationService.countNotificationsNonLues(formateur.getId());
                
                Map<String, Object> response = new HashMap<>();
                response.put("notifications", notifications);
                response.put("nombreNonLues", nombreNonLues);
                
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(401).body(Map.of("error", "Non authentifié"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/non-lues")
    public ResponseEntity<?> getNotificationsNonLues() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                com.iit.formation.entity.Formateur formateur = formateurService.getFormateurByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
                
                List<Notification> notifications = notificationService.getNotificationsNonLues(formateur.getId());
                return ResponseEntity.ok(notifications);
            }
            return ResponseEntity.status(401).body(Map.of("error", "Non authentifié"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/marquer-lue")
    public ResponseEntity<?> marquerCommeLue(@PathVariable Long id) {
        try {
            notificationService.marquerCommeLue(id);
            return ResponseEntity.ok(Map.of("message", "Notification marquée comme lue"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/marquer-toutes-lues")
    public ResponseEntity<?> marquerToutesCommeLues() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                com.iit.formation.entity.Formateur formateur = formateurService.getFormateurByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
                
                notificationService.marquerToutesCommeLues(formateur.getId());
                return ResponseEntity.ok(Map.of("message", "Toutes les notifications ont été marquées comme lues"));
            }
            return ResponseEntity.status(401).body(Map.of("error", "Non authentifié"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

