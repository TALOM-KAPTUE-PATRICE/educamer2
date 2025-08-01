package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.AssignmentRequest;
import com.kaptue.educamer.dto.response.AssignmentResponse;
import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.*;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AssignmentService {

    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CloudinaryService cloudinaryService;

    @Transactional
    public AssignmentResponse createAssignment(Long courseId, AssignmentRequest request, MultipartFile file) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        assignment.setCourse(course);
        
        if (file != null && !file.isEmpty()) {
            String folder = "courses/" + courseId + "/assignments";
            FileUploadResponse response = cloudinaryService.uploadRawFile(file, folder); // Les devoirs sont des fichiers privés
           // On NE stocke PAS l'URL directe de Cloudinary
         
            assignment.setAttachmentIsPrivate(true);
            assignment.setAttachmentPublicId(response.getPublicId());
        }
        
        Assignment saved = assignmentRepository.save(assignment);
                // Important: après le premier save, l'ID est généré. On met à jour l'URL avec cet ID.
        if (saved.isAttachmentIsPrivate()) {
            saved.setAttachmentUrl("/api/files/assignment/" + saved.getId() + "/access");
            saved = assignmentRepository.save(saved);
        }

        return AssignmentResponse.fromEntity(saved);
    }
    
    @Transactional
    public AssignmentResponse updateAssignment(Long assignmentId, AssignmentRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Devoir non trouvé"));
            
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        // La gestion du fichier se fera via un endpoint dédié si besoin
        
        return AssignmentResponse.fromEntity(assignmentRepository.save(assignment));
    }

    @Transactional
    public void deleteAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Devoir non trouvé"));
            
        if (assignment.getAttachmentPublicId() != null) {
            cloudinaryService.deleteFile(assignment.getAttachmentPublicId());
        }
        
        assignmentRepository.delete(assignment);
    }
}