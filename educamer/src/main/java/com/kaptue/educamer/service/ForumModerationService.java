package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.ForumPostUpdateRequest;
import com.kaptue.educamer.entity.ForumPost;
import com.kaptue.educamer.repository.ForumPostRepository;
import com.kaptue.educamer.repository.ForumThreadRepository;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ForumModerationService {

    @Autowired private ForumThreadRepository forumThreadRepository;
    @Autowired private ForumPostRepository forumPostRepository;

    /**
     * Supprime un fil de discussion complet, y compris tous ses messages (géré par la cascade).
     */
    @Transactional
    public void deleteForumThread(Long threadId) {
        if (!forumThreadRepository.existsById(threadId)) {
            throw new ResourceNotFoundException("Fil de discussion non trouvé avec l'ID: " + threadId);
        }
        forumThreadRepository.deleteById(threadId);
    }

    /**
     * Supprime un message spécifique du forum.
     */
    @Transactional
    public void deleteForumPost(Long postId) {
        if (!forumPostRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Message du forum non trouvé avec l'ID: " + postId);
        }
        forumPostRepository.deleteById(postId);
    }
    
    /**
     * Modifie le contenu d'un message existant.
     * L'auteur original reste le même.
     */  
    @Transactional
    public ForumPost updateForumPost(Long postId, ForumPostUpdateRequest request) {
    ForumPost post = forumPostRepository.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("Message du forum non trouvé"));
        
    post.setContent(request.getContent() + "\n\n*(Message modifié par un modérateur)*");
    return forumPostRepository.save(post);
}
}