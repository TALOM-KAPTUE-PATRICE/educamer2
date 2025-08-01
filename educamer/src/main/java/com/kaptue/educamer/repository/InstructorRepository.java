package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    
    Optional<Instructor> findByEmail(String email);

    // Exemple de méthode spécifique : trouver un instructeur par son ID de cours
    // @Query("SELECT c.instructor FROM Course c WHERE c.id = :courseId")
    // Optional<Instructor> findByCourseId(Long courseId);
}
