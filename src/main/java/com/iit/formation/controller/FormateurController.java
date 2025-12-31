package com.iit.formation.controller;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Formateur;
import com.iit.formation.entity.Note;
import com.iit.formation.service.CoursService;
import com.iit.formation.service.FormateurService;
import com.iit.formation.service.NoteService;
import com.iit.formation.service.InscriptionService;
import com.iit.formation.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/formateur")
public class FormateurController {
    
    @Autowired
    private CoursService coursService;
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private InscriptionService inscriptionService;
    
    @Autowired
    private FormateurService formateurService;
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                Formateur formateur = formateurService.getFormateurByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
                
                // Récupérer les cours du formateur
                List<Cours> cours = coursService.getCoursByFormateur(formateur.getId());
                model.addAttribute("cours", cours);
                
                // Récupérer les notifications
                List<com.iit.formation.entity.Notification> notifications = 
                        notificationService.getNotificationsNonLues(formateur.getId());
                model.addAttribute("notifications", notifications);
                model.addAttribute("nombreNotifications", notificationService.countNotificationsNonLues(formateur.getId()));
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du tableau de bord: " + e.getMessage());
        }
        return "formateur/dashboard";
    }
    
    @GetMapping("/cours")
    public String mesCours(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // TODO: Filtrer par formateur connecté
        model.addAttribute("cours", coursService.getAllCours());
        return "formateur/cours/list";
    }
    
    @GetMapping("/cours/{id}/notes")
    public String gererNotes(@PathVariable Long id, Model model) {
        try {
            Cours cours = coursService.getCoursById(id).orElse(null);
            if (cours != null) {
                model.addAttribute("cours", cours);
                List<Note> notes = noteService.getNotesByCours(id);
                model.addAttribute("notes", notes);
                // Récupérer les étudiants inscrits au cours (inscriptions actives uniquement)
                List<com.iit.formation.entity.Inscription> inscriptions = inscriptionService.getInscriptionsByCours(id);
                List<com.iit.formation.entity.Etudiant> etudiantsInscrits = inscriptions.stream()
                        .filter(ins -> ins.getStatut() == com.iit.formation.entity.Inscription.StatutInscription.ACTIVE)
                        .map(ins -> ins.getEtudiant())
                        .toList();
                model.addAttribute("etudiantsInscrits", etudiantsInscrits);
                // Créer une map pour faciliter l'accès aux notes par étudiant
                java.util.Map<Long, Note> notesMap = new java.util.HashMap<>();
                for (Note note : notes) {
                    notesMap.put(note.getEtudiant().getId(), note);
                }
                model.addAttribute("notesMap", notesMap);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement: " + e.getMessage());
        }
        return "formateur/cours/notes";
    }
    
    @PostMapping("/cours/{coursId}/notes")
    public String attribuerNote(@PathVariable Long coursId,
                                @RequestParam Long etudiantId,
                                @RequestParam Double valeur,
                                @RequestParam(required = false) String commentaire) {
        noteService.attribuerNote(etudiantId, coursId, valeur, commentaire);
        return "redirect:/formateur/cours/" + coursId + "/notes";
    }
    
    @GetMapping("/notifications")
    public String notifications(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                Formateur formateur = formateurService.getFormateurByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
                
                List<com.iit.formation.entity.Notification> toutesNotifications = 
                        notificationService.getNotificationsByFormateur(formateur.getId());
                model.addAttribute("notifications", toutesNotifications);
                model.addAttribute("nombreNotifications", notificationService.countNotificationsNonLues(formateur.getId()));
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des notifications: " + e.getMessage());
        }
        return "formateur/notifications";
    }
    
    @PostMapping("/notifications/{id}/marquer-lue")
    public String marquerCommeLue(@PathVariable Long id) {
        try {
            notificationService.marquerCommeLue(id);
        } catch (Exception e) {
            // Erreur silencieuse
        }
        return "redirect:/formateur/notifications";
    }
    
    @PostMapping("/notifications/marquer-toutes-lues")
    public String marquerToutesCommeLues() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                Formateur formateur = formateurService.getFormateurByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
                notificationService.marquerToutesCommeLues(formateur.getId());
            }
        } catch (Exception e) {
            // Erreur silencieuse
        }
        return "redirect:/formateur/notifications";
    }
}






