package com.iit.formation.service;

import com.iit.formation.entity.Session;
import com.iit.formation.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionService {
    
    @Autowired
    private SessionRepository sessionRepository;
    
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
    
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }
    
    public Session createSession(Session session) {
        if (sessionRepository.existsByNom(session.getNom())) {
            throw new RuntimeException("Une session avec ce nom existe déjà");
        }
        return sessionRepository.save(session);
    }
    
    public Session updateSession(Long id, Session sessionDetails) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        
        if (!session.getNom().equals(sessionDetails.getNom()) &&
            sessionRepository.existsByNom(sessionDetails.getNom())) {
            throw new RuntimeException("Une session avec ce nom existe déjà");
        }
        
        session.setNom(sessionDetails.getNom());
        session.setDateDebut(sessionDetails.getDateDebut());
        session.setDateFin(sessionDetails.getDateFin());
        session.setType(sessionDetails.getType());
        
        return sessionRepository.save(session);
    }
    
    public void deleteSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        sessionRepository.delete(session);
    }
}






