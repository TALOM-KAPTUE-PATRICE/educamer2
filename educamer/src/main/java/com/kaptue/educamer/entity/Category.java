package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter 
@Setter
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    // Un cours peut appartenir à plusieurs catégories
    @ManyToMany(mappedBy = "categories")
    private Set<Course> courses;
}