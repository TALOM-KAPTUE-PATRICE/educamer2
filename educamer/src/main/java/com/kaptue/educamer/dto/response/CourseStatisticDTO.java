package com.kaptue.educamer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Génère un constructeur avec tous les arguments
public class CourseStatisticDTO {
    private long totalEnrollments;      // Nombre total d'inscrits
    private double averageProgress;     // Pourcentage de progression moyen
    private double averageGrade;        // Note moyenne du cours
    private long pendingSubmissions;    // Nombre de devoirs à corriger
}