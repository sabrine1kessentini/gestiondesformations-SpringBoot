package com.iit.formation.repository;

import com.iit.formation.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByNom(String nom);
    boolean existsByNom(String nom);
}






