package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Inscription;
import com.iit.formation.entity.Inscription.StatutInscription;
import com.iit.formation.repository.CoursRepository;
import com.iit.formation.repository.EtudiantRepository;
import com.iit.formation.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InscriptionService {
    
    @Autowired
    private InscriptionRepository inscriptionRepository;
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private CoursRepository coursRepository;
    
    @Autowired
    private EmailService emailService;
    
    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }
    
    public Optional<Inscription> getInscriptionById(Long id) {
        return inscriptionRepository.findById(id);
    }
    
    public List<Inscription> getInscriptionsByEtudiant(Long etudiantId) {
        return inscriptionRepository.findByEtudiantId(etudiantId);
    }
    
    public List<Inscription> getInscriptionsByCours(Long coursId) {
        return inscriptionRepository.findByCoursId(coursId);
    }
    
    public Inscription inscrireEtudiant(Long etudiantId, Long coursId) {
        if (inscriptionRepository.existsByEtudiantIdAndCoursId(etudiantId, coursId)) {
            throw new RuntimeException("L'étudiant est déjà inscrit à ce cours");
        }
        
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        
        Inscription inscription = new Inscription(etudiant, cours);
        inscription = inscriptionRepository.save(inscription);
        
        // Envoyer un email de notification
        try {
            emailService.envoyerEmailInscription(etudiant, cours);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer l'inscription
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
        
        return inscription;
    }
    
    public void annulerInscription(Long inscriptionId) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));
        
        inscription.setStatut(StatutInscription.ANNULEE);
        inscriptionRepository.save(inscription);
        
        // Notifier le formateur
        try {
            emailService.envoyerEmailDesinscription(inscription.getEtudiant(), 
                    inscription.getCours(), inscription.getCours().getFormateur());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }
    
    public void annulerInscription(Long etudiantId, Long coursId) {
        Inscription inscription = inscriptionRepository
                .findByEtudiantIdAndCoursId(etudiantId, coursId)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));
        annulerInscription(inscription.getId());
    }
}






