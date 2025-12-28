package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Note;
import com.iit.formation.repository.CoursRepository;
import com.iit.formation.repository.EtudiantRepository;
import com.iit.formation.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private CoursRepository coursRepository;
    
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
    
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
    
    public List<Note> getNotesByEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }
    
    public List<Note> getNotesByCours(Long coursId) {
        return noteRepository.findByCoursId(coursId);
    }
    
    public Note attribuerNote(Long etudiantId, Long coursId, Double valeur, String commentaire) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        
        Note note = noteRepository.findByEtudiantIdAndCoursId(etudiantId, coursId)
                .orElse(new Note());
        
        note.setEtudiant(etudiant);
        note.setCours(cours);
        note.setValeur(valeur);
        note.setCommentaire(commentaire);
        
        return noteRepository.save(note);
    }
    
    public Note updateNote(Long id, Double valeur, String commentaire) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        
        note.setValeur(valeur);
        note.setCommentaire(commentaire);
        
        return noteRepository.save(note);
    }
    
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        noteRepository.delete(note);
    }
    
    public double getTauxReussite(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        return cours.getTauxReussite();
    }
}






