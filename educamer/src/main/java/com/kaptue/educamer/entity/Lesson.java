package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet; 
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "lessons")
@Getter 
@Setter
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private Integer lessonOrder;

    private boolean completionRequired = false; // Par défaut, pas de blocage

    @Column(columnDefinition = "TEXT")
    private String content; // Contenu textuel principal de la leçon


    @JsonBackReference("course-lessons")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    @JsonManagedReference("lesson-resources")
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resource> resources = new HashSet<>();


        // Nouvelle relation gérée
    @JsonManagedReference("lesson-quiz")
    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Quiz quiz;
}