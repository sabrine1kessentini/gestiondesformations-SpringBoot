package com.iit.formation.controller;

import com.iit.formation.dto.ChangePasswordDTO;
import com.iit.formation.dto.UpdateProfileDTO;
import com.iit.formation.entity.Utilisateur;
import com.iit.formation.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profil")
public class ProfilController {
    
    @Autowired
    private UtilisateurService utilisateurService;
    
    @GetMapping
    public String profil(Model model) {
        Utilisateur utilisateur = utilisateurService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Préparer le DTO avec les données actuelles
        UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO(
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getUsername()
        );
        
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("updateProfileDTO", updateProfileDTO);
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        
        // Déterminer la vue selon le rôle
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        
        if (auth != null && auth.getAuthorities() != null) {
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "admin/profil";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_FORMATEUR"))) {
                return "formateur/profil";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ETUDIANT"))) {
                return "etudiant/profil";
            }
        }
        
        return "profil";
    }
    
    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute UpdateProfileDTO updateProfileDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erreur de validation");
            return "redirect:/profil";
        }
        
        try {
            utilisateurService.updateProfile(updateProfileDTO);
            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/profil";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute ChangePasswordDTO changePasswordDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erreur de validation");
            return "redirect:/profil";
        }
        
        if (!changePasswordDTO.isPasswordMatch()) {
            redirectAttributes.addFlashAttribute("error", "Les nouveaux mots de passe ne correspondent pas");
            return "redirect:/profil";
        }
        
        try {
            utilisateurService.changePassword(changePasswordDTO);
            redirectAttributes.addFlashAttribute("success", "Mot de passe modifié avec succès");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/profil";
    }
}

