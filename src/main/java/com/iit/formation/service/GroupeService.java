package com.iit.formation.service;

import com.iit.formation.entity.Groupe;
import com.iit.formation.repository.GroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupeService {
    
    @Autowired
    private GroupeRepository groupeRepository;
    
    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }
    
    public Optional<Groupe> getGroupeById(Long id) {
        return groupeRepository.findById(id);
    }
    
    public Groupe createGroupe(Groupe groupe) {
        if (groupeRepository.existsByNom(groupe.getNom())) {
            throw new RuntimeException("Un groupe avec ce nom existe déjà");
        }
        return groupeRepository.save(groupe);
    }
    
    public Groupe updateGroupe(Long id, Groupe groupeDetails) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));
        
        if (!groupe.getNom().equals(groupeDetails.getNom()) &&
            groupeRepository.existsByNom(groupeDetails.getNom())) {
            throw new RuntimeException("Un groupe avec ce nom existe déjà");
        }
        
        groupe.setNom(groupeDetails.getNom());
        groupe.setDescription(groupeDetails.getDescription());
        
        return groupeRepository.save(groupe);
    }
    
    public void deleteGroupe(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));
        groupeRepository.delete(groupe);
    }
}






