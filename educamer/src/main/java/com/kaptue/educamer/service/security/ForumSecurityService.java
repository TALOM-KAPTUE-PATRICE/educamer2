package com.kaptue.educamer.service.security;

import com.kaptue.educamer.repository.EnrollmentRepository;
import com.kaptue.educamer.repository.ForumPostRepository;
import com.kaptue.educamer.repository.ForumThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("forumSecurityService")
public class ForumSecurityService {

    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private ForumThreadRepository forumThreadRepository;
    @Autowired private ForumPostRepository forumPostRepository;
    @Autowired private StudentCourseSecurityService studentCourseSecurityService;
    @Autowired private CourseSecurityService courseSecurityService; // On peut réutiliser d'autres services de sécurité

    /**
     * Vérifie si un utilisateur peut poster dans un fil de discussion.
     * Condition: il doit être inscrit au cours auquel le fil appartient.
     */
    public boolean canPostInThread(Authentication authentication, Long threadId) {
        Long courseId = forumThreadRepository.findCourseIdById(threadId);
        if (courseId == null) return false;
        
        return enrollmentRepository.existsByStudent_EmailAndCourseId(authentication.getName(), courseId);
    }

    /**
     * Vérifie si un utilisateur a le droit de voir (et donc de poster dans) un fil de discussion.
     * Conditions: être l'instructeur du cours OU être un élève inscrit au cours.
     * @param authentication L'objet d'authentification.
     * @param threadId L'ID du fil de discussion.
     * @return true si l'accès est autorisé.
     */
    
    public boolean canViewThread(Authentication authentication, Long threadId) {
        Long courseId = forumThreadRepository.findCourseIdById(threadId);
        if (courseId == null) return false; // Si le thread n'existe pas, accès refusé

        // On réutilise les services de sécurité existants
        boolean isInstructor = courseSecurityService.isInstructorOfCourse(authentication, courseId);
        boolean isEnrolledStudent = studentCourseSecurityService.isEnrolled(authentication, courseId);
        
        return isInstructor || isEnrolledStudent;
    }

    /**
     * Vérifie si un utilisateur peut modérer un fil de discussion.
     * Condition: il doit être l'instructeur du cours auquel le fil appartient.
     */
    public boolean canModerateThread(Authentication authentication, Long threadId) {
        Long courseId = forumThreadRepository.findCourseIdById(threadId);
        if (courseId == null) return false;

        return courseSecurityService.isInstructorOfCourse(authentication, courseId);
    }
    
    /**
     * Vérifie si un utilisateur peut modérer un post.
     * Condition: il doit être l'instructeur du cours auquel le post appartient.
     */
     public boolean canModeratePost(Authentication authentication, Long postId) {
        Long courseId = forumPostRepository.findCourseIdByPostId(postId);
        if (courseId == null) return false;
        
        return courseSecurityService.isInstructorOfCourse(authentication, courseId);
     }
}