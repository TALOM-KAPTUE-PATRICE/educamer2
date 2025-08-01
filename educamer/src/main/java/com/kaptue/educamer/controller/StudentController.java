package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.HelpRequestCreateDTO;
import com.kaptue.educamer.dto.response.*;
import com.kaptue.educamer.entity.User;

import com.kaptue.educamer.security.CurrentUser;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.HelpRequestService;
import com.kaptue.educamer.service.StudentCourseService;
import com.kaptue.educamer.service.StudentService;
import com.kaptue.educamer.service.StudentTrackingService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student") // Toutes les routes ici sont pour les élèves
@PreAuthorize("hasRole('STUDENT')") // Sécurité au niveau de la classe
public class StudentController {

    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired private StudentService studentService;
    @Autowired
    private StudentTrackingService studentTrackingService;
     @Autowired private HelpRequestService helpRequestService; // INJECTER

    // --- Fonctionnalité : Consulter le catalogue ---
    @GetMapping("/courses/catalog")
    @PreAuthorize("hasAuthority('" + Permission.COURSE_READ_CATALOG + "')")
    public ResponseEntity<List<CourseCatalogDTO>> getCourseCatalog() {
        return ResponseEntity.ok(studentCourseService.getPublishedCourses());
    }

    // --- Fonctionnalité : S'inscrire à un cours ---
    @PostMapping("/courses/{courseId}/enroll")
    @PreAuthorize("hasAuthority('" + Permission.ENROLLMENT_CREATE + "')")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, @CurrentUser User currentUser) {
        studentCourseService.enrollStudentInCourse(courseId, currentUser.getId());
        return ResponseEntity.ok("Inscription au cours réussie !");
    }

    // --- Fonctionnalité : Suivre un cours (voir les détails) ---
    @GetMapping("/courses/{courseId}/details")
    @PreAuthorize("hasAuthority('" + Permission.COURSE_READ_ENROLLED + "') and @studentCourseSecurityService.isEnrolled(authentication, #courseId)")
    public ResponseEntity<EnrolledCourseDetailDTO> getMyCourseDetails(@PathVariable Long courseId, @CurrentUser User currentUser) {
        // La vérification de l'inscription est faite dans le service
        EnrolledCourseDetailDTO courseDetails = studentCourseService.getEnrolledCourseDetails(courseId, currentUser.getId());
        return ResponseEntity.ok(courseDetails);
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<EnrolledCourseSummaryDTO>> getMyCourses(@CurrentUser User currentUser) {
        List<EnrolledCourseSummaryDTO> courses = studentCourseService.getEnrolledCoursesForStudent(currentUser.getId());
        return ResponseEntity.ok(courses);
    }

    // NOUVEAU: Obtenir le contenu détaillé d'une leçon
    @GetMapping("/courses/{courseId}/lessons/{lessonId}")
    @PreAuthorize("hasAuthority('" + Permission.COURSE_READ_ENROLLED + "')")
    public ResponseEntity<LessonDetailDTO> getMyLessonDetails(
            @PathVariable Long courseId, @PathVariable Long lessonId, @CurrentUser User currentUser) {
        LessonDetailDTO lesson = studentCourseService.getLessonDetails(courseId, lessonId, currentUser.getId());
        return ResponseEntity.ok(lesson);
    }

   // --- Fonctionnalité : Consulter sa progression pour un cours ---
    @GetMapping("/tracking/course/{courseId}/my-progress")
    @PreAuthorize("hasAuthority('" + Permission.TRACKING_READ_OWN + "') and @studentCourseSecurityService.isEnrolled(authentication, #courseId)")
    public ResponseEntity<StudentProgressDTO> getMyProgressForCourse(@PathVariable Long courseId, @CurrentUser User currentUser) {
        // ▼▼▼ CORRECTION : On appelle la méthode optimisée ▼▼▼
        StudentProgressDTO myProgress = studentTrackingService.getStudentProgressForCourse(currentUser.getId(), courseId);
        return ResponseEntity.ok(myProgress);
    }

    // --- Fonctionnalité : Consulter ses notes pour un cours ---
    @GetMapping("/tracking/course/{courseId}/my-grades")
    @PreAuthorize("hasAuthority('" + Permission.TRACKING_READ_OWN + "') and @studentCourseSecurityService.isEnrolled(authentication, #courseId)")
    public ResponseEntity<StudentGradebookDTO> getMyGradesForCourse(@PathVariable Long courseId, @CurrentUser User currentUser) {
        // ▼▼▼ CORRECTION : On appelle la méthode optimisée ▼▼▼
        StudentGradebookDTO myGradebook = studentTrackingService.getStudentGradebookForCourse(currentUser.getId(), courseId);
        return ResponseEntity.ok(myGradebook);
    }

    // --- Les autres fonctionnalités (soumission, forum, feedback) sont dans leurs propres contrôleurs ---
    // Par exemple, le POST pour soumettre un devoir est déjà dans SubmissionController.
    // Il faudra créer un ForumController et un FeedbackController.
    // --- Fonctionnalité : Demande d'assistance (Exemple simple) ---
    @PostMapping("/help/request")
    @PreAuthorize("hasAuthority('" + Permission.HELP_REQUEST_CREATE + "')")
    public ResponseEntity<String> requestAssistance(@RequestBody String message, @CurrentUser User currentUser) {
        // Ici, vous implémenteriez la logique pour créer une "demande d'aide" et la lier à un tuteur.
        // Par exemple, créer une entité "HelpRequest" et la sauvegarder.
        System.out.println("Demande d'aide de " + currentUser.getName() + ": " + message);
        return ResponseEntity.ok("Votre demande d'assistance a été envoyée.");
    }

        // --- NOUVEL ENDPOINT : Demande d'assistance ---
    @PostMapping("/help-requests")
    @PreAuthorize("hasAuthority('" + Permission.HELP_REQUEST_CREATE + "')")
    public ResponseEntity<HelpRequestDTO> createHelpRequest(@CurrentUser User currentUser, @Valid @RequestBody HelpRequestCreateDTO request) {
        HelpRequestDTO newRequest = helpRequestService.createHelpRequest(currentUser.getId(), request);
        return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }
       

    // --- NOUVEL ENDPOINT ---
    @PostMapping("/generate-linking-code")
    public ResponseEntity<LinkingCodeResponse> generateLinkingCode(@CurrentUser User currentUser) {
        LinkingCodeResponse response = studentService.generateLinkingCode(currentUser.getId());
        return ResponseEntity.ok(response);
    }

}
