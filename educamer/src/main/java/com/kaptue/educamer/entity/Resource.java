package com.kaptue.educamer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kaptue.educamer.entity.enums.ResourceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "resources")
@Getter 
@Setter
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Column(nullable = false, length = 1024)
    private String url; // Pour les ressources publiques (images, videos) ou les liens externes

    @Column(length = 255)
    private String publicId; // <-- AJOUTER : Pour la gestion (suppression) et les URLs signées

     // NOUVEAU CHAMP
    private boolean isPrivate = false; // Par défaut, les ressources sont publiques

    @JsonBackReference("lesson-resources")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}

