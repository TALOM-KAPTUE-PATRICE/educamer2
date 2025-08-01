package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.kaptue.educamer.entity.enums.ApplicationStatus;
import com.kaptue.educamer.entity.enums.ApplicationRole;

@Entity
@Table(name = "pedagogical_applications")
@Getter
@Setter
public class PedagogicalApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(columnDefinition = "TEXT")
    private String motivation;
    @Column(columnDefinition = "TEXT")
    private String specializations; // ex: "Maths, Physique-Chimie"
    private String resumeUrl;
    private String resumePublicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationRole desiredRole;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private LocalDateTime createdAt = LocalDateTime.now();
}
