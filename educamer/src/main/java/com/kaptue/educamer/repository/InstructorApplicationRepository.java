package com.kaptue.educamer.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kaptue.educamer.entity.PedagogicalApplication;
import com.kaptue.educamer.entity.enums.ApplicationStatus;


@Repository
public interface InstructorApplicationRepository extends JpaRepository<PedagogicalApplication, Long> {
    // Méthodes spécifiques aux administrateurs si nécessaire.

    List<PedagogicalApplication> findByStatus(ApplicationStatus status);
}