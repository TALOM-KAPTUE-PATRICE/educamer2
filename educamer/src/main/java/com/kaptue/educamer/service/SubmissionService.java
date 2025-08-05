package com.kaptue.educamer.service;
// ... imports ...

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaptue.educamer.dto.request.GradeRequest;
import com.kaptue.educamer.dto.request.SubmissionRequest;
import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.dto.response.SubmissionResponse;
import com.kaptue.educamer.entity.Assignment;
import com.kaptue.educamer.entity.Student;
import com.kaptue.educamer.entity.Submission;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.repository.AssignmentRepository;
import com.kaptue.educamer.repository.StudentRepository;
import com.kaptue.educamer.repository.SubmissionRepository;
import jakarta.transaction.Transactional;


@Service
public class SubmissionService {
    @Autowired private SubmissionRepository submissionRepository;
    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CloudinaryService cloudinaryService; // Le service Cloudinary

    @Transactional
    public SubmissionResponse createSubmission(Long assignmentId, Long studentId, SubmissionRequest request) {
        // Vérifier si l'étudiant est bien inscrit au cours... (logique à ajouter)
        
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new ResourceNotFoundException("Devoir non trouvé"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        // Un étudiant ne peut soumettre qu'une seule fois
        if(submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId).isPresent()) {
            throw new RuntimeException("Vous avez déjà soumis un travail pour ce devoir.");
        }
        
        Submission submission = new Submission();
        submission.setAssignment(assignment);        
        submission.setStudent(student);
        submission.setTextContent(request.getTextContent());

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            // Uploader le fichier sur Cloudinary dans un dossier spécifique
            FileUploadResponse response = cloudinaryService.uploadRawFile(request.getFile(), "submissions" );
            submission.setFileUrl(response.getSecureUrl());
            submission.setSubmissionPublicId(response.getPublicId());
            
        }

        Submission saved = submissionRepository.save(submission);


                // Trouver la leçon associée au devoir (s'il y en a une, ce qui n'est pas le cas dans notre modèle actuel).
        // Si les devoirs étaient liés à des leçons, on ferait :
        // Long lessonId = saved.getAssignment().getLesson().getId();
        // progressTrackingService.markLessonAsCompleted(studentId, lessonId);
        
        // Logique alternative: si un devoir est lié à un cours, on ne peut pas marquer une leçon spécifique.
        // On pourrait décider que soumettre TOUS les devoirs d'un cours contribue à la progression.
        // Pour l'instant, on laisse cette partie en suspens car notre modèle lie les devoirs aux cours, pas aux leçons.

        return SubmissionResponse.fromEntity(saved);
    }
    
    @Transactional
    public SubmissionResponse gradeSubmission(Long submissionId, GradeRequest gradeRequest) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> new ResourceNotFoundException("Soumission non trouvée"));
        
        submission.setGrade(gradeRequest.getGrade());
        submission.setFeedback(gradeRequest.getFeedback());
        
        Submission saved = submissionRepository.save(submission);
        // Envoyer une notification à l'élève... (logique à ajouter)
        return SubmissionResponse.fromEntity(saved);
    }
}