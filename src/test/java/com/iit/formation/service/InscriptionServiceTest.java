package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Formateur;
import com.iit.formation.entity.Inscription;
import com.iit.formation.entity.Inscription.StatutInscription;
import com.iit.formation.repository.CoursRepository;
import com.iit.formation.repository.EtudiantRepository;
import com.iit.formation.repository.InscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscriptionServiceTest {
    
    @Mock
    private InscriptionRepository inscriptionRepository;
    
    @Mock
    private EtudiantRepository etudiantRepository;
    
    @Mock
    private CoursRepository coursRepository;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private InscriptionService inscriptionService;
    
    private Etudiant etudiant;
    private Formateur formateur;
    private Cours cours;
    private Inscription inscription;
    
    @BeforeEach
    void setUp() {
        // Créer un formateur
        formateur = new Formateur();
        formateur.setId(1L);
        formateur.setNom("Dupont");
        formateur.setPrenom("Jean");
        formateur.setEmail("jean.dupont@formation.com");
        
        // Créer un cours
        cours = new Cours();
        cours.setId(1L);
        cours.setCode("JAVA-101");
        cours.setTitre("Programmation Java");
        cours.setFormateur(formateur);
        
        // Créer un étudiant
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Martin");
        etudiant.setPrenom("Sophie");
        etudiant.setEmail("sophie.martin@etudiant.com");
        
        // Créer une inscription
        inscription = new Inscription(etudiant, cours);
        inscription.setId(1L);
        inscription.setStatut(StatutInscription.ACTIVE);
    }
    
    @Test
    void testInscrireEtudiant_NouvelleInscription_EnvoieEmail() {
        // Arrange
        when(inscriptionRepository.findByEtudiantIdAndCoursId(1L, 1L))
                .thenReturn(Optional.empty());
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(coursRepository.findById(1L)).thenReturn(Optional.of(cours));
        when(inscriptionRepository.save(any(Inscription.class))).thenReturn(inscription);
        
        // Act
        Inscription result = inscriptionService.inscrireEtudiant(1L, 1L);
        
        // Assert
        assertNotNull(result);
        verify(inscriptionRepository).save(any(Inscription.class));
        verify(emailService, times(1)).envoyerEmailInscription(etudiant, cours);
    }
    
    @Test
    void testInscrireEtudiant_InscriptionExistanteActive_LanceException() {
        // Arrange
        inscription.setStatut(StatutInscription.ACTIVE);
        when(inscriptionRepository.findByEtudiantIdAndCoursId(1L, 1L))
                .thenReturn(Optional.of(inscription));
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            inscriptionService.inscrireEtudiant(1L, 1L);
        });
        
        verify(emailService, never()).envoyerEmailInscription(any(), any());
    }
    
    @Test
    void testInscrireEtudiant_InscriptionAnnulee_ReactiveEtEnvoieEmail() {
        // Arrange
        inscription.setStatut(StatutInscription.ANNULEE);
        when(inscriptionRepository.findByEtudiantIdAndCoursId(1L, 1L))
                .thenReturn(Optional.of(inscription));
        when(inscriptionRepository.save(inscription)).thenReturn(inscription);
        
        // Act
        Inscription result = inscriptionService.inscrireEtudiant(1L, 1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(StatutInscription.ACTIVE, inscription.getStatut());
        verify(inscriptionRepository).save(inscription);
        verify(emailService, times(1)).envoyerEmailInscription(etudiant, cours);
    }
    
    @Test
    void testAnnulerInscription_EnvoieEmailDesinscription() {
        // Arrange
        when(inscriptionRepository.findById(1L)).thenReturn(Optional.of(inscription));
        when(inscriptionRepository.save(inscription)).thenReturn(inscription);
        
        // Act
        inscriptionService.annulerInscription(1L);
        
        // Assert
        assertEquals(StatutInscription.ANNULEE, inscription.getStatut());
        verify(inscriptionRepository).save(inscription);
        verify(emailService, times(1)).envoyerEmailDesinscription(
                etudiant, cours, formateur);
    }
    
    @Test
    void testAnnulerInscription_ErreurEmail_NeLancePasException() {
        // Arrange
        when(inscriptionRepository.findById(1L)).thenReturn(Optional.of(inscription));
        when(inscriptionRepository.save(inscription)).thenReturn(inscription);
        doThrow(new RuntimeException("Erreur email")).when(emailService)
                .envoyerEmailDesinscription(any(), any(), any());
        
        // Act & Assert - Ne doit pas lancer d'exception
        assertDoesNotThrow(() -> {
            inscriptionService.annulerInscription(1L);
        });
        
        // L'inscription doit quand même être annulée
        assertEquals(StatutInscription.ANNULEE, inscription.getStatut());
    }
    
    @Test
    void testInscrireEtudiant_ErreurEmail_NeLancePasException() {
        // Arrange
        when(inscriptionRepository.findByEtudiantIdAndCoursId(1L, 1L))
                .thenReturn(Optional.empty());
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(coursRepository.findById(1L)).thenReturn(Optional.of(cours));
        when(inscriptionRepository.save(any(Inscription.class))).thenReturn(inscription);
        doThrow(new RuntimeException("Erreur email")).when(emailService)
                .envoyerEmailInscription(any(), any());
        
        // Act & Assert - Ne doit pas lancer d'exception
        assertDoesNotThrow(() -> {
            Inscription result = inscriptionService.inscrireEtudiant(1L, 1L);
            assertNotNull(result);
        });
    }
}

