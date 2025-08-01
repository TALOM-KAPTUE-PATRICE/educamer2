package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.LessonRepository;
import com.kaptue.educamer.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("lessonSecurityService")
public class LessonSecurityService {

    @Autowired private LessonRepository lessonRepository;
    @Autowired private ResourceRepository resourceRepository;

    /**
     * Vérifie si l'utilisateur authentifié est l'instructeur de la leçon spécifiée.
     *
     * @param authentication L'objet d'authentification.
     * @param lessonId L'ID de la leçon.
     * @return true si l'utilisateur est le propriétaire.
     */
    public boolean isInstructorOfLesson(Authentication authentication, Long lessonId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String userEmail = authentication.getName();
        // On vérifie que la leçon avec cet ID a un instructeur avec cet email.
        return lessonRepository.existsByIdAndCourse_Instructor_Email(lessonId, userEmail);
    }
    
    /**
     * Vérifie si l'utilisateur authentifié est l'instructeur de la ressource spécifiée.
     *
     * @param authentication L'objet d'authentification.
     * @param resourceId L'ID de la ressource.
     * @return true si l'utilisateur est le propriétaire.
     */
     public boolean isInstructorOfResource(Authentication authentication, Long resourceId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String userEmail = authentication.getName();
        return resourceRepository.existsByIdAndLesson_Course_Instructor_Email(resourceId, userEmail);
     }
}