package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Méthodes spécifiques aux administrateurs si nécessaire.
}