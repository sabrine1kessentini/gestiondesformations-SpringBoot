package com.iit.formation.repository;

import com.iit.formation.entity.MockEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockEmailRepository extends JpaRepository<MockEmail, Long> {
    List<MockEmail> findByToEmailOrderByDateEnvoiDesc(String toEmail);
    List<MockEmail> findByToEmailAndEstLuFalseOrderByDateEnvoiDesc(String toEmail);
    long countByToEmailAndEstLuFalse(String toEmail);
    List<MockEmail> findAllByOrderByDateEnvoiDesc();
}

