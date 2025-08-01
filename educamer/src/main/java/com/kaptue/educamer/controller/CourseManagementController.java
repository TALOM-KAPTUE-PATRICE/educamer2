package com.kaptue.educamer.controller;


import com.kaptue.educamer.dto.request.CourseRequest;
import com.kaptue.educamer.dto.request.LessonOrderRequest;
import com.kaptue.educamer.dto.request.LessonRequest;
import com.kaptue.educamer.dto.request.AssignmentRequest;
import com.kaptue.educamer.dto.response.AssignmentResponse;
import com.kaptue.educamer.dto.response.CourseResponse;
import com.kaptue.educamer.dto.response.CourseStatisticDTO;
import com.kaptue.educamer.dto.response.LessonResponse;
import com.kaptue.educamer.dto.response.editor.CourseEditorDTO;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.AssignmentService;
import com.kaptue.educamer.service.CourseService;
import com.kaptue.educamer.service.LessonService;
import com.kaptue.educamer.security.CurrentUser; // Une annotation custom pour la simplicité

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/instructor/courses") // Toutes les routes ici sont pour les instructeurs
@PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')") // Sécurité au niveau de la classe
public class CourseManagementController {

    @Autowired
    private CourseService courseService;
    @Autowired private AssignmentService assignmentService;
    @Autowired
    private LessonService lessonService;
    // ... injecter d'autres services (Assignment, Quiz, etc.)

    /**
     * Endpoint pour créer un nouveau cours. L'ID de l'instructeur est récupéré
     * depuis l'utilisateur authentifié.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('" + Permission.COURSE_CREATE + "')")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request, @CurrentUser User currentUser) {
        CourseResponse createdCourse = courseService.createCourse(request, currentUser.getId());
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    // Ajout d'une méthode PUT pour la mise à jour des détails du cours
    @PutMapping("/{courseId}")
    // L'utilisateur doit avoir la permission de mettre à jour ET être le propriétaire du cours.
    @PreAuthorize("hasAuthority('" + Permission.COURSE_UPDATE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long courseId, @Valid @RequestBody CourseRequest request) {
        // Logique à implémenter dans CourseService
        return ResponseEntity.ok(courseService.updateCourseDetails(courseId, request));
    }

    /**
     * Endpoint pour uploader/mettre à jour l'image d'un cours. On vérifie que
     * l'instructeur authentifié est bien le propriétaire du cours.
     */
    @PostMapping("/{courseId}/image")
    @PreAuthorize("hasAuthority('" + Permission.COURSE_UPDATE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<CourseResponse> uploadCourseImage(@PathVariable Long courseId, @RequestParam("file") MultipartFile file) {
        CourseResponse updatedCourse = courseService.updateCourseImage(courseId, file);
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Endpoint pour ajouter une leçon à un cours.
     */
    @PostMapping("/{courseId}/lessons")
    @PreAuthorize("hasAuthority('" + Permission.CONTENT_MANAGE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<LessonResponse> addLesson(@PathVariable Long courseId, @Valid @RequestBody LessonRequest request) {
        LessonResponse newLesson = lessonService.addLessonToCourse(courseId, request);
        return new ResponseEntity<>(newLesson, HttpStatus.CREATED);
    }

    // Endpoint pour consulter les statistiques
    @GetMapping("/{courseId}/statistics")
    @PreAuthorize("hasAuthority('" + Permission.TRACKING_READ_COURSE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<CourseStatisticDTO> getStatistics(@PathVariable Long courseId) {
        CourseStatisticDTO stats = courseService.getCourseStatistics(courseId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Récupère la liste de tous les cours appartenant à l'instructeur authentifié.
    */
    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseResponse>> getMyCourses(@CurrentUser User currentUser) {
        List<CourseResponse> courses = courseService.getCoursesByInstructor(currentUser.getId());
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{courseId}/publish")
    @PreAuthorize("hasAuthority('" + Permission.COURSE_PUBLISH + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<CourseResponse> publishCourse(@PathVariable Long courseId) {
        CourseResponse publishedCourse = courseService.publishCourse(courseId);
        return ResponseEntity.ok(publishedCourse);
    }


    @GetMapping("/{courseId}/editor-details")
    @PreAuthorize("@courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<CourseEditorDTO> getCourseForEditing(@PathVariable Long courseId) {
        CourseEditorDTO courseDetails = courseService.getCourseForEditor(courseId);
        return ResponseEntity.ok(courseDetails);
    }

    @PutMapping("/{courseId}/lessons/order")
    @PreAuthorize("hasAuthority('" + Permission.CONTENT_MANAGE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<Void> updateLessonOrder(@PathVariable Long courseId, @Valid @RequestBody LessonOrderRequest request) {
        lessonService.updateLessonOrder(courseId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/assignments")
    @PreAuthorize("hasAuthority('" + Permission.CONTENT_MANAGE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<AssignmentResponse> createAssignmentForCourse(
            @PathVariable Long courseId,
            @Valid @RequestPart("data") AssignmentRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        AssignmentResponse newAssignment = assignmentService.createAssignment(courseId, request, file);
        return new ResponseEntity<>(newAssignment, HttpStatus.CREATED);
    }

    @PutMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasAuthority('" + Permission.CONTENT_MANAGE + "') and @assignmentSecurityService.isInstructorOfAssignment(authentication, #assignmentId)")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse updatedAssignment = assignmentService.updateAssignment(assignmentId, request);
        return ResponseEntity.ok(updatedAssignment);
    }

    @DeleteMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasAuthority('" + Permission.CONTENT_MANAGE + "') and @assignmentSecurityService.isInstructorOfAssignment(authentication, #assignmentId)")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAuthority('" + Permission.COURSE_DELETE + "') and @courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build(); // Statut 204
    }

    /**
 * Récupère les détails de base d'un cours spécifique.
 * Utile pour afficher des en-têtes ou des informations sommaires.
 */
@GetMapping("/{courseId}")
@PreAuthorize("@courseSecurityService.isInstructorOfCourse(authentication, #courseId)")
public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long courseId) {
    CourseResponse course = courseService.findCourseById(courseId);
    return ResponseEntity.ok(course);
}
    

   
}
