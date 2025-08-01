package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service("courseSecurityService") // Le nom du bean reste le même
public class CourseSecurityService {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Vérifie si l'utilisateur authentifié est bien l'instructeur du cours spécifié.
     * Cette méthode est optimisée pour ne requêter que l'information nécessaire.
     *
     * @param authentication L'objet d'authentification fourni par Spring Security.
     * @param courseId L'ID du cours à vérifier.
     * @return true si l'utilisateur est le propriétaire, false sinon.
     */
    public boolean isInstructorOfCourse(Authentication authentication, Long courseId) {
        // La première vérification est essentielle pour la sécurité
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // On récupère l'email de l'utilisateur actuellement connecté
        String userEmail = authentication.getName();
        
        // On demande directement à la base de données si un cours avec cet ID ET cet instructeur existe.
        // C'est plus performant que de récupérer toute l'entité Course.
        return courseRepository.existsByIdAndInstructor_Email(courseId, userEmail);
    }
}