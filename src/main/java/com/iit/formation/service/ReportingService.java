package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Note;
import com.iit.formation.repository.CoursRepository;
import com.iit.formation.repository.EtudiantRepository;
import com.iit.formation.repository.FormateurRepository;
import com.iit.formation.repository.NoteRepository;
import com.iit.formation.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingService {
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private CoursRepository coursRepository;
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private FormateurRepository formateurRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    public Map<String, Object> getStatistiquesEtudiant(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("etudiant", etudiant);
        stats.put("moyenneGenerale", etudiant.getMoyenneGenerale());
        stats.put("nombreCours", etudiant.getInscriptions().size());
        stats.put("nombreNotes", etudiant.getNotes().size());
        
        return stats;
    }
    
    public Map<String, Object> getStatistiquesCours(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("cours", cours);
        stats.put("nombreInscrits", cours.getNombreInscrits());
        stats.put("tauxReussite", cours.getTauxReussite());
        stats.put("nombreNotes", cours.getNotes().size());
        
        return stats;
    }
    
    public List<Cours> getCoursLesPlusSuivis() {
        return coursRepository.findCoursLesPlusSuivis();
    }
    
    public Map<String, Object> getTableauDeBord() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalEtudiants", etudiantRepository.count());
        dashboard.put("totalCours", coursRepository.count());
        dashboard.put("totalFormateurs", formateurRepository.count());
        dashboard.put("totalSessions", sessionRepository.count());
        dashboard.put("coursLesPlusSuivis", getCoursLesPlusSuivis());
        
        return dashboard;
    }
}






