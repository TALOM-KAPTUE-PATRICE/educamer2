package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet; // <-- Importer

@Entity @Table(name = "quizzes") 
@Getter 
@Setter
public class Quiz {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String title;

    @JsonBackReference("lesson-quiz")
    @OneToOne @JoinColumn(name = "lesson_id") private Lesson lesson;
        
    @JsonManagedReference("quiz-questions")
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true) 
    private Set<Question> questions = new HashSet<>();
}
