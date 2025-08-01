package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.kaptue.educamer.entity.enums.HelpRequestStatus;


@Entity
@Table(name = "help_requests")
@Getter
@Setter
public class HelpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id") // Le tuteur est assigné plus tard
    private Tutor tutor;

    @Column(nullable = false)
    private String subject; // ex: "Aide en Mathématiques - Chapitre 3"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private HelpRequestStatus status = HelpRequestStatus.OPEN;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime resolvedAt;
}

