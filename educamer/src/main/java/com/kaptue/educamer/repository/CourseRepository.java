package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Course;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.kaptue.educamer.entity.enums.CourseStatus;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Trouver tous les cours d'un instructeur spécifique
    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByStatus(CourseStatus status);
        // Nouvelle méthode pour la sécurité
    boolean existsByIdAndInstructor_Email(Long courseId, String instructorEmail);

    @Query("SELECT c FROM Course c WHERE c.status = :status ORDER BY SIZE(c.enrollments) DESC")
    List<Course> findTrendingCourses(CourseStatus status, Pageable pageable);

    @Query("SELECT c FROM Course c JOIN c.feedbacks f WHERE c.status = :status GROUP BY c ORDER BY AVG(f.rating) DESC")
    List<Course> findTopRatedCourses(CourseStatus status, Pageable pageable);
    
    List<Course> findByCategories_IdAndStatus(Long categoryId, CourseStatus status);
}