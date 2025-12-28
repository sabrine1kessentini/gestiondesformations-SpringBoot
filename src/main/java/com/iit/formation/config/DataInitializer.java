package com.iit.formation.config;

import com.iit.formation.entity.*;
import com.iit.formation.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private FormateurRepository formateurRepository;
    
    @Autowired
    private GroupeRepository groupeRepository;
    
    @Autowired
    private SpecialiteRepository specialiteRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private CoursRepository coursRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Créer des données de test seulement si la base est vide
        if (etudiantRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        // Créer un admin
        Etudiant admin = new Etudiant();
        admin.setMatricule("ADMIN001");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@iit.tn");
        admin.setNom("Admin");
        admin.setPrenom("Administrateur");
        admin.addRole(Utilisateur.Role.ADMIN);
        admin.addRole(Utilisateur.Role.ETUDIANT);
        etudiantRepository.save(admin);
        
        // Créer des spécialités
        Specialite info = new Specialite("Informatique", "Spécialité en informatique");
        Specialite reseaux = new Specialite("Réseaux", "Spécialité en réseaux");
        Specialite ia = new Specialite("Intelligence Artificielle", "Spécialité en IA");
        specialiteRepository.save(info);
        specialiteRepository.save(reseaux);
        specialiteRepository.save(ia);
        
        // Créer des groupes
        Groupe groupe1 = new Groupe("TP1", "Groupe TP 1");
        Groupe groupe2 = new Groupe("TP2", "Groupe TP 2");
        groupeRepository.save(groupe1);
        groupeRepository.save(groupe2);
        
        // Créer des sessions
        Session session1 = new Session("S1 2025-2026", 
                LocalDate.of(2025, 9, 1), 
                LocalDate.of(2026, 1, 31), 
                Session.TypeSession.SEMESTRE);
        sessionRepository.save(session1);
        
        // Créer des formateurs
        Formateur formateur1 = new Formateur("formateur1", passwordEncoder.encode("formateur1"), 
                "formateur1@iit.tn", "Dupont", "Jean", "Informatique");
        Formateur formateur2 = new Formateur("formateur2", passwordEncoder.encode("formateur2"), 
                "formateur2@iit.tn", "Martin", "Marie", "Réseaux");
        formateurRepository.save(formateur1);
        formateurRepository.save(formateur2);
        
        // Créer des étudiants
        Etudiant etudiant1 = new Etudiant("ETU001", "etudiant1", passwordEncoder.encode("etudiant1"), 
                "etudiant1@iit.tn", "Ben", "Ali");
        etudiant1.setGroupe(groupe1);
        Etudiant etudiant2 = new Etudiant("ETU002", "etudiant2", passwordEncoder.encode("etudiant2"), 
                "etudiant2@iit.tn", "Ben", "Fatma");
        etudiant2.setGroupe(groupe1);
        Etudiant etudiant3 = new Etudiant("ETU003", "etudiant3", passwordEncoder.encode("etudiant3"), 
                "etudiant3@iit.tn", "Trabelsi", "Mohamed");
        etudiant3.setGroupe(groupe2);
        etudiantRepository.save(etudiant1);
        etudiantRepository.save(etudiant2);
        etudiantRepository.save(etudiant3);
        
        // Créer des cours
        Cours cours1 = new Cours("INF101", "Programmation Java", 
                "Introduction à la programmation orientée objet avec Java", formateur1);
        cours1.setSpecialite(info);
        cours1.setSession(session1);
        cours1.getGroupes().add(groupe1);
        coursRepository.save(cours1);
        
        Cours cours2 = new Cours("RES201", "Réseaux TCP/IP", 
                "Fondamentaux des réseaux et protocoles TCP/IP", formateur2);
        cours2.setSpecialite(reseaux);
        cours2.setSession(session1);
        cours2.getGroupes().add(groupe1);
        cours2.getGroupes().add(groupe2);
        coursRepository.save(cours2);
        
        System.out.println("Données initiales créées avec succès!");
        System.out.println("Admin: admin/admin");
        System.out.println("Formateur: formateur1/formateur1");
        System.out.println("Étudiant: etudiant1/etudiant1");
    }
}

