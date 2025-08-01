package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.Enrollment;
import lombok.Getter;
import lombok.Setter;
//Ce DTO regroupera les informations de progression d'un élève dans un cours spécifique.
@Getter
@Setter
public class StudentProgressDTO {
    private Long studentId;
    private String studentName;
    private String studentAvatarUrl;
    private double courseProgress; // Pourcentage de complétion du cours (0.0 à 100.0)
    private int completedLessons;
    private int totalLessons;

    /**
     * Crée un DTO de progression à partir d'une entité Enrollment.
     * La logique de calcul (completedLessons, etc.) sera dans le service.
     */
    public static StudentProgressDTO fromEnrollment(Enrollment enrollment) {
        StudentProgressDTO dto = new StudentProgressDTO();
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getName());
        dto.setStudentAvatarUrl(enrollment.getStudent().getAvatarUrl());
        dto.setCourseProgress(enrollment.getProgress());
        // Les champs 'completedLessons' et 'totalLessons' seront remplis par le service.
        return dto;
    }
}