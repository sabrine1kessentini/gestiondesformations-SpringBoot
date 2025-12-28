package com.iit.formation.controller.api;

import com.iit.formation.entity.Cours;
import com.iit.formation.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reporting")
public class ReportingRestController {
    
    @Autowired
    private ReportingService reportingService;
    
    @GetMapping("/etudiant/{etudiantId}/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiquesEtudiant(@PathVariable Long etudiantId) {
        try {
            return ResponseEntity.ok(reportingService.getStatistiquesEtudiant(etudiantId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/cours/{coursId}/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiquesCours(@PathVariable Long coursId) {
        try {
            return ResponseEntity.ok(reportingService.getStatistiquesCours(coursId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/cours/populaires")
    public ResponseEntity<List<Cours>> getCoursLesPlusSuivis() {
        return ResponseEntity.ok(reportingService.getCoursLesPlusSuivis());
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getTableauDeBord() {
        return ResponseEntity.ok(reportingService.getTableauDeBord());
    }
}






