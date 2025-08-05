package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.response.StudentGradebookDTO;
import com.kaptue.educamer.dto.response.StudentProgressDTO;
import com.kaptue.educamer.dto.response.GradebookEntryDTO;
import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.*;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentTrackingService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private StudentRepository studentRepository;

    /**
     * Récupère la liste de tous les élèves inscrits à un cours avec leur
     * progression.
     *
     * @param courseId L'ID du cours.
     * @return Une liste de DTOs de progression.
     */
    @Transactional(readOnly = true)
    public List<StudentProgressDTO> getCourseProgressForAllStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));

        int totalLessons = course.getLessons().size();
        if (totalLessons == 0) { // Évite la division par zéro
            return List.of();
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        return enrollments.stream().map(enrollment -> {
            StudentProgressDTO dto = StudentProgressDTO.fromEnrollment(enrollment);
            // Logique de calcul simple, vous pouvez la complexifier
            // Ici, on suppose que 'progress' est déjà calculé et stocké.
            // On peut aussi le calculer à la volée.
            int completedLessons = (int) Math.round(enrollment.getProgress() / 100.0 * totalLessons);
            dto.setCompletedLessons(completedLessons);
            dto.setTotalLessons(totalLessons);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Récupère le carnet de notes complet pour tous les élèves d'un cours.
     *
     * @param courseId L'ID du cours.
     * @return Une liste de carnets de notes, un par élève.
     */
    @Transactional(readOnly = true)
    public List<StudentGradebookDTO> getGradebookForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));

        // Récupérer tous les élèves inscrits
        List<Student> students = course.getEnrollments().stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toList());

        return students.stream().map(student -> {
            StudentGradebookDTO gradebookDTO = StudentGradebookDTO.fromStudent(student);

            // Récupérer toutes les soumissions de cet élève pour les devoirs de ce cours
            List<Submission> submissions = submissionRepository.findByStudentIdAndAssignment_CourseId(student.getId(), courseId);

            List<GradebookEntryDTO> gradeEntries = submissions.stream()
                    .map(GradebookEntryDTO::fromSubmission)
                    .collect(Collectors.toList());

            gradebookDTO.setGrades(gradeEntries);

            // Calculer la moyenne
            double average = gradeEntries.stream()
                    .filter(entry -> entry.getGrade() != null)
                    .mapToDouble(GradebookEntryDTO::getGrade)
                    .average()
                    .orElse(0.0); // Moyenne de 0 si aucune note

            gradebookDTO.setAverageGrade(Math.round(average * 100.0) / 100.0); // Arrondi à 2 décimales

            return gradebookDTO;
        }).collect(Collectors.toList());
    }

    /**
     * Récupère la progression d'UN SEUL élève pour un cours. (Utilisé par le
     * Parent et l'Élève lui-même)
     */
    @Transactional(readOnly = true)
    public StudentProgressDTO getStudentProgressForCourse(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée"));

        int totalLessons = enrollment.getCourse().getLessons().size();
        StudentProgressDTO dto = StudentProgressDTO.fromEnrollment(enrollment);

        if (totalLessons > 0) {
            int completedLessons = (int) Math.round(enrollment.getProgress() / 100.0 * totalLessons);
            dto.setCompletedLessons(completedLessons);
        } else {
            dto.setCompletedLessons(0);
        }

        dto.setTotalLessons(totalLessons);
        return dto;
    }

    /**
     * Récupère le carnet de notes d'UN SEUL élève pour un cours. (Utilisé par
     * le Parent et l'Élève lui-même)
     */
    @Transactional(readOnly = true)
    public StudentGradebookDTO getStudentGradebookForCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé avec l'ID: " + studentId));

        List<Submission> submissions = submissionRepository.findByStudentIdAndAssignment_CourseId(studentId, courseId);

        List<GradebookEntryDTO> gradeEntries = submissions.stream()
                .map(GradebookEntryDTO::fromSubmission)
                .collect(Collectors.toList());

        double average = gradeEntries.stream()
                .filter(entry -> entry.getGrade() != null)
                .mapToDouble(GradebookEntryDTO::getGrade)
                .average()
                .orElse(0.0);

        StudentGradebookDTO gradebookDTO = new StudentGradebookDTO();
        gradebookDTO.setStudentId(student.getId());
        gradebookDTO.setStudentName(student.getName());
        gradebookDTO.setGrades(gradeEntries);
        gradebookDTO.setAverageGrade(Math.round(average * 100.0) / 100.0);

        return gradebookDTO;
    }

}
