package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("assignmentSecurityService") // Nom du bean utilisé dans les annotations @PreAuthorize
public class AssignmentSecurityService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    /**
     * Vérifie si l'utilisateur actuellement authentifié est bien l'instructeur
     * du cours auquel le devoir spécifié est rattaché.
     *
     * C'est une vérification de propriété cruciale pour les opérations de mise à jour et de suppression.
     *
     * @param authentication L'objet d'authentification fourni par Spring Security, contenant l'email de l'utilisateur.
     * @param assignmentId L'ID du devoir dont on veut vérifier la propriété.
     * @return true si l'utilisateur authentifié est le propriétaire du cours du devoir, false sinon.
     */
    public boolean isInstructorOfAssignment(Authentication authentication, Long assignmentId) {
        // Étape 1: Vérification de base de l'authentification
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Étape 2: Récupération de l'identifiant de l'utilisateur (son email)
        String instructorEmail = authentication.getName();

        // Étape 3: Utilisation du repository pour effectuer la vérification directement en base de données.
        // C'est la méthode la plus performante car elle évite de charger les entités complètes.
        // La requête va vérifier si un devoir existe avec l'ID donné ET si le champ 'email'
        // de l'instructeur associé au cours du devoir correspond à l'email de l'utilisateur connecté.
        return assignmentRepository.existsByIdAndCourse_Instructor_Email(assignmentId, instructorEmail);
    }
    
    
}