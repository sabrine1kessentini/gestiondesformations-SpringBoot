package com.iit.formation.repository;

import com.iit.formation.entity.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByCoursId(Long coursId);
    
    @Query("SELECT s FROM Seance s WHERE s.cours.formateur.id = :formateurId " +
           "AND ((s.dateHeureDebut <= :fin AND s.dateHeureFin >= :debut))")
    List<Seance> findConflitsFormateur(@Param("formateurId") Long formateurId,
                                       @Param("debut") LocalDateTime debut,
                                       @Param("fin") LocalDateTime fin);
    
    @Query("SELECT s FROM Seance s JOIN s.cours.inscriptions i WHERE i.etudiant.id = :etudiantId " +
           "AND i.statut = 'ACTIVE' AND ((s.dateHeureDebut <= :fin AND s.dateHeureFin >= :debut))")
    List<Seance> findConflitsEtudiant(@Param("etudiantId") Long etudiantId,
                                      @Param("debut") LocalDateTime debut,
                                      @Param("fin") LocalDateTime fin);
}






