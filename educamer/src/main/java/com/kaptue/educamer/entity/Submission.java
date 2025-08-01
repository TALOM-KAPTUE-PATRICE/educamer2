package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter @Setter
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String fileUrl; // URL vers le fichier sur Cloudinary

    @Column(columnDefinition = "TEXT")
    private String textContent; // Pour les soumissions textuelles
    
    private Double grade; // Note

    @Column(length = 255)
    private String submissionPublicId;
    
    @Column(columnDefinition = "TEXT")
    private String feedback; // Feedback de l'instructeur

    @Column(updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}