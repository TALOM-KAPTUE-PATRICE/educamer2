package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("parentSecurityService")
public class ParentSecurityService {

    @Autowired
    private ParentRepository parentRepository;
    
    /**
     * Vérifie si l'utilisateur authentifié (un parent) est bien le parent de l'élève spécifié.
     *
     * @param authentication L'objet d'authentification.
     * @param studentId L'ID de l'élève à vérifier.
     * @return true si le lien parent-enfant existe, false sinon.
     */
    public boolean isParentOfStudent(Authentication authentication, Long studentId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String parentEmail = authentication.getName();

        // On vérifie si un parent avec cet email existe et si, parmi ses enfants,
        // l'un d'eux a l'ID recherché.
        return parentRepository.existsByEmailAndChildren_Id(parentEmail, studentId);
    }
}