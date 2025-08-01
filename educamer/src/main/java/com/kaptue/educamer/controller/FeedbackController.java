package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.CourseFeedbackRequest;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.security.CurrentUser;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.CourseFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
@PreAuthorize("hasRole('STUDENT')") // Seuls les élèves peuvent donner un feedback
public class FeedbackController {

    @Autowired
    private CourseFeedbackService feedbackService;

    @PostMapping("/course/{courseId}")
    // On vérifie que l'utilisateur a le droit de donner un feedback ET qu'il est bien inscrit au cours.
    @PreAuthorize("hasAuthority('" + Permission.FEEDBACK_CREATE + "') and @studentCourseSecurityService.isEnrolled(authentication, #courseId)")
    public ResponseEntity<String> submitCourseFeedback(@PathVariable Long courseId, @CurrentUser User currentUser, @Valid @RequestBody CourseFeedbackRequest request) {
        feedbackService.submitFeedback(courseId, currentUser.getId(), request);
        return ResponseEntity.ok("Merci pour votre retour !");
    }
}
