package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.LinkStudentRequest;
import com.kaptue.educamer.dto.response.ChildActivitySummaryDTO;
import com.kaptue.educamer.dto.response.LinkedStudentDTO;
import com.kaptue.educamer.entity.Parent;
import com.kaptue.educamer.entity.Student;
import com.kaptue.educamer.repository.EnrollmentRepository;
import com.kaptue.educamer.repository.ParentRepository;
import com.kaptue.educamer.repository.StudentRepository;
import com.kaptue.educamer.repository.SubmissionRepository;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParentService {

    @Autowired private ParentRepository parentRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private EnrollmentRepository enrollmentRepository; // Ajouter
    @Autowired private SubmissionRepository submissionRepository; // Ajouter


    /**
     * Lie un compte Élève à un compte Parent.
     * @param parentId L'ID du parent qui fait la demande.
     * @param studentEmail L'email de l'élève à lier.
     * @return Le DTO de l'élève qui a été lié.
     */

    @Transactional
    public LinkedStudentDTO linkStudentToParent(Long parentId, LinkStudentRequest request) {
        Parent parent = parentRepository.findById(parentId).orElseThrow(/*...*/);
        
        Student student = studentRepository.findByEmail(request.getStudentEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Aucun élève trouvé avec cet email."));
        
        // --- VÉRIFICATIONS DE SÉCURITÉ CRUCIALES ---
        if (student.getLinkingCode() == null || student.getLinkingCodeExpiry() == null) {
            throw new IllegalStateException("L'élève n'a pas généré de code de liaison.");
        }
        if (student.getLinkingCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Le code de liaison a expiré. Veuillez en générer un nouveau.");
        }
        if (!student.getLinkingCode().equals(request.getLinkingCode())) {
            throw new IllegalArgumentException("Le code de liaison est incorrect.");
        }
        // --- FIN DES VÉRIFICATIONS ---

        if (parent.getChildren().contains(student)) {
            throw new IllegalStateException("Cet élève est déjà lié à votre compte.");
        }

        // Si tout est bon, on lie le compte et on invalide le code
        student.setLinkingCode(null);
        student.setLinkingCodeExpiry(null);
        studentRepository.save(student);

        parent.getChildren().add(student);
        parentRepository.save(parent);
        
        return LinkedStudentDTO.fromEntity(student);
    }

    @Transactional(readOnly = true)
    public List<LinkedStudentDTO> getLinkedStudents(Long parentId) {
        Parent parent = parentRepository.findById(parentId).orElseThrow(() -> new ResourceNotFoundException("Compte Parent non trouvé avec l'ID: " + parentId));
        
        return parent.getChildren().stream().map(student -> {
            LinkedStudentDTO dto = LinkedStudentDTO.fromEntity(student);
            
            // Calculer le résumé d'activité
            long activeCourses = enrollmentRepository.countByStudentId(student.getId());
            long recentSubmissions = submissionRepository.countByStudentId(student.getId()); // Simplifié, on pourrait ajouter une contrainte de date
            long totalAssignments = submissionRepository.countTotalAssignmentsForStudent(student.getId()); // Méthode à créer
            
            dto.setActivitySummary(new ChildActivitySummaryDTO(activeCourses, recentSubmissions, totalAssignments));
            return dto;
        }).collect(Collectors.toList());
    }


}