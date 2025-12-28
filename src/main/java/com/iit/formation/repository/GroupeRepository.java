package com.iit.formation.repository;

import com.iit.formation.entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    Optional<Groupe> findByNom(String nom);
    boolean existsByNom(String nom);
}






