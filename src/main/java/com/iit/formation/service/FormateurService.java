package com.iit.formation.service;

import com.iit.formation.entity.Formateur;
import com.iit.formation.repository.FormateurRepository;
import com.iit.formation.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormateurService {
    
    @Autowired
    private FormateurRepository formateurRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }
    
    public Optional<Formateur> getFormateurById(Long id) {
        return formateurRepository.findById(id);
    }
    
    public Formateur createFormateur(Formateur formateur) {
        if (utilisateurRepository.existsByEmail(formateur.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        if (utilisateurRepository.existsByUsername(formateur.getUsername())) {
            throw new RuntimeException("Un utilisateur avec ce nom d'utilisateur existe déjà");
        }
        formateur.setPassword(passwordEncoder.encode(formateur.getPassword()));
        return formateurRepository.save(formateur);
    }
    
    public Formateur updateFormateur(Long id, Formateur formateurDetails) {
        Formateur formateur = formateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
        
        formateur.setNom(formateurDetails.getNom());
        formateur.setPrenom(formateurDetails.getPrenom());
        formateur.setEmail(formateurDetails.getEmail());
        formateur.setSpecialite(formateurDetails.getSpecialite());
        
        if (formateurDetails.getPassword() != null && !formateurDetails.getPassword().isEmpty()) {
            formateur.setPassword(passwordEncoder.encode(formateurDetails.getPassword()));
        }
        
        return formateurRepository.save(formateur);
    }
    
    public void deleteFormateur(Long id) {
        Formateur formateur = formateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
        formateurRepository.delete(formateur);
    }
}

