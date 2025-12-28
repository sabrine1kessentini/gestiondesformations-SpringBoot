package com.iit.formation.service;

import com.iit.formation.entity.Specialite;
import com.iit.formation.repository.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SpecialiteService {
    
    @Autowired
    private SpecialiteRepository specialiteRepository;
    
    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }
    
    public Optional<Specialite> getSpecialiteById(Long id) {
        return specialiteRepository.findById(id);
    }
    
    public Specialite createSpecialite(Specialite specialite) {
        if (specialiteRepository.existsByNom(specialite.getNom())) {
            throw new RuntimeException("Une spécialité avec ce nom existe déjà");
        }
        return specialiteRepository.save(specialite);
    }
    
    public Specialite updateSpecialite(Long id, Specialite specialiteDetails) {
        Specialite specialite = specialiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spécialité non trouvée"));
        
        if (!specialite.getNom().equals(specialiteDetails.getNom()) &&
            specialiteRepository.existsByNom(specialiteDetails.getNom())) {
            throw new RuntimeException("Une spécialité avec ce nom existe déjà");
        }
        
        specialite.setNom(specialiteDetails.getNom());
        specialite.setDescription(specialiteDetails.getDescription());
        
        return specialiteRepository.save(specialite);
    }
    
    public void deleteSpecialite(Long id) {
        Specialite specialite = specialiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spécialité non trouvée"));
        specialiteRepository.delete(specialite);
    }
}






