package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id") // Lien avec la table users
@Getter 
@Setter
public class Student extends User {


     @Column(length = 10)
    private String linkingCode;
    private LocalDateTime linkingCodeExpiry;

    // Relation avec les inscriptions
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;

    // Relation avec les tentatives de quiz
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<QuizAttempt> quizAttempts;

    // Relation avec les soumissions de devoirs
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Submission> submissions;
    
    // Un étudiant est lié à un ou plusieurs parents
    @ManyToMany(mappedBy = "children")
    private Set<Parent> parents;
}