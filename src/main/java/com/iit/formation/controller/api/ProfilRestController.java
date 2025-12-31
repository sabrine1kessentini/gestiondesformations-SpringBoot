package com.iit.formation.controller.api;

import com.iit.formation.dto.ChangePasswordDTO;
import com.iit.formation.dto.UpdateProfileDTO;
import com.iit.formation.entity.Utilisateur;
import com.iit.formation.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profil")
public class ProfilRestController {
    
    @Autowired
    private UtilisateurService utilisateurService;
    
    @GetMapping
    public ResponseEntity<?> getProfil() {
        try {
            Utilisateur utilisateur = utilisateurService.getCurrentUser()
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            Map<String, Object> profil = new HashMap<>();
            profil.put("id", utilisateur.getId());
            profil.put("username", utilisateur.getUsername());
            profil.put("nom", utilisateur.getNom());
            profil.put("prenom", utilisateur.getPrenom());
            profil.put("email", utilisateur.getEmail());
            profil.put("roles", utilisateur.getRoles());
            
            return ResponseEntity.ok(profil);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping
    public ResponseEntity<?> updateProfil(@Valid @RequestBody UpdateProfileDTO updateProfileDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }
        
        try {
            Utilisateur utilisateur = utilisateurService.updateProfile(updateProfileDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profil mis à jour avec succès");
            response.put("utilisateur", Map.of(
                    "id", utilisateur.getId(),
                    "username", utilisateur.getUsername(),
                    "nom", utilisateur.getNom(),
                    "prenom", utilisateur.getPrenom(),
                    "email", utilisateur.getEmail()
            ));
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }
        
        if (!changePasswordDTO.isPasswordMatch()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Les nouveaux mots de passe ne correspondent pas"));
        }
        
        try {
            utilisateurService.changePassword(changePasswordDTO);
            return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

