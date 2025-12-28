package com.iit.formation.controller.api;

import com.iit.formation.entity.Seance;
import com.iit.formation.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/seances")
public class SeanceRestController {
    
    @Autowired
    private SeanceService seanceService;
    
    @GetMapping
    public ResponseEntity<List<Seance>> getAllSeances() {
        return ResponseEntity.ok(seanceService.getAllSeances());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Seance> getSeanceById(@PathVariable Long id) {
        return seanceService.getSeanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Seance>> getSeancesByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(seanceService.getSeancesByCours(coursId));
    }
    
    @GetMapping("/etudiant/{etudiantId}/emploi-du-temps")
    public ResponseEntity<List<Seance>> getEmploiDuTempsEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(seanceService.getEmploiDuTempsEtudiant(etudiantId));
    }
    
    @PostMapping("/cours/{coursId}")
    public ResponseEntity<Seance> planifierSeance(
            @PathVariable Long coursId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateHeureDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateHeureFin,
            @RequestParam String salle,
            @RequestParam(required = false) String type) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(seanceService.planifierSeance(coursId, dateHeureDebut, dateHeureFin, salle, type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Seance> updateSeance(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateHeureDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateHeureFin,
            @RequestParam String salle,
            @RequestParam(required = false) String type) {
        try {
            return ResponseEntity.ok(seanceService.updateSeance(id, dateHeureDebut, dateHeureFin, salle, type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        try {
            seanceService.deleteSeance(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}






