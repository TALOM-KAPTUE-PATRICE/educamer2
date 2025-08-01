package com.kaptue.educamer.controller;
// ... imports ...

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaptue.educamer.dto.request.GradeRequest;
import com.kaptue.educamer.dto.request.SubmissionRequest;
import com.kaptue.educamer.dto.response.SubmissionResponse;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.repository.UserRepository;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.SubmissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private UserRepository userRepository;

    // Endpoint pour qu'un élève soumette un devoir
    @PostMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAuthority('" + Permission.SUBMISSION_CREATE + "')")
    public ResponseEntity<SubmissionResponse> submitAssignment(
            @PathVariable Long assignmentId,
            @ModelAttribute SubmissionRequest request, // Utiliser @ModelAttribute pour les fichiers
            Authentication authentication) {

        // Récupérer l'ID de l'étudiant depuis le token d'authentification
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        SubmissionResponse response = submissionService.createSubmission(assignmentId, currentUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint pour qu'un instructeur note une soumission
    @PostMapping("/{submissionId}/grade")
    @PreAuthorize("hasAuthority('" + Permission.ASSIGNMENT_GRADE + "')")
    public ResponseEntity<SubmissionResponse> gradeSubmission(
            @PathVariable Long submissionId,
            @Valid @RequestBody GradeRequest gradeRequest) {

        // On pourrait ajouter une vérification pour s'assurer que l'instructeur
        // qui note est bien l'instructeur du cours concerné.
        SubmissionResponse response = submissionService.gradeSubmission(submissionId, gradeRequest);
        return ResponseEntity.ok(response);
    }
}
