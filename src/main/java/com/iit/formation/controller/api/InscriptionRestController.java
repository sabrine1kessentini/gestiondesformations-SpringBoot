package com.iit.formation.controller.api;

import com.iit.formation.entity.Inscription;
import com.iit.formation.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionRestController {
    
    @Autowired
    private InscriptionService inscriptionService;
    
    @GetMapping
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable Long id) {
        return inscriptionService.getInscriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Inscription>> getInscriptionsByEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByEtudiant(etudiantId));
    }
    
    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Inscription>> getInscriptionsByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByCours(coursId));
    }
    
    @PostMapping("/etudiant/{etudiantId}/cours/{coursId}")
    public ResponseEntity<Inscription> inscrireEtudiant(
            @PathVariable Long etudiantId,
            @PathVariable Long coursId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(inscriptionService.inscrireEtudiant(etudiantId, coursId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> annulerInscription(@PathVariable Long id) {
        try {
            inscriptionService.annulerInscription(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/etudiant/{etudiantId}/cours/{coursId}")
    public ResponseEntity<Void> annulerInscription(
            @PathVariable Long etudiantId,
            @PathVariable Long coursId) {
        try {
            inscriptionService.annulerInscription(etudiantId, coursId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}






