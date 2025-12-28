package com.iit.formation.controller.api;

import com.iit.formation.entity.Etudiant;
import com.iit.formation.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantRestController {
    
    @Autowired
    private EtudiantService etudiantService;
    
    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        return ResponseEntity.ok(etudiantService.getAllEtudiants());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        return etudiantService.getEtudiantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Etudiant> getEtudiantByMatricule(@PathVariable String matricule) {
        return etudiantService.getEtudiantByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant etudiant) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(etudiantService.createEtudiant(etudiant));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiant) {
        try {
            return ResponseEntity.ok(etudiantService.updateEtudiant(id, etudiant));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        try {
            etudiantService.deleteEtudiant(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/moyenne")
    public ResponseEntity<Double> getMoyenneGenerale(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(etudiantService.getMoyenneGenerale(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}






