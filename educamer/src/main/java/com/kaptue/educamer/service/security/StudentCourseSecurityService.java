package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("studentCourseSecurityService")
public class StudentCourseSecurityService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * Vérifie si l'utilisateur authentifié (un élève) est bien inscrit au cours spécifié.
     *
     * @param authentication L'objet d'authentification.
     * @param courseId L'ID du cours à vérifier.
     * @return true si l'inscription existe, false sinon.
     */
    public boolean isEnrolled(Authentication authentication, Long courseId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String studentEmail = authentication.getName();
        
        // On vérifie directement l'existence de l'inscription
        return enrollmentRepository.existsByStudent_EmailAndCourseId(studentEmail, courseId);
    }
}