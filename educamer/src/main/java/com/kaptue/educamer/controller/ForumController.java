package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.ForumPostRequest;
import com.kaptue.educamer.dto.request.ForumThreadCreateRequest;
import com.kaptue.educamer.dto.response.ForumPostDTO;
import com.kaptue.educamer.dto.response.ForumThreadDTO;
import com.kaptue.educamer.entity.ForumThread;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.security.CurrentUser;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
@PreAuthorize("isAuthenticated()") // Tous les utilisateurs authentifiés peuvent accéder au forum
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/course/{courseId}/threads")
    @PreAuthorize("hasAuthority('" + Permission.FORUM_READ + "') and (@courseSecurityService.isInstructorOfCourse(authentication, #courseId) or @studentCourseSecurityService.isEnrolled(authentication, #courseId)) ")
    public ResponseEntity<List<ForumThreadDTO>> getCourseThreads(@PathVariable Long courseId) {
        return ResponseEntity.ok(forumService.getThreadsForCourse(courseId));
    }

    @PostMapping("/threads/{threadId}/posts")
    // On vérifie la permission de poster ET que l'utilisateur a le droit de poster dans CE thread spécifique.
    @PreAuthorize("hasAuthority('" + Permission.FORUM_POST + "') and (@forumSecurityService.canPostInThread(authentication, #threadId) or @forumSecurityService.canViewThread(authentication, #threadId))")
    public ResponseEntity<ForumPostDTO> createPost(@PathVariable Long threadId, @Valid @RequestBody ForumPostRequest request, @CurrentUser User currentUser) {
        ForumPostDTO newPost = forumService.addPostToThread(threadId, request, currentUser.getId());
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping("/threads/{threadId}")
    @PreAuthorize("@forumSecurityService.canPostInThread(authentication, #threadId) or @forumSecurityService.canViewThread(authentication, #threadId)")
    public ResponseEntity<ForumThreadDTO> getThreadById(@PathVariable Long threadId) {
        // Ajouter une vérification de sécurité
        return ResponseEntity.ok(ForumThreadDTO.fromEntity(forumService.getThreadById(threadId)));
    }

    @PostMapping("/course/{courseId}/threads")
    @PreAuthorize("@studentCourseSecurityService.isEnrolled(authentication, #courseId)")
    public ResponseEntity<ForumThreadDTO> createThread(
            @PathVariable Long courseId,
            @CurrentUser User currentUser,
            @Valid @RequestBody ForumThreadCreateRequest request) {
        ForumThread newThread = forumService.createThread(courseId, currentUser.getId(), request);
        return new ResponseEntity<>(ForumThreadDTO.fromEntity(newThread), HttpStatus.CREATED);
    }
    
}
