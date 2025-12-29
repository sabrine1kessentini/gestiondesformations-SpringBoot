package com.iit.formation.controller;

import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Inscription;
import com.iit.formation.entity.Note;
import com.iit.formation.entity.Seance;
import com.iit.formation.service.CoursService;
import com.iit.formation.service.EtudiantService;
import com.iit.formation.service.InscriptionService;
import com.iit.formation.service.NoteService;
import com.iit.formation.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/etudiant")
public class EtudiantController {
    
    @Autowired
    private EtudiantService etudiantService;
    
    @Autowired
    private InscriptionService inscriptionService;
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private SeanceService seanceService;
    
    @Autowired
    private CoursService coursService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // TODO: Récupérer l'étudiant connecté
            // Pour l'instant, on prend le premier étudiant
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            if (!etudiants.isEmpty()) {
                Etudiant etudiant = etudiants.get(0);
                model.addAttribute("etudiant", etudiant);
                model.addAttribute("moyenne", etudiantService.getMoyenneGenerale(etudiant.getId()));
            } else {
                model.addAttribute("error", "Aucun étudiant trouvé");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du tableau de bord: " + e.getMessage());
        }
        return "etudiant/dashboard";
    }
    
    @GetMapping("/cours")
    public String mesCours(Model model) {
        try {
            // TODO: Filtrer par étudiant connecté
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            if (!etudiants.isEmpty()) {
                Long etudiantId = etudiants.get(0).getId();
                List<Inscription> toutesInscriptions = inscriptionService.getInscriptionsByEtudiant(etudiantId);
                // Filtrer pour ne montrer que les inscriptions actives
                List<Inscription> inscriptionsActives = toutesInscriptions.stream()
                        .filter(ins -> ins.getStatut() == Inscription.StatutInscription.ACTIVE)
                        .toList();
                model.addAttribute("inscriptions", inscriptionsActives);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des cours: " + e.getMessage());
        }
        return "etudiant/cours/list";
    }
    
    @GetMapping("/notes")
    public String mesNotes(Model model) {
        // TODO: Filtrer par étudiant connecté
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        if (!etudiants.isEmpty()) {
            Long etudiantId = etudiants.get(0).getId();
            model.addAttribute("notes", noteService.getNotesByEtudiant(etudiantId));
            model.addAttribute("moyenne", etudiantService.getMoyenneGenerale(etudiantId));
        }
        return "etudiant/notes/list";
    }
    
    @GetMapping("/emploi-du-temps")
    public String emploiDuTemps(Model model) {
        try {
            // TODO: Filtrer par étudiant connecté
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            if (!etudiants.isEmpty()) {
                Long etudiantId = etudiants.get(0).getId();
                List<Seance> seances = seanceService.getEmploiDuTempsEtudiant(etudiantId);
                model.addAttribute("seances", seances != null ? seances : new java.util.ArrayList<>());
            } else {
                model.addAttribute("seances", new java.util.ArrayList<>());
                model.addAttribute("error", "Aucun étudiant trouvé");
            }
        } catch (Exception e) {
            model.addAttribute("seances", new java.util.ArrayList<>());
            model.addAttribute("error", "Erreur lors du chargement de l'emploi du temps: " + e.getMessage());
        }
        return "etudiant/emploi-du-temps";
    }
    
    @GetMapping("/cours/disponibles")
    public String coursDisponibles(Model model) {
        try {
            // TODO: Récupérer l'étudiant connecté
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            if (!etudiants.isEmpty()) {
                Long etudiantId = etudiants.get(0).getId();
                List<com.iit.formation.entity.Cours> tousLesCours = coursService.getAllCours();
                List<Inscription> inscriptions = inscriptionService.getInscriptionsByEtudiant(etudiantId);
                // Filtrer pour ne prendre que les inscriptions actives
                List<Long> coursInscrits = inscriptions.stream()
                        .filter(ins -> ins.getStatut() == Inscription.StatutInscription.ACTIVE)
                        .map(ins -> ins.getCours().getId())
                        .toList();
                
                model.addAttribute("cours", tousLesCours);
                model.addAttribute("coursInscrits", coursInscrits);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des cours: " + e.getMessage());
        }
        return "etudiant/cours/disponibles";
    }
    
    @PostMapping("/inscrire/{coursId}")
    public String inscrireAuCours(@PathVariable Long coursId) {
        try {
            // TODO: Récupérer l'étudiant connecté
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            if (!etudiants.isEmpty()) {
                Long etudiantId = etudiants.get(0).getId();
                inscriptionService.inscrireEtudiant(etudiantId, coursId);
            }
        } catch (RuntimeException e) {
            // L'erreur sera gérée par la redirection
        }
        return "redirect:/etudiant/cours/disponibles";
    }
    
    @PostMapping("/desinscrire/{coursId}")
    public String desinscrireDuCours(@PathVariable Long coursId) {
        try {
            // TODO: Récupérer l'étudiant connecté
            List<Etudiant> etudiants = etudiantService.getAllEtudiants();
            if (!etudiants.isEmpty()) {
                Long etudiantId = etudiants.get(0).getId();
                inscriptionService.annulerInscription(etudiantId, coursId);
            }
        } catch (RuntimeException e) {
            // L'erreur sera gérée par la redirection
        }
        return "redirect:/etudiant/cours";
    }
}






