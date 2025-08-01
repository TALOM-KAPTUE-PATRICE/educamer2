package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.LinkStudentRequest;
import com.kaptue.educamer.dto.response.*;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.security.CurrentUser;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.ParentService;
import com.kaptue.educamer.service.StudentCourseService;
import com.kaptue.educamer.service.StudentTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/parent") // Route de base pour les parents
@PreAuthorize("hasRole('PARENT')") // Sécurité au niveau de la classe
public class ParentController {

    @Autowired
    private ParentService parentService;

    @Autowired
    private StudentCourseService studentCourseService; // Réutilisation

    @Autowired
    private StudentTrackingService studentTrackingService; // Réutilisation

    // --- Fonctionnalité Clé : Lier un compte enfant ---
    @PostMapping("/link-student")
    public ResponseEntity<LinkedStudentDTO> linkStudent(@CurrentUser User currentUser, @Valid @RequestBody LinkStudentRequest request) {
        LinkedStudentDTO linkedStudent = parentService.linkStudentToParent(currentUser.getId(), request);
        return ResponseEntity.ok(linkedStudent);
    }

    // --- Fonctionnalité : Voir les enfants liés ---
    @GetMapping("/my-children")
    public ResponseEntity<List<LinkedStudentDTO>> getMyChildren(@CurrentUser User currentUser) {
        List<LinkedStudentDTO> children = parentService.getLinkedStudents(currentUser.getId());
        return ResponseEntity.ok(children);
    }

    // --- Fonctionnalité : Consulter le catalogue de cours (réutilisation) ---
    // Le parent voit le même catalogue que l'élève.
    @GetMapping("/courses/catalog")
    @PreAuthorize("hasAuthority('" + Permission.COURSE_READ_CATALOG + "')")
    public ResponseEntity<List<CourseCatalogDTO>> getCourseCatalog() {
        return ResponseEntity.ok(studentCourseService.getPublishedCourses());
    }

    @GetMapping("/children/{studentId}/tracking/course/{courseId}/progress")
    @PreAuthorize("@parentSecurityService.isParentOfStudent(authentication, #studentId)")
    public ResponseEntity<StudentProgressDTO> getChildProgressForCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        // On appelle la nouvelle méthode de service plus efficace
        StudentProgressDTO childProgress = studentTrackingService.getStudentProgressForCourse(studentId, courseId);
        return ResponseEntity.ok(childProgress);
    }

    @GetMapping("/children/{studentId}/tracking/course/{courseId}/gradebook")
    @PreAuthorize("@parentSecurityService.isParentOfStudent(authentication, #studentId)")
    public ResponseEntity<StudentGradebookDTO> getChildGradesForCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        // On appelle la nouvelle méthode de service plus efficace
        StudentGradebookDTO childGradebook = studentTrackingService.getStudentGradebookForCourse(studentId, courseId);
        return ResponseEntity.ok(childGradebook);
    }


        /**
     * Récupère la liste des cours auxquels un enfant spécifique est inscrit.
     * La sécurité vérifie que l'utilisateur authentifié est bien le parent de cet enfant.
     */
    @GetMapping("/children/{studentId}/courses")
    @PreAuthorize("@parentSecurityService.isParentOfStudent(authentication, #studentId)")
    public ResponseEntity<List<EnrolledCourseSummaryDTO>> getEnrolledCoursesForChild(@PathVariable Long studentId) {
        List<EnrolledCourseSummaryDTO> courses = studentCourseService.getEnrolledCoursesForStudent(studentId);
        return ResponseEntity.ok(courses);
    }

}
