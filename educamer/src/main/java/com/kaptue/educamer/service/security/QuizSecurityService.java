package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.EnrollmentRepository;
import com.kaptue.educamer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("quizSecurityService")
public class QuizSecurityService {
    
    @Autowired private QuizRepository quizRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    /**
     * Vérifie si un élève peut passer un quiz.
     * Condition: il doit être inscrit au cours qui contient le quiz.
     */
    public boolean canAttemptQuiz(Authentication authentication, Long quizId) {
        Long courseId = quizRepository.findCourseIdByQuizId(quizId);
        if (courseId == null) return false;
        
        return enrollmentRepository.existsByStudent_EmailAndCourseId(authentication.getName(), courseId);
    }
}