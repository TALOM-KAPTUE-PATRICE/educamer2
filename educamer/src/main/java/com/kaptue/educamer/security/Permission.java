package com.kaptue.educamer.security;

/**
 * Centralise toutes les chaînes de permissions de l'application.
 * Utiliser des constantes évite les fautes de frappe dans les annotations @PreAuthorize.
 */
public final class Permission {

    private Permission() {} // Empêche l'instanciation

    // --- Permissions Générales ---
    public static final String PROFILE_READ = "profile:read";
    public static final String PROFILE_UPDATE = "profile:update";

    // --- Permissions sur les Utilisateurs (Admin) ---
    public static final String USER_LIST = "user:list";
    public static final String USER_MANAGE = "user:manage"; // Créer, modifier, supprimer

    // --- Permissions sur les Cours (Course) ---
    public static final String COURSE_READ_CATALOG = "course:read_catalog"; // Pour tous (élèves, parents)
    public static final String COURSE_READ_ENROLLED = "course:read_enrolled"; // Pour l'élève inscrit
    public static final String COURSE_CREATE = "course:create"; // Pour l'instructeur
    public static final String COURSE_UPDATE = "course:update"; // Pour l'instructeur propriétaire
    public static final String COURSE_DELETE = "course:delete"; // Pour l'instructeur propriétaire
    public static final String COURSE_PUBLISH = "course:publish"; // Pour l'instructeur propriétaire

    // --- Permissions sur les Inscriptions (Enrollment) ---
    public static final String ENROLLMENT_CREATE = "enrollment:create"; // Pour l'élève

    // --- Permissions sur les Leçons, Devoirs, Quiz, Ressources (Contenu de cours) ---
    public static final String CONTENT_MANAGE = "content:manage"; // Regroupe création/update/delete pour instructeur propriétaire

    // --- Permissions sur les Soumissions (Submission) ---
    public static final String SUBMISSION_CREATE = "submission:create"; // Pour l'élève
    public static final String SUBMISSION_READ_OWN = "submission:read_own"; // L'élève lit sa propre soumission
    public static final String SUBMISSION_READ_ALL = "submission:read_all"; // L'instructeur lit toutes les soumissions d'un devoir
    public static final String SUBMISSION_GRADE = "submission:grade";   // L'instructeur note une soumission

    // --- Permissions sur le Tutorat (HelpRequest) ---
    public static final String HELP_REQUEST_CREATE = "help_request:create"; // Pour l'élève
    public static final String HELP_REQUEST_READ_ALL = "help_request:read_all"; // Pour les tuteurs
    public static final String HELP_REQUEST_ASSIGN = "help_request:assign"; // Pour les tuteurs

    // --- Permissions sur le Suivi (Tracking) ---
    public static final String TRACKING_READ_OWN = "tracking:read_own"; // L'élève consulte sa progression/notes
    public static final String TRACKING_READ_CHILD = "tracking:read_child"; // Le parent consulte celles de son enfant
    public static final String TRACKING_READ_COURSE = "tracking:read_course"; // L'instructeur consulte celles de son cours

    // --- Permissions sur le Forum ---
    public static final String FORUM_READ = "forum:read"; // Pour les participants du cours
    public static final String FORUM_POST = "forum:post"; // Pour les participants du cours
    public static final String FORUM_MODERATE = "forum:moderate"; // Pour l'instructeur et l'admin

    // --- Permissions sur le Feedback ---
    public static final String FEEDBACK_CREATE = "feedback:create"; // Pour un élève inscrit et ayant progressé
    // Permissions sur les Devoirs (Assignment)
    public static final String ASSIGNMENT_CREATE = "assignment:create"; // Pour l'instructeur
    public static final String ASSIGNMENT_GRADE = "assignment:grade";   // Pour l'instructeur

  
 

    
}