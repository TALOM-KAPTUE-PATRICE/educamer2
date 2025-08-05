package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "parents")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter 
@Setter
public class Parent extends User {

    @ManyToMany
    @JoinTable(
        name = "parent_student_link",
        joinColumns = @JoinColumn(name = "parent_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> children;
}