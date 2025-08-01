package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.response.CourseCatalogDTO;
import com.kaptue.educamer.dto.response.EnrolledCourseDetailDTO;
import com.kaptue.educamer.dto.response.EnrolledCourseSummaryDTO;
import com.kaptue.educamer.dto.response.LessonDetailDTO;
import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.*;
import org.springframework.security.access.AccessDeniedException;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kaptue.educamer.entity.enums.CourseStatus;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentCourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonCompletionRepository lessonCompletionRepository;

    /**
     * Récupère tous les cours publiés pour le catalogue.
     */
    @Transactional(readOnly = true)
    public List<CourseCatalogDTO> getPublishedCourses() {
        return courseRepository.findByStatus(CourseStatus.PUBLISHED).stream()
                .map(CourseCatalogDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Inscrit un élève à un cours.
     */
    @Transactional
    public void enrollStudentInCourse(Long courseId, Long studentId) {
        // Vérifier si l'étudiant est déjà inscrit
        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new IllegalStateException("L'élève est déjà inscrit à ce cours.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé avec l'ID: " + studentId));

        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new IllegalStateException("Impossible de s'inscrire à un cours non publié.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        enrollmentRepository.save(enrollment);
    }

    /**
     * Récupère les détails d'un cours auquel l'élève est inscrit.
     */
    @Transactional(readOnly = true)
    public EnrolledCourseDetailDTO getEnrolledCourseDetails(Long courseId, Long studentId) {
        // Vérifie si l'inscription existe, ce qui confirme l'accès
        enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new AccessDeniedException("Vous n'êtes pas inscrit à ce cours."));

        Course course = courseRepository.findById(courseId).orElseThrow(); // On sait qu'il existe
        return EnrolledCourseDetailDTO.fromEntity(course);
    }

    @Transactional(readOnly = true)
    public LessonDetailDTO getLessonDetails(Long courseId, Long lessonId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        if (!enrollmentRepository.existsByStudent_EmailAndCourseId(student.getEmail(), courseId)) {
            throw new AccessDeniedException("Vous n'êtes pas inscrit à ce cours.");
        }

        Lesson lessonToAccess = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Leçon non trouvée"));

        if (!lessonToAccess.getCourse().getId().equals(courseId)) {
            throw new AccessDeniedException("Cette leçon n'appartient pas à ce cours.");
        }

        // ==========================================================
        // === LOGIQUE DE PROGRESSION SÉQUENTIELLE                  ===
        // ==========================================================
        // Si ce n'est pas la première leçon (ordre > 1)
        if (lessonToAccess.getLessonOrder() > 1) {
            // Trouver la leçon précédente
            Lesson previousLesson = lessonRepository
                    .findByCourseIdAndLessonOrder(courseId, lessonToAccess.getLessonOrder() - 1)
                    .orElse(null); // Il devrait toujours y en avoir une

            // Si la leçon précédente existe et qu'elle nécessite d'être complétée
            if (previousLesson != null && previousLesson.isCompletionRequired()) {
                // Vérifier si l'élève a bien complété la leçon précédente
                boolean hasCompletedPrevious = lessonCompletionRepository
                        .existsByStudent_IdAndLesson_Id(studentId, previousLesson.getId());

                if (!hasCompletedPrevious) {
                    throw new AccessDeniedException("Vous devez compléter la leçon '" + previousLesson.getTitle() + "' avant d'accéder à celle-ci.");
                }
            }
        }
        // ==========================================================

        LessonDetailDTO dto = LessonDetailDTO.fromEntity(lessonToAccess);

        // Enrichir le DTO avec l'état de complétion de l'élève
        boolean isCompleted = lessonCompletionRepository.existsByStudent_IdAndLesson_Id(studentId, lessonId);
        dto.setCompletedByStudent(isCompleted);

        return dto;
    }

    /**
     * Récupère un résumé des cours auxquels un élève est inscrit, incluant sa
     * progression.
     *
     * @param studentId L'ID de l'élève.
     * @return Une liste de DTOs résumant les cours inscrits.
     */
    @Transactional(readOnly = true)
    public List<EnrolledCourseSummaryDTO> getEnrolledCoursesForStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        return enrollments.stream()
                .map(EnrolledCourseSummaryDTO::fromEnrollment)
                .collect(Collectors.toList());
    }

}
