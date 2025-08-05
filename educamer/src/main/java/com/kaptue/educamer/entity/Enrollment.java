package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = { @UniqueConstraint(columnNames = {"student_id", "course_id"}) })
@Getter 
@Setter
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name = "student_id") private Student student;
    @ManyToOne @JoinColumn(name = "course_id") private Course course;
    private LocalDateTime enrolledAt = LocalDateTime.now();
    private double progress = 0.0;
}