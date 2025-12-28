package com.iit.formation.controller;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Note;
import com.iit.formation.service.CoursService;
import com.iit.formation.service.NoteService;
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
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Récupérer l'ID du formateur depuis l'authentification
        // Pour simplifier, on récupère tous les cours
        model.addAttribute("cours", coursService.getAllCours());
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
        model.addAttribute("cours", coursService.getCoursById(id).orElse(null));
        model.addAttribute("notes", noteService.getNotesByCours(id));
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
}






