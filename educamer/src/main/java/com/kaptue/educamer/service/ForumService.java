package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.ForumPostRequest;
import com.kaptue.educamer.dto.request.ForumThreadCreateRequest;
import com.kaptue.educamer.dto.response.ForumPostDTO;
import com.kaptue.educamer.dto.response.ForumThreadDTO;
import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.*;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ForumService {

    @Autowired
    private ForumThreadRepository forumThreadRepository;
    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Transactional(readOnly = true)
    public List<ForumThreadDTO> getThreadsForCourse(Long courseId) {
        return forumThreadRepository.findByCourseId(courseId).stream()
                .map(ForumThreadDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ForumPostDTO addPostToThread(Long threadId, ForumPostRequest request, Long authorId) {
        ForumThread thread = forumThreadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Fil de discussion non trouvé"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé"));

        ForumPost post = new ForumPost();
        post.setContent(request.getContent());
        post.setThread(thread);
        post.setAuthor(author);

        ForumPost savedPost = forumPostRepository.save(post);
        return ForumPostDTO.fromEntity(savedPost);
    }

    @Transactional(readOnly = true)
    public ForumThread getThreadById(Long threadId) {
        return forumThreadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Fil de discussion non trouvé"));
    }

    @Transactional
    public ForumThread createThread(Long courseId, Long authorId, ForumThreadCreateRequest request) {
    Course course = courseRepository.findById(courseId).orElseThrow(/*...*/);
    User author = userRepository.findById(authorId).orElseThrow(/*...*/);

    // Créer le fil de discussion
    ForumThread thread = new ForumThread();
    thread.setTitle(request.getTitle());
    thread.setCourse(course);
    thread.setAuthor(author);
    
    // Créer le premier message
    ForumPost firstPost = new ForumPost();
    firstPost.setContent(request.getFirstPostContent());
    firstPost.setAuthor(author);
    firstPost.setThread(thread);

    thread.setPosts(Set.of(firstPost));
    
    return forumThreadRepository.save(thread);
}

}
