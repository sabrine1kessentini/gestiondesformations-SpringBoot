package com.iit.formation.service;

import com.iit.formation.dto.ChangePasswordDTO;
import com.iit.formation.dto.UpdateProfileDTO;
import com.iit.formation.entity.Utilisateur;
import com.iit.formation.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UtilisateurService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Récupère l'utilisateur actuellement connecté
     */
    public Optional<Utilisateur> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return utilisateurRepository.findByUsername(username);
        }
        return Optional.empty();
    }
    
    /**
     * Récupère un utilisateur par son ID
     */
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }
    
    /**
     * Met à jour le profil de l'utilisateur connecté
     */
    public Utilisateur updateProfile(UpdateProfileDTO updateProfileDTO) {
        Utilisateur utilisateur = getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (!utilisateur.getEmail().equals(updateProfileDTO.getEmail())) {
            if (utilisateurRepository.existsByEmail(updateProfileDTO.getEmail())) {
                throw new RuntimeException("Cet email est déjà utilisé par un autre utilisateur");
            }
        }
        
        utilisateur.setNom(updateProfileDTO.getNom());
        utilisateur.setPrenom(updateProfileDTO.getPrenom());
        utilisateur.setEmail(updateProfileDTO.getEmail());
        
        return utilisateurRepository.save(utilisateur);
    }
    
    /**
     * Met à jour le profil d'un utilisateur par son ID (pour admin)
     */
    public Utilisateur updateProfileById(Long id, UpdateProfileDTO updateProfileDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        if (!utilisateur.getEmail().equals(updateProfileDTO.getEmail())) {
            if (utilisateurRepository.existsByEmail(updateProfileDTO.getEmail())) {
                throw new RuntimeException("Cet email est déjà utilisé par un autre utilisateur");
            }
        }
        
        utilisateur.setNom(updateProfileDTO.getNom());
        utilisateur.setPrenom(updateProfileDTO.getPrenom());
        utilisateur.setEmail(updateProfileDTO.getEmail());
        
        return utilisateurRepository.save(utilisateur);
    }
    
    /**
     * Change le mot de passe de l'utilisateur connecté
     */
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        Utilisateur utilisateur = getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier que le mot de passe actuel est correct
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), utilisateur.getPassword())) {
            throw new RuntimeException("Le mot de passe actuel est incorrect");
        }
        
        // Vérifier que les nouveaux mots de passe correspondent
        if (!changePasswordDTO.isPasswordMatch()) {
            throw new RuntimeException("Les nouveaux mots de passe ne correspondent pas");
        }
        
        // Vérifier que le nouveau mot de passe est différent de l'ancien
        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), utilisateur.getPassword())) {
            throw new RuntimeException("Le nouveau mot de passe doit être différent de l'ancien");
        }
        
        // Encoder et sauvegarder le nouveau mot de passe
        utilisateur.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        utilisateurRepository.save(utilisateur);
    }
    
    /**
     * Change le mot de passe d'un utilisateur par son ID (pour admin)
     */
    public void changePasswordById(Long id, String newPassword) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 6 caractères");
        }
        
        utilisateur.setPassword(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(utilisateur);
    }
}

