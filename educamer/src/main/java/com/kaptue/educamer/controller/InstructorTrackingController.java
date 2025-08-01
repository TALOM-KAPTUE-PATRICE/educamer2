package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.response.CourseStatisticDTO;
import com.kaptue.educamer.dto.response.StudentGradebookDTO;
import com.kaptue.educamer.dto.response.StudentProgressDTO;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.CourseService;
import com.kaptue.educamer.service.StudentTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructor/tracking") // Route de base pour le suivi
@PreAuthorize("hasRole('INSTRUCTEUR') or hasRole('ADMINISTRATEUR')")
public class InstructorTrackingController {

    @Autowired private CourseService courseService;
    @Autowired private StudentTrackingService studentTrackingService;

    /**
     * USE CASE: Consulter les statistiques d'un cours.
     * Fournit une vue d'ensemble agrégée de la performance d'un cours.
     */
    @GetMapping("/course/{courseId}/statistics")
    @PreAuthorize("hasAuthority('" + Permission.TRACKING_READ_COURSE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<CourseStatisticDTO> getCourseStatistics(@PathVariable Long courseId) {
        CourseStatisticDTO stats = courseService.getCourseStatistics(courseId);
        return ResponseEntity.ok(stats);
    }

    /**
     * USE CASE: Consulter la progression.
     * Fournit une liste de tous les élèves inscrits à un cours avec leur pourcentage de progression.
     */
    @GetMapping("/course/{courseId}/progress")
    @PreAuthorize("hasAuthority('" + Permission.TRACKING_READ_COURSE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<List<StudentProgressDTO>> getStudentsProgress(@PathVariable Long courseId) {
        List<StudentProgressDTO> progressList = studentTrackingService.getCourseProgressForAllStudents(courseId);
        return ResponseEntity.ok(progressList);
    }

    /**
     * USE CASE: Consulter les notes.
     * Fournit le carnet de notes complet (tous les élèves, tous les devoirs) pour un cours donné.
     */
    @GetMapping("/course/{courseId}/gradebook")
    @PreAuthorize("hasAuthority('" + Permission.TRACKING_READ_COURSE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<List<StudentGradebookDTO>> getCourseGradebook(@PathVariable Long courseId) {
        List<StudentGradebookDTO> gradebook = studentTrackingService.getGradebookForCourse(courseId);
        return ResponseEntity.ok(gradebook);
    }
}