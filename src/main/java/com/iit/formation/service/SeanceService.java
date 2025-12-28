package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Seance;
import com.iit.formation.repository.CoursRepository;
import com.iit.formation.repository.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SeanceService {
    
    @Autowired
    private SeanceRepository seanceRepository;
    
    @Autowired
    private CoursRepository coursRepository;
    
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }
    
    public Optional<Seance> getSeanceById(Long id) {
        return seanceRepository.findById(id);
    }
    
    public List<Seance> getSeancesByCours(Long coursId) {
        return seanceRepository.findByCoursId(coursId);
    }
    
    public Seance planifierSeance(Long coursId, LocalDateTime dateHeureDebut, 
                                  LocalDateTime dateHeureFin, String salle, String type) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        
        // Vérifier les conflits pour le formateur
        List<Seance> conflitsFormateur = seanceRepository.findConflitsFormateur(
                cours.getFormateur().getId(), dateHeureDebut, dateHeureFin);
        if (!conflitsFormateur.isEmpty()) {
            throw new RuntimeException("Conflit d'horaires pour le formateur");
        }
        
        Seance seance = new Seance(dateHeureDebut, dateHeureFin, salle, cours);
        seance.setType(type);
        
        return seanceRepository.save(seance);
    }
    
    public Seance updateSeance(Long id, LocalDateTime dateHeureDebut, 
                               LocalDateTime dateHeureFin, String salle, String type) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));
        
        // Vérifier les conflits pour le formateur (exclure la séance actuelle)
        List<Seance> conflitsFormateur = seanceRepository.findConflitsFormateur(
                seance.getCours().getFormateur().getId(), dateHeureDebut, dateHeureFin);
        conflitsFormateur.removeIf(s -> s.getId().equals(id));
        if (!conflitsFormateur.isEmpty()) {
            throw new RuntimeException("Conflit d'horaires pour le formateur");
        }
        
        seance.setDateHeureDebut(dateHeureDebut);
        seance.setDateHeureFin(dateHeureFin);
        seance.setSalle(salle);
        seance.setType(type);
        
        return seanceRepository.save(seance);
    }
    
    public void deleteSeance(Long id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));
        seanceRepository.delete(seance);
    }
    
    public List<Seance> getEmploiDuTempsEtudiant(Long etudiantId) {
        // Récupérer toutes les séances des cours où l'étudiant est inscrit
        return seanceRepository.findAll().stream()
                .filter(seance -> seance.getCours().getInscriptions().stream()
                        .anyMatch(inscription -> inscription.getEtudiant().getId().equals(etudiantId) &&
                                inscription.getStatut().equals(com.iit.formation.entity.Inscription.StatutInscription.ACTIVE)))
                .toList();
    }
}






