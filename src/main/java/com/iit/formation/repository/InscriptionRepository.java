package com.iit.formation.repository;

import com.iit.formation.entity.Inscription;
import com.iit.formation.entity.Inscription.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    Optional<Inscription> findByEtudiantIdAndCoursId(Long etudiantId, Long coursId);
    List<Inscription> findByEtudiantId(Long etudiantId);
    List<Inscription> findByCoursId(Long coursId);
    List<Inscription> findByStatut(StatutInscription statut);
    boolean existsByEtudiantIdAndCoursId(Long etudiantId, Long coursId);
}






