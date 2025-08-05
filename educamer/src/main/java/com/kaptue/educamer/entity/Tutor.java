package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tutors")
@PrimaryKeyJoinColumn(name = "user_id")

@Getter 
@Setter
public class Tutor extends User {
    // Spécificités du tuteur, par ex. les matières dans lesquelles il est spécialisé
    private String specializations;
}