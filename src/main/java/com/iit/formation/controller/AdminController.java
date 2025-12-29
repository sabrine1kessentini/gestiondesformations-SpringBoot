package com.iit.formation.controller;

import com.iit.formation.entity.*;
import com.iit.formation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private EtudiantService etudiantService;
    
    @Autowired
    private FormateurService formateurService;
    
    @Autowired
    private CoursService coursService;
    
    @Autowired
    private GroupeService groupeService;
    
    @Autowired
    private SpecialiteService specialiteService;
    
    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private ReportingService reportingService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("dashboard", reportingService.getTableauDeBord());
        return "admin/dashboard";
    }
    
    // Gestion des étudiants
    @GetMapping("/etudiants")
    public String listEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        return "admin/etudiants/list";
    }
    
    @GetMapping("/etudiants/new")
    public String newEtudiantForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("groupes", groupeService.getAllGroupes());
        return "admin/etudiants/form";
    }
    
    @PostMapping("/etudiants")
    public String createEtudiant(@ModelAttribute Etudiant etudiant) {
        etudiantService.createEtudiant(etudiant);
        return "redirect:/admin/etudiants";
    }
    
    @GetMapping("/etudiants/{id}/edit")
    public String editEtudiantForm(@PathVariable Long id, Model model) {
        etudiantService.getEtudiantById(id).ifPresent(etudiant -> {
            model.addAttribute("etudiant", etudiant);
            model.addAttribute("groupes", groupeService.getAllGroupes());
        });
        return "admin/etudiants/form";
    }
    
    @PostMapping("/etudiants/{id}")
    public String updateEtudiant(@PathVariable Long id, @ModelAttribute Etudiant etudiant) {
        etudiantService.updateEtudiant(id, etudiant);
        return "redirect:/admin/etudiants";
    }
    
    @PostMapping("/etudiants/{id}/delete")
    public String deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return "redirect:/admin/etudiants";
    }
    
    // Gestion des formateurs
    @GetMapping("/formateurs")
    public String listFormateurs(Model model) {
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        return "admin/formateurs/list";
    }
    
    @GetMapping("/formateurs/new")
    public String newFormateurForm(Model model) {
        model.addAttribute("formateur", new Formateur());
        return "admin/formateurs/form";
    }
    
    @PostMapping("/formateurs")
    public String createFormateur(@ModelAttribute Formateur formateur) {
        formateurService.createFormateur(formateur);
        return "redirect:/admin/formateurs";
    }
    
    @GetMapping("/formateurs/{id}/edit")
    public String editFormateurForm(@PathVariable Long id, Model model) {
        formateurService.getFormateurById(id).ifPresent(formateur -> {
            model.addAttribute("formateur", formateur);
        });
        return "admin/formateurs/form";
    }
    
    @PostMapping("/formateurs/{id}")
    public String updateFormateur(@PathVariable Long id, @ModelAttribute Formateur formateur) {
        formateurService.updateFormateur(id, formateur);
        return "redirect:/admin/formateurs";
    }
    
    @PostMapping("/formateurs/{id}/delete")
    public String deleteFormateur(@PathVariable Long id) {
        formateurService.deleteFormateur(id);
        return "redirect:/admin/formateurs";
    }
    
    // Gestion des cours
    @GetMapping("/cours")
    public String listCours(Model model) {
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/cours/list";
    }
    
    @GetMapping("/cours/new")
    public String newCoursForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        model.addAttribute("specialites", specialiteService.getAllSpecialites());
        model.addAttribute("sessions", sessionService.getAllSessions());
        model.addAttribute("groupes", groupeService.getAllGroupes());
        return "admin/cours/form";
    }
    
    @PostMapping("/cours")
    public String createCours(@ModelAttribute Cours cours, @RequestParam(required = false) List<Long> groupeIds) {
        Cours coursCree = coursService.createCours(cours);
        if (groupeIds != null && !groupeIds.isEmpty()) {
            for (Long groupeId : groupeIds) {
                coursService.ajouterGroupe(coursCree.getId(), groupeId);
            }
        }
        return "redirect:/admin/cours";
    }
    
    @GetMapping("/cours/{id}/edit")
    public String editCoursForm(@PathVariable Long id, Model model) {
        coursService.getCoursById(id).ifPresent(cours -> {
            model.addAttribute("cours", cours);
            model.addAttribute("formateurs", formateurService.getAllFormateurs());
            model.addAttribute("specialites", specialiteService.getAllSpecialites());
            model.addAttribute("sessions", sessionService.getAllSessions());
            model.addAttribute("groupes", groupeService.getAllGroupes());
        });
        return "admin/cours/form";
    }
    
    @PostMapping("/cours/{id}")
    public String updateCours(@PathVariable Long id, @ModelAttribute Cours cours, @RequestParam(required = false) List<Long> groupeIds) {
        Cours coursExistant = coursService.getCoursById(id).orElse(null);
        if (coursExistant != null) {
            // Sauvegarder les groupes actuels
            List<Long> groupesActuels = coursExistant.getGroupes().stream()
                    .map(g -> g.getId())
                    .toList();
            
            // Mettre à jour le cours
            coursService.updateCours(id, cours);
            
            // Retirer les groupes non sélectionnés
            if (groupesActuels != null) {
                for (Long groupeId : groupesActuels) {
                    if (groupeIds == null || !groupeIds.contains(groupeId)) {
                        coursService.retirerGroupe(id, groupeId);
                    }
                }
            }
            
            // Ajouter les nouveaux groupes
            if (groupeIds != null) {
                for (Long groupeId : groupeIds) {
                    if (groupesActuels == null || !groupesActuels.contains(groupeId)) {
                        coursService.ajouterGroupe(id, groupeId);
                    }
                }
            }
        }
        return "redirect:/admin/cours";
    }
    
    @PostMapping("/cours/{id}/delete")
    public String deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return "redirect:/admin/cours";
    }
    
    // Gestion des groupes
    @GetMapping("/groupes")
    public String listGroupes(Model model) {
        model.addAttribute("groupes", groupeService.getAllGroupes());
        return "admin/groupes/list";
    }
    
    @GetMapping("/groupes/new")
    public String newGroupeForm(Model model) {
        model.addAttribute("groupe", new Groupe());
        return "admin/groupes/form";
    }
    
    @PostMapping("/groupes")
    public String createGroupe(@ModelAttribute Groupe groupe) {
        groupeService.createGroupe(groupe);
        return "redirect:/admin/groupes";
    }
    
    @GetMapping("/groupes/{id}/edit")
    public String editGroupeForm(@PathVariable Long id, Model model) {
        groupeService.getGroupeById(id).ifPresent(groupe -> {
            model.addAttribute("groupe", groupe);
        });
        return "admin/groupes/form";
    }
    
    @PostMapping("/groupes/{id}")
    public String updateGroupe(@PathVariable Long id, @ModelAttribute Groupe groupe) {
        groupeService.updateGroupe(id, groupe);
        return "redirect:/admin/groupes";
    }
    
    @PostMapping("/groupes/{id}/delete")
    public String deleteGroupe(@PathVariable Long id) {
        groupeService.deleteGroupe(id);
        return "redirect:/admin/groupes";
    }
    
    // Gestion des spécialités
    @GetMapping("/specialites")
    public String listSpecialites(Model model) {
        model.addAttribute("specialites", specialiteService.getAllSpecialites());
        return "admin/specialites/list";
    }
    
    @GetMapping("/specialites/new")
    public String newSpecialiteForm(Model model) {
        model.addAttribute("specialite", new Specialite());
        return "admin/specialites/form";
    }
    
    @PostMapping("/specialites")
    public String createSpecialite(@ModelAttribute Specialite specialite) {
        specialiteService.createSpecialite(specialite);
        return "redirect:/admin/specialites";
    }
    
    @GetMapping("/specialites/{id}/edit")
    public String editSpecialiteForm(@PathVariable Long id, Model model) {
        specialiteService.getSpecialiteById(id).ifPresent(specialite -> {
            model.addAttribute("specialite", specialite);
        });
        return "admin/specialites/form";
    }
    
    @PostMapping("/specialites/{id}")
    public String updateSpecialite(@PathVariable Long id, @ModelAttribute Specialite specialite) {
        specialiteService.updateSpecialite(id, specialite);
        return "redirect:/admin/specialites";
    }
    
    @PostMapping("/specialites/{id}/delete")
    public String deleteSpecialite(@PathVariable Long id) {
        specialiteService.deleteSpecialite(id);
        return "redirect:/admin/specialites";
    }
    
    // Gestion des sessions
    @GetMapping("/sessions")
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionService.getAllSessions());
        return "admin/sessions/list";
    }
    
    @GetMapping("/sessions/new")
    public String newSessionForm(Model model) {
        model.addAttribute("session", new Session());
        return "admin/sessions/form";
    }
    
    @PostMapping("/sessions")
    public String createSession(@ModelAttribute Session session) {
        sessionService.createSession(session);
        return "redirect:/admin/sessions";
    }
    
    @GetMapping("/sessions/{id}/edit")
    public String editSessionForm(@PathVariable Long id, Model model) {
        sessionService.getSessionById(id).ifPresent(session -> {
            model.addAttribute("session", session);
        });
        return "admin/sessions/form";
    }
    
    @PostMapping("/sessions/{id}")
    public String updateSession(@PathVariable Long id, @ModelAttribute Session session) {
        sessionService.updateSession(id, session);
        return "redirect:/admin/sessions";
    }
    
    @PostMapping("/sessions/{id}/delete")
    public String deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return "redirect:/admin/sessions";
    }
}






