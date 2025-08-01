package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Vérifier si un étudiant est déjà inscrit à un cours
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    // Trouver toutes les inscriptions pour un étudiant
    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);
    long countByStudentId(Long studentId);

    long countByCourseId(Long courseId);
    @Query("SELECT AVG(e.progress) FROM Enrollment e WHERE e.course.id = :courseId")
    Optional<Double> findAverageProgressByCourseId(Long courseId);
    
        // Nouvelle méthode pour la sécurité
    boolean existsByStudent_EmailAndCourseId(String studentEmail, Long courseId);
}