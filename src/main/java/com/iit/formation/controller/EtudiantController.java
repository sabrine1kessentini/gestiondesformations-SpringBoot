package com.iit.formation.controller;

import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Inscription;
import com.iit.formation.entity.Note;
import com.iit.formation.entity.Seance;
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
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // TODO: Récupérer l'étudiant connecté
        // Pour l'instant, on prend le premier étudiant
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        if (!etudiants.isEmpty()) {
            Etudiant etudiant = etudiants.get(0);
            model.addAttribute("etudiant", etudiant);
            model.addAttribute("moyenne", etudiantService.getMoyenneGenerale(etudiant.getId()));
        }
        return "etudiant/dashboard";
    }
    
    @GetMapping("/cours")
    public String mesCours(Model model) {
        // TODO: Filtrer par étudiant connecté
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        if (!etudiants.isEmpty()) {
            Long etudiantId = etudiants.get(0).getId();
            model.addAttribute("inscriptions", inscriptionService.getInscriptionsByEtudiant(etudiantId));
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
        // TODO: Filtrer par étudiant connecté
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        if (!etudiants.isEmpty()) {
            Long etudiantId = etudiants.get(0).getId();
            model.addAttribute("seances", seanceService.getEmploiDuTempsEtudiant(etudiantId));
        }
        return "etudiant/emploi-du-temps";
    }
    
    @PostMapping("/inscrire/{coursId}")
    public String inscrireAuCours(@PathVariable Long coursId) {
        // TODO: Récupérer l'étudiant connecté
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        if (!etudiants.isEmpty()) {
            Long etudiantId = etudiants.get(0).getId();
            inscriptionService.inscrireEtudiant(etudiantId, coursId);
        }
        return "redirect:/etudiant/cours";
    }
}






