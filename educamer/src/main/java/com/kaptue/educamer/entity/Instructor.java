package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "instructors")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter @Setter
public class Instructor extends User {

    @Column(columnDefinition = "TEXT")
    private String biography;

    // Un instructeur crée et possède plusieurs cours
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> coursesTaught;
}