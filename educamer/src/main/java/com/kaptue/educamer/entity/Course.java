package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet; // <-- Importer
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kaptue.educamer.entity.enums.CourseStatus;


@Entity
@Table(name = "courses")
@Getter 
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String imageUrl; // Pour une image de couverture

   @Column(length = 255)
    private String coursePublicId;


    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

     // C'est le "père" de la relation. Il sera sérialisé.
    @JsonManagedReference("course-lessons")
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lessonOrder ASC")
    private Set<Lesson> lessons = new HashSet<>();

    @JsonManagedReference("course-enrollments")
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
     private Set<Enrollment> enrollments = new HashSet<>();

    @JsonManagedReference("course-assignments")
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
      private Set<Assignment> assignments = new HashSet<>();
    
    @JsonManagedReference("course-feedbacks")
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseFeedback> feedbacks = new HashSet<>();
    
    @JsonManagedReference("course-threads")
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ForumThread> forumThreads = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "course_category",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
    
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;


    public Course() {
        this.status = CourseStatus.DRAFT;
    }




}


