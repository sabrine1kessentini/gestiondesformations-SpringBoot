package com.iit.formation.repository;

import com.iit.formation.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByMatricule(String matricule);
    boolean existsByMatricule(String matricule);
}






