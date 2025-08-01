package com.kaptue.educamer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaptue.educamer.entity.Parent;

// Dans ParentRepository.java
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByEmail(String email);
        // Nouvelle méthode pour la sécurité
    boolean existsByEmailAndChildren_Id(String parentEmail, Long studentId);
}