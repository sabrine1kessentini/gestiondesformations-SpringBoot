package com.iit.formation.controller.api;

import com.iit.formation.entity.Cours;
import com.iit.formation.service.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
public class CoursRestController {
    
    @Autowired
    private CoursService coursService;
    
    @GetMapping
    public ResponseEntity<List<Cours>> getAllCours() {
        return ResponseEntity.ok(coursService.getAllCours());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable Long id) {
        return coursService.getCoursById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/formateur/{formateurId}")
    public ResponseEntity<List<Cours>> getCoursByFormateur(@PathVariable Long formateurId) {
        return ResponseEntity.ok(coursService.getCoursByFormateur(formateurId));
    }
    
    @GetMapping("/populaires")
    public ResponseEntity<List<Cours>> getCoursLesPlusSuivis() {
        return ResponseEntity.ok(coursService.getCoursLesPlusSuivis());
    }
    
    @PostMapping
    public ResponseEntity<Cours> createCours(@RequestBody Cours cours) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(coursService.createCours(cours));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody Cours cours) {
        try {
            return ResponseEntity.ok(coursService.updateCours(id, cours));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        try {
            coursService.deleteCours(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{coursId}/groupes/{groupeId}")
    public ResponseEntity<Cours> ajouterGroupe(@PathVariable Long coursId, @PathVariable Long groupeId) {
        try {
            return ResponseEntity.ok(coursService.ajouterGroupe(coursId, groupeId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{coursId}/groupes/{groupeId}")
    public ResponseEntity<Cours> retirerGroupe(@PathVariable Long coursId, @PathVariable Long groupeId) {
        try {
            return ResponseEntity.ok(coursService.retirerGroupe(coursId, groupeId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}






