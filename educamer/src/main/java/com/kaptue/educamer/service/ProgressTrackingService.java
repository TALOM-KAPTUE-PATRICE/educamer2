package com.kaptue.educamer.service;

import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProgressTrackingService {

    @Autowired private LessonCompletionRepository completionRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private LessonRepository lessonRepository;
    @Autowired private EnrollmentRepository enrollmentRepository; // Nécessaire pour mettre à jour la progression



     /**
     * Vérifie si un étudiant a déjà complété une leçon spécifique.
     * Cette méthode est publique pour être utilisée par d'autres services (comme QuizService).
     *
     * @param studentId L'ID de l'étudiant.
     * @param lessonId L'ID de la leçon.
     * @return true si la leçon est complétée, false sinon.
     */
    @Transactional(readOnly = true)
    public boolean isLessonCompleted(Long studentId, Long lessonId) {
        // On délègue simplement l'appel au repository qui est optimisé pour cette vérification.
        return completionRepository.existsByStudent_IdAndLesson_Id(studentId, lessonId);
    }
    
  

    /**
     * Marque une leçon comme complétée pour un étudiant et met à jour
     * la progression globale du cours pour cet étudiant.
     * Cette méthode est idempotente : l'appeler plusieurs fois pour la même leçon/étudiant
     * n'aura d'effet que la première fois.
     *
     * @param studentId L'ID de l'étudiant.
     * @param lessonId L'ID de la leçon à marquer comme complétée.
     */
    @Transactional
    public void markLessonAsCompleted(Long studentId, Long lessonId) {
        // 1. Vérifier si la complétion n'existe pas déjà pour éviter le travail inutile.
        if (completionRepository.existsByStudent_IdAndLesson_Id(studentId, lessonId)) {
            return;
        }

        // 2. Récupérer les entités nécessaires.
        // Utilisation de .orElseThrow pour une gestion d'erreur propre.
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec l'ID: " + studentId));
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Leçon non trouvée avec l'ID: " + lessonId));

        // 3. Créer et sauvegarder l'enregistrement de complétion.
        LessonCompletion completion = new LessonCompletion();
        completion.setStudent(student);
        completion.setLesson(lesson);
        completionRepository.save(completion);
        
        // 4. Mettre à jour le pourcentage de progression global du cours.
        updateCourseProgress(studentId, lesson.getCourse().getId());
    }

    /**
     * Calcule et met à jour le pourcentage de progression d'un étudiant pour un cours.
     * Cette méthode est privée car elle ne devrait être appelée qu'après une action de complétion.
     *
     * @param studentId L'ID de l'étudiant.
     * @param courseId L'ID du cours.
     */
    private void updateCourseProgress(Long studentId, Long courseId) {
        // Récupérer l'inscription de l'étudiant à ce cours.
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée pour l'étudiant " + studentId + " au cours " + courseId));

        // Compter le nombre total de leçons dans le cours.
        long totalLessonsInCourse = lessonRepository.countByCourseId(courseId);
        
        // Si le cours n'a pas de leçons, la progression est de 0%.
        if (totalLessonsInCourse == 0) {
            enrollment.setProgress(0.0);
            enrollmentRepository.save(enrollment);
            return;
        }

        // Compter combien de leçons de ce cours l'étudiant a complétées.
        Set<Long> completedLessonIds = completionRepository.findCompletedLessonIdsByStudentIdAndCourseId(studentId, courseId);
        long completedLessonsCount = completedLessonIds.size();

        // Calculer le pourcentage de progression.
        double progressPercentage = ((double) completedLessonsCount / totalLessonsInCourse) * 100.0;
        
        // Arrondir à deux décimales pour un stockage propre.
        double roundedProgress = Math.round(progressPercentage * 100.0) / 100.0;

        // Mettre à jour et sauvegarder l'inscription.
        enrollment.setProgress(roundedProgress);
        enrollmentRepository.save(enrollment);
    }
    

}