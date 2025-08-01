package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.ForumPostUpdateRequest;
import com.kaptue.educamer.dto.response.ForumPostDTO;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.ForumModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/moderation/forum")
// La permission de base est 'forum:moderate'
@PreAuthorize("hasAuthority('" + Permission.FORUM_MODERATE + "')")
public class ForumModerationController {

    @Autowired
    private ForumModerationService forumModerationService;

    /**
     * USE CASE: Modérer le Forum (Supprimer un fil)
     */
    @DeleteMapping("/threads/{threadId}")
    @PreAuthorize("@forumSecurityService.canModerateThread(authentication, #threadId)")
    public ResponseEntity<Void> deleteThread(@PathVariable Long threadId) {
        forumModerationService.deleteForumThread(threadId);
        return ResponseEntity.noContent().build();
    }

    /**
     * USE CASE: Modérer le Forum (Supprimer un message)
     */
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("@forumSecurityService.canModeratePost(authentication, #postId)")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        forumModerationService.deleteForumPost(postId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * USE CASE: Modérer le Forum (Modifier un message)
     */
    @PutMapping("/posts/{postId}")
    @PreAuthorize("@forumSecurityService.canModeratePost(authentication, #postId)")
    public ResponseEntity<ForumPostDTO> updatePost(@PathVariable Long postId, @Valid @RequestBody ForumPostUpdateRequest request) {
        // On retourne le post mis à jour pour que le frontend puisse l'afficher directement
        return ResponseEntity.ok(ForumPostDTO.fromEntity(forumModerationService.updateForumPost(postId, request)));
    }
}