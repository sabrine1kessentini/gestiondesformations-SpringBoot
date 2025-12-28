package com.iit.formation.repository;

import com.iit.formation.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    Optional<Cours> findByCode(String code);
    boolean existsByCode(String code);
    List<Cours> findByFormateurId(Long formateurId);
    List<Cours> findBySpecialiteId(Long specialiteId);
    
    @Query("SELECT c FROM Cours c ORDER BY SIZE(c.inscriptions) DESC")
    List<Cours> findCoursLesPlusSuivis();
}






