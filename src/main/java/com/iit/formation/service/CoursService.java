package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Formateur;
import com.iit.formation.entity.Groupe;
import com.iit.formation.entity.Specialite;
import com.iit.formation.entity.Session;
import com.iit.formation.repository.CoursRepository;
import com.iit.formation.repository.FormateurRepository;
import com.iit.formation.repository.GroupeRepository;
import com.iit.formation.repository.SpecialiteRepository;
import com.iit.formation.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoursService {
    
    @Autowired
    private CoursRepository coursRepository;
    
    @Autowired
    private FormateurRepository formateurRepository;
    
    @Autowired
    private SpecialiteRepository specialiteRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private GroupeRepository groupeRepository;
    
    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }
    
    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }
    
    public Optional<Cours> getCoursByCode(String code) {
        return coursRepository.findByCode(code);
    }
    
    public List<Cours> getCoursByFormateur(Long formateurId) {
        return coursRepository.findByFormateurId(formateurId);
    }
    
    public List<Cours> getCoursLesPlusSuivis() {
        return coursRepository.findCoursLesPlusSuivis();
    }
    
    public Cours createCours(Cours cours) {
        if (coursRepository.existsByCode(cours.getCode())) {
            throw new RuntimeException("Un cours avec ce code existe déjà");
        }
        
        Formateur formateur = formateurRepository.findById(cours.getFormateur().getId())
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
        cours.setFormateur(formateur);
        
        if (cours.getSpecialite() != null && cours.getSpecialite().getId() != null) {
            Specialite specialite = specialiteRepository.findById(cours.getSpecialite().getId())
                    .orElse(null);
            cours.setSpecialite(specialite);
        }
        
        if (cours.getSession() != null && cours.getSession().getId() != null) {
            Session session = sessionRepository.findById(cours.getSession().getId())
                    .orElse(null);
            cours.setSession(session);
        }
        
        return coursRepository.save(cours);
    }
    
    public Cours updateCours(Long id, Cours coursDetails) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        
        if (!cours.getCode().equals(coursDetails.getCode()) &&
            coursRepository.existsByCode(coursDetails.getCode())) {
            throw new RuntimeException("Un cours avec ce code existe déjà");
        }
        
        cours.setCode(coursDetails.getCode());
        cours.setTitre(coursDetails.getTitre());
        cours.setDescription(coursDetails.getDescription());
        
        if (coursDetails.getFormateur() != null) {
            Formateur formateur = formateurRepository.findById(coursDetails.getFormateur().getId())
                    .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
            cours.setFormateur(formateur);
        }
        
        if (coursDetails.getSpecialite() != null && coursDetails.getSpecialite().getId() != null) {
            Specialite specialite = specialiteRepository.findById(coursDetails.getSpecialite().getId())
                    .orElse(null);
            cours.setSpecialite(specialite);
        }
        
        if (coursDetails.getSession() != null && coursDetails.getSession().getId() != null) {
            Session session = sessionRepository.findById(coursDetails.getSession().getId())
                    .orElse(null);
            cours.setSession(session);
        }
        
        return coursRepository.save(cours);
    }
    
    public void deleteCours(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        coursRepository.delete(cours);
    }
    
    public Cours ajouterGroupe(Long coursId, Long groupeId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));
        
        if (!cours.getGroupes().contains(groupe)) {
            cours.getGroupes().add(groupe);
        }
        
        return coursRepository.save(cours);
    }
    
    public Cours retirerGroupe(Long coursId, Long groupeId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));
        
        cours.getGroupes().remove(groupe);
        return coursRepository.save(cours);
    }
}






