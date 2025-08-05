package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity 
@Table(name = "course_feedbacks") 
@Getter 
@Setter
public class CourseFeedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private int rating; // 1 à 5
    @Column(columnDefinition = "TEXT") private String comment;
    @ManyToOne @JoinColumn(name = "student_id") private Student student;
    @ManyToOne @JoinColumn(name = "course_id") private Course course;
    private LocalDateTime createdAt = LocalDateTime.now();
}