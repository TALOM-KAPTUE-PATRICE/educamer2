package com.kaptue.educamer.service;

import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.security.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class PermissionMappingService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionMappingService.class);

    /**
     * Détermine l'ensemble des permissions pour un utilisateur donné en
     * fonction de son type (rôle).
     *
     * @param user L'utilisateur dont on veut les permissions.
     * @return Un Set de chaînes représentant les permissions.
     */
    public Set<String> getPermissionsForUser(User user) {
        if (user == null) {
            return Collections.emptySet();
        }

        // 1. On commence TOUJOURS avec les permissions de base.
        Set<String> permissions = getBasePermissions();

        // 2. On AJOUTE les permissions spécifiques au rôle.
        if (user instanceof Admin) {
            permissions.addAll(getAdminPermissions());
        } else if (user instanceof Instructor) {
            permissions.addAll(getInstructorPermissions());
        } else if (user instanceof Student) {
            permissions.addAll(getStudentPermissions());
        } else if (user instanceof Tutor) {
            permissions.addAll(getTutorPermissions());
        } else if (user instanceof Parent) {
            permissions.addAll(getParentPermissions());
        } else {
            logger.warn("Utilisateur {} avec un type non reconnu. Seules les permissions de base sont attribuées.", user.getEmail());
        }

        // 3. On retourne le Set complet.
        return permissions;
    }

    /**
     * Permissions communes à tous les utilisateurs authentifiés.
     */
    private Set<String> getBasePermissions() {
        return new HashSet<>(Set.of(
                Permission.PROFILE_READ,
                Permission.PROFILE_UPDATE
        ));
    }

    /**
     * L'Administrateur a tous les droits. C'est le super-utilisateur.
     */
    private Set<String> getAdminPermissions() {
        Set<String> permissions = new HashSet<>();
        permissions.add(Permission.USER_LIST);
        permissions.add(Permission.USER_MANAGE);
        // L'admin hérite de toutes les permissions de l'instructeur et du tuteur pour une gestion complète.
        permissions.addAll(getInstructorPermissions());
        permissions.addAll(getTutorPermissions());
        return permissions;
    }

    /**
     * L'Instructeur gère le contenu pédagogique et le suivi de ses cours.
     */
    private Set<String> getInstructorPermissions() {
        return Set.of(
                // Gestion de ses propres cours
                Permission.COURSE_CREATE,
                Permission.COURSE_UPDATE,
                Permission.COURSE_DELETE,
                Permission.COURSE_PUBLISH,
                Permission.CONTENT_MANAGE, // Gérer leçons, devoirs, quiz, etc.

                // Suivi des élèves de ses cours
                Permission.TRACKING_READ_COURSE,
                Permission.SUBMISSION_READ_ALL,
                Permission.SUBMISSION_GRADE,
                // Interaction dans ses cours
                Permission.FORUM_READ,
                Permission.FORUM_POST,
                Permission.FORUM_MODERATE
        );
    }

    /**
     * L'Élève interagit avec les cours auxquels il est inscrit.
     */
    private Set<String> getStudentPermissions() {
        return Set.of(
                // Découverte et inscription
                Permission.COURSE_READ_CATALOG,
                Permission.ENROLLMENT_CREATE,
                // Participation
                Permission.COURSE_READ_ENROLLED,
                Permission.SUBMISSION_CREATE,
                Permission.SUBMISSION_READ_OWN,
                Permission.HELP_REQUEST_CREATE,
                Permission.FEEDBACK_CREATE,
                Permission.FORUM_READ,
                Permission.FORUM_POST,
                // Suivi personnel
                Permission.TRACKING_READ_OWN
        );
    }

    /**
     * Le Parent a des droits de lecture sur les données de ses enfants.
     */
    private Set<String> getParentPermissions() {
        return Set.of(
                // Découverte de cours pour ses enfants
                Permission.COURSE_READ_CATALOG,
                // Suivi de ses enfants
                Permission.TRACKING_READ_CHILD
        );
    }

    /**
     * Le Tuteur fournit de l'aide et a des droits de lecture pour le contexte.
     */
    private Set<String> getTutorPermissions() {
        return Set.of(
                // Gestion des demandes d'aide
                Permission.HELP_REQUEST_READ_ALL,
                Permission.HELP_REQUEST_ASSIGN,
                // Droits de lecture pour aider efficacement
                Permission.COURSE_READ_ENROLLED, // Pour voir le contenu du cours de l'élève
                Permission.SUBMISSION_READ_ALL // Pour voir les devoirs de l'élève
        );
    }
}
