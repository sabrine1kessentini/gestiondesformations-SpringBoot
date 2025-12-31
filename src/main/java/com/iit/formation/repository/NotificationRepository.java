package com.iit.formation.repository;

import com.iit.formation.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByFormateurIdOrderByDateCreationDesc(Long formateurId);
    List<Notification> findByFormateurIdAndLueFalseOrderByDateCreationDesc(Long formateurId);
    long countByFormateurIdAndLueFalse(Long formateurId);
}

