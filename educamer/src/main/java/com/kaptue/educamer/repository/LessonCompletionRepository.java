// Fichier: LessonCompletionRepository.java
package com.kaptue.educamer.repository;
import com.kaptue.educamer.entity.LessonCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface LessonCompletionRepository extends JpaRepository<LessonCompletion, Long> {
    // Vérifie si un élève a complété une leçon spécifique
    boolean existsByStudent_IdAndLesson_Id(Long studentId, Long lessonId);
    
    // Récupère tous les IDs des leçons complétées par un élève pour un cours donné
    @Query("SELECT lc.lesson.id FROM LessonCompletion lc WHERE lc.student.id = :studentId AND lc.lesson.course.id = :courseId")
    Set<Long> findCompletedLessonIdsByStudentIdAndCourseId(Long studentId, Long courseId);
}