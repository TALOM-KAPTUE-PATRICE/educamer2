package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    // Trouver toutes les soumissions pour un devoir spécifique
    List<Submission> findByAssignmentId(Long assignmentId);

        @Query("SELECT AVG(s.grade) FROM Submission s WHERE s.assignment.course.id = :courseId AND s.grade IS NOT NULL")
    Optional<Double> findAverageGradeByCourseId(Long courseId);
    long countByAssignment_CourseIdAndGradeIsNull(Long courseId);

    // Trouver la soumission d'un étudiant pour un devoir spécifique
    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    // Trouver toutes les soumissions d'un étudiant pour un cours spécifique
    List<Submission> findByStudentIdAndAssignment_CourseId(Long studentId, Long courseId);


    long countByStudentId(Long studentId);

    @Query("SELECT COUNT(a) FROM Assignment a JOIN a.course.enrollments e WHERE e.student.id = :studentId")
    long countTotalAssignmentsForStudent(Long studentId);


}
