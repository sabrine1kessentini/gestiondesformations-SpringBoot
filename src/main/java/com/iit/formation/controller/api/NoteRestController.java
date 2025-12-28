package com.iit.formation.controller.api;

import com.iit.formation.entity.Note;
import com.iit.formation.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteRestController {
    
    @Autowired
    private NoteService noteService;
    
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Note>> getNotesByEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(noteService.getNotesByEtudiant(etudiantId));
    }
    
    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Note>> getNotesByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(noteService.getNotesByCours(coursId));
    }
    
    @PostMapping("/etudiant/{etudiantId}/cours/{coursId}")
    public ResponseEntity<Note> attribuerNote(
            @PathVariable Long etudiantId,
            @PathVariable Long coursId,
            @RequestParam Double valeur,
            @RequestParam(required = false) String commentaire) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(noteService.attribuerNote(etudiantId, coursId, valeur, commentaire));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long id,
            @RequestParam Double valeur,
            @RequestParam(required = false) String commentaire) {
        try {
            return ResponseEntity.ok(noteService.updateNote(id, valeur, commentaire));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/cours/{coursId}/taux-reussite")
    public ResponseEntity<Double> getTauxReussite(@PathVariable Long coursId) {
        try {
            return ResponseEntity.ok(noteService.getTauxReussite(coursId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}






