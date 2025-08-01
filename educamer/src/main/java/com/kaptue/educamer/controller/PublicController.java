package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.ApplicationRequest;
import com.kaptue.educamer.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/instructor-applications")
    public ResponseEntity<String> submitApplication(@Valid @ModelAttribute ApplicationRequest request) {
        applicationService.submitInstructorApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Votre candidature a été envoyée avec succès. Nous vous contacterons bientôt.");
    }

    
}