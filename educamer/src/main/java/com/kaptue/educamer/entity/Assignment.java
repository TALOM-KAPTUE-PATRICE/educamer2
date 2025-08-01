package com.kaptue.educamer.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "assignments")
@Getter @Setter
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private LocalDateTime dueDate;
    
    @Column(length = 1024)
    private String attachmentUrl;

    @Column(length = 255)
    private String attachmentPublicId;

    private boolean attachmentIsPrivate = false; // Pour gérer la logique

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private Set<Submission> submissions;
}