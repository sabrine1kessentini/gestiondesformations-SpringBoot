package com.iit.formation.service;

import com.iit.formation.entity.Cours;
import com.iit.formation.entity.Etudiant;
import com.iit.formation.entity.Formateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    
    @Mock
    private JavaMailSender mailSender;
    
    @InjectMocks
    private EmailService emailService;
    
    private Etudiant etudiant;
    private Formateur formateur;
    private Cours cours;
    
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
        cours.setDescription("Cours d'introduction à Java");
        cours.setFormateur(formateur);
        
        // Créer un étudiant
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Martin");
        etudiant.setPrenom("Sophie");
        etudiant.setEmail("sophie.martin@etudiant.com");
    }
    
    @Test
    void testEnvoyerEmailInscription_AvecMailSender_EnvoieEmailEtudiant() {
        // Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        emailService.envoyerEmailInscription(etudiant, cours);
        
        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(messageCaptor.capture());
        
        SimpleMailMessage messageEtudiant = messageCaptor.getAllValues().get(0);
        assertEquals(etudiant.getEmail(), messageEtudiant.getTo()[0]);
        assertTrue(messageEtudiant.getSubject().contains("Inscription au cours"));
        assertTrue(messageEtudiant.getSubject().contains(cours.getTitre()));
        assertTrue(messageEtudiant.getText().contains(etudiant.getPrenom()));
        assertTrue(messageEtudiant.getText().contains(etudiant.getNom()));
        assertTrue(messageEtudiant.getText().contains(cours.getTitre()));
        assertTrue(messageEtudiant.getText().contains(cours.getCode()));
    }
    
    @Test
    void testEnvoyerEmailInscription_AvecMailSender_EnvoieEmailFormateur() {
        // Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        emailService.envoyerEmailInscription(etudiant, cours);
        
        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(messageCaptor.capture());
        
        SimpleMailMessage messageFormateur = messageCaptor.getAllValues().get(1);
        assertEquals(formateur.getEmail(), messageFormateur.getTo()[0]);
        assertTrue(messageFormateur.getSubject().contains("Notification"));
        assertTrue(messageFormateur.getSubject().contains("Nouvel étudiant inscrit"));
        assertTrue(messageFormateur.getText().contains(etudiant.getPrenom()));
        assertTrue(messageFormateur.getText().contains(etudiant.getNom()));
        assertTrue(messageFormateur.getText().contains(etudiant.getEmail()));
        assertTrue(messageFormateur.getText().contains(cours.getTitre()));
    }
    
    @Test
    void testEnvoyerEmailInscription_SansFormateur_EnvoieSeulementEmailEtudiant() {
        // Arrange
        cours.setFormateur(null);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        emailService.envoyerEmailInscription(etudiant, cours);
        
        // Assert - Seulement un email à l'étudiant
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void testEnvoyerEmailDesinscription_AvecMailSender_EnvoieEmails() {
        // Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        emailService.envoyerEmailDesinscription(etudiant, cours, formateur);
        
        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(messageCaptor.capture());
        
        // Email à l'étudiant
        SimpleMailMessage messageEtudiant = messageCaptor.getAllValues().get(0);
        assertEquals(etudiant.getEmail(), messageEtudiant.getTo()[0]);
        assertTrue(messageEtudiant.getSubject().contains("Annulation d'inscription"));
        assertTrue(messageEtudiant.getText().contains(etudiant.getPrenom()));
        
        // Email au formateur
        SimpleMailMessage messageFormateur = messageCaptor.getAllValues().get(1);
        assertEquals(formateur.getEmail(), messageFormateur.getTo()[0]);
        assertTrue(messageFormateur.getSubject().contains("Désinscription"));
        assertTrue(messageFormateur.getText().contains(etudiant.getPrenom()));
        assertTrue(messageFormateur.getText().contains(etudiant.getNom()));
    }
    
    @Test
    void testEnvoyerEmailDesinscription_SansFormateur_EnvoieSeulementEmailEtudiant() {
        // Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        emailService.envoyerEmailDesinscription(etudiant, cours, null);
        
        // Assert - Seulement un email à l'étudiant
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void testEnvoyerEmailInscription_SansMailSender_SimuleEmail() {
        // Arrange - Créer un service sans mailSender (null)
        EmailService emailServiceSansMail = new EmailService();
        // Le mailSender sera null par défaut, donc le mode simulation sera utilisé
        
        // Act & Assert - Ne doit pas lancer d'exception
        assertDoesNotThrow(() -> {
            emailServiceSansMail.envoyerEmailInscription(etudiant, cours);
        });
    }
}

