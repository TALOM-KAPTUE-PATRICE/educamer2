package com.kaptue.educamer.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity 
@Table(name = "quiz_attempts") 
@Getter 
@Setter
public class QuizAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name = "student_id") private Student student;
    @ManyToOne @JoinColumn(name = "quiz_id") private Quiz quiz;
    private int score;
    private int totalQuestions;
    private LocalDateTime completedAt = LocalDateTime.now();
}
