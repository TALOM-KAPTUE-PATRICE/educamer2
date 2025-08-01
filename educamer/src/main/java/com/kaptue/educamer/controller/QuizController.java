package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.QuizAttemptRequest;
import com.kaptue.educamer.dto.response.QuizAttemptResultDTO;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.security.CurrentUser;
import com.kaptue.educamer.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
@PreAuthorize("hasRole('STUDENT')")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/{quizId}/attempt")
    // On vérifie qu'il a le droit de soumettre (implicite pour un élève) ET qu'il peut accéder à ce quiz.
    // La vérification de l'inscription au cours du quiz est parfaite.
    @PreAuthorize("hasRole('ELEVE') and @quizSecurityService.canAttemptQuiz(authentication, #quizId)")
    public ResponseEntity<QuizAttemptResultDTO> submitQuiz(
            @PathVariable Long quizId,
            @CurrentUser User currentUser,
            @RequestBody QuizAttemptRequest request) {

        QuizAttemptResultDTO result = quizService.submitQuizAttempt(quizId, currentUser.getId(), request);
        return ResponseEntity.ok(result);
    }
    
}
