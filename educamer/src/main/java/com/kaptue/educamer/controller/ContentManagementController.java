package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.QuizRequest;
import com.kaptue.educamer.dto.request.ResourceRequest;
import com.kaptue.educamer.dto.response.editor.LessonEditorDTO;
import com.kaptue.educamer.entity.Lesson;
import com.kaptue.educamer.service.LessonService;
import com.kaptue.educamer.service.QuizManagementService;
import com.kaptue.educamer.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/instructor/content")
@PreAuthorize("hasAuthority('content:manage')")
public class ContentManagementController {
    @Autowired private LessonService lessonService;
    @Autowired private ResourceService resourceService;
    @Autowired private QuizManagementService quizManagementService;

    @PostMapping("/lessons/{lessonId}/resources")
    // Ajouter une vérification de sécurité fine pour s'assurer que l'instructeur possède la leçon
     @PreAuthorize("@lessonSecurityService.isInstructorOfLesson(authentication, #lessonId)")
    public ResponseEntity<LessonEditorDTO> addResource(
            @PathVariable Long lessonId,
            @Valid @RequestPart("data") ResourceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        
        Lesson updatedLesson = resourceService.addResourceToLesson(lessonId, request, file);
        return ResponseEntity.ok(LessonEditorDTO.fromEntity(updatedLesson));
    }

    @DeleteMapping("/resources/{resourceId}")
    @PreAuthorize("@resourceSecurityService.isInstructorOfResource(authentication, #resourceId)")
    public ResponseEntity<Void> deleteResource(@PathVariable Long resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lessons/{lessonId}/quiz")
    @PreAuthorize("@lessonSecurityService.isInstructorOfLesson(authentication, #lessonId)")
    public ResponseEntity<LessonEditorDTO> createOrUpdateQuiz(@PathVariable Long lessonId, @Valid @RequestBody QuizRequest request) {
        Lesson updatedLesson = quizManagementService.createOrUpdateQuizForLesson(lessonId, request);
        return ResponseEntity.ok(LessonEditorDTO.fromEntity(updatedLesson));
    }


    @DeleteMapping("/lessons/{lessonId}")
    // @PreAuthorize("@lessonSecurityService.isInstructorOfLesson(authentication, #lessonId)")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
    
    
}