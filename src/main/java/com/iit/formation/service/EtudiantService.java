package com.iit.formation.service;

import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Groupe;
import com.iit.formation.repository.EtudiantRepository;
import com.iit.formation.repository.GroupeRepository;
import com.iit.formation.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EtudiantService {
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private GroupeRepository groupeRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }
    
    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }
    
    public Optional<Etudiant> getEtudiantByMatricule(String matricule) {
        return etudiantRepository.findByMatricule(matricule);
    }
    
    public Etudiant createEtudiant(Etudiant etudiant) {
        if (etudiantRepository.existsByMatricule(etudiant.getMatricule())) {
            throw new RuntimeException("Un étudiant avec ce matricule existe déjà");
        }
        if (utilisateurRepository.existsByEmail(etudiant.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        if (utilisateurRepository.existsByUsername(etudiant.getUsername())) {
            throw new RuntimeException("Un utilisateur avec ce nom d'utilisateur existe déjà");
        }
        etudiant.setPassword(passwordEncoder.encode(etudiant.getPassword()));
        return etudiantRepository.save(etudiant);
    }
    
    public Etudiant updateEtudiant(Long id, Etudiant etudiantDetails) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        if (!etudiant.getMatricule().equals(etudiantDetails.getMatricule()) &&
            etudiantRepository.existsByMatricule(etudiantDetails.getMatricule())) {
            throw new RuntimeException("Un étudiant avec ce matricule existe déjà");
        }
        
        etudiant.setMatricule(etudiantDetails.getMatricule());
        etudiant.setNom(etudiantDetails.getNom());
        etudiant.setPrenom(etudiantDetails.getPrenom());
        etudiant.setEmail(etudiantDetails.getEmail());
        
        if (etudiantDetails.getPassword() != null && !etudiantDetails.getPassword().isEmpty()) {
            etudiant.setPassword(passwordEncoder.encode(etudiantDetails.getPassword()));
        }
        
        if (etudiantDetails.getGroupe() != null) {
            Groupe groupe = groupeRepository.findById(etudiantDetails.getGroupe().getId())
                    .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));
            etudiant.setGroupe(groupe);
        }
        
        return etudiantRepository.save(etudiant);
    }
    
    public void deleteEtudiant(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        etudiantRepository.delete(etudiant);
    }
    
    public double getMoyenneGenerale(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return etudiant.getMoyenneGenerale();
    }
}

