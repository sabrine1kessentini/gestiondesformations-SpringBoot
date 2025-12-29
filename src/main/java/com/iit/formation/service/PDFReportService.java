package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Note;
import com.iit.formation.repository.CoursRepository;
import com.iit.formation.repository.NoteRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PDFReportService {
    
    @Autowired
    private CoursRepository coursRepository;
    
    @Autowired
    private NoteRepository noteRepository;
    
    /**
     * Génère un rapport PDF des notes pour un cours donné
     * @param coursId L'ID du cours
     * @return Le byte array du PDF généré
     */
    public byte[] genererRapportNotes(Long coursId) throws Exception {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        
        List<Note> notes = noteRepository.findByCoursId(coursId);
        
        // Vérifier que le template existe
        ClassPathResource resource = new ClassPathResource("reports/notes-report.jrxml");
        if (!resource.exists()) {
            throw new RuntimeException("Template JasperReports non trouvé: reports/notes-report.jrxml");
        }
        
        // Charger le template JasperReports
        InputStream templateStream = null;
        JasperReport jasperReport;
        try {
            templateStream = resource.getInputStream();
            jasperReport = JasperCompileManager.compileReport(templateStream);
        } catch (JRException e) {
            if (templateStream != null) {
                try { templateStream.close(); } catch (Exception ignored) {}
            }
            throw new RuntimeException("Erreur lors de la compilation du template JasperReports: " + e.getMessage() + 
                    ". Détails: " + (e.getCause() != null ? e.getCause().getMessage() : ""), e);
        } finally {
            if (templateStream != null) {
                try { templateStream.close(); } catch (Exception ignored) {}
            }
        }
        
        // Préparer les paramètres
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("coursCode", cours.getCode() != null ? cours.getCode() : "");
        parameters.put("coursTitre", cours.getTitre() != null ? cours.getTitre() : "");
        parameters.put("formateurNom", cours.getFormateur() != null ? 
                cours.getFormateur().getPrenom() + " " + cours.getFormateur().getNom() : "N/A");
        parameters.put("nombreInscrits", cours.getNombreInscrits());
        parameters.put("nombreNotes", notes.size());
        parameters.put("tauxReussite", cours.getTauxReussite());
        parameters.put("dateGeneration", java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Préparer les données - mapper les notes vers un format compatible avec le template
        List<Map<String, Object>> noteData = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Note note : notes) {
            Map<String, Object> noteMap = new HashMap<>();
            noteMap.put("etudiantNom", note.getEtudiant() != null ? note.getEtudiant().getNom() : "");
            noteMap.put("etudiantPrenom", note.getEtudiant() != null ? note.getEtudiant().getPrenom() : "");
            noteMap.put("valeur", note.getValeur() != null ? note.getValeur() : 0.0);
            noteMap.put("commentaire", note.getCommentaire() != null ? note.getCommentaire() : "");
            noteMap.put("dateAttribution", note.getDateAttribution() != null ? 
                    note.getDateAttribution().format(dateFormatter) : "");
            noteData.add(noteMap);
        }
        
        // Si aucune note, créer une liste vide pour éviter les erreurs
        if (noteData.isEmpty()) {
            Map<String, Object> emptyNote = new HashMap<>();
            emptyNote.put("etudiantNom", "");
            emptyNote.put("etudiantPrenom", "");
            emptyNote.put("valeur", 0.0);
            emptyNote.put("commentaire", "Aucune note");
            emptyNote.put("dateAttribution", "");
            noteData.add(emptyNote);
        }
        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(noteData);
        
        // Générer le rapport
        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        } catch (JRException e) {
            throw new RuntimeException("Erreur lors de la génération du rapport: " + e.getMessage(), e);
        }
        
        // Exporter en PDF
        try {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Erreur lors de l'export PDF: " + e.getMessage(), e);
        }
    }
}

