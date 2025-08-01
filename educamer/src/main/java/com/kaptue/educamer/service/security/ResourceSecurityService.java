package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

// ▼▼▼ LA CORRECTION EST ICI ▼▼▼
// 1. Ajouter @Service pour que Spring crée un bean
// 2. Donner explicitement le nom "resourceSecurityService" au bean pour correspondre à @PreAuthorize
@Service("resourceSecurityService") 
public class ResourceSecurityService {

    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * Vérifie si l'utilisateur authentifié est l'instructeur propriétaire de la ressource.
     * La vérification se fait en remontant de la Ressource -> Leçon -> Cours -> Instructeur.
     *
     * @param authentication L'objet d'authentification.
     * @param resourceId L'ID de la ressource à vérifier.
     * @return true si l'utilisateur est le propriétaire.
     */
    public boolean isInstructorOfResource(Authentication authentication, Long resourceId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String userEmail = authentication.getName();
        
        // On utilise la méthode du repository que nous avons déjà définie
      
        return resourceRepository.existsByIdAndLesson_Course_Instructor_Email(resourceId, userEmail);
    }
}