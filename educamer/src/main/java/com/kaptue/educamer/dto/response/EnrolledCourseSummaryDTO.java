package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.Enrollment;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EnrolledCourseSummaryDTO {
    private Long id; // ID du cours
    private String title;
    private String description;
    private String imageUrl;
    private String instructorName;
    private double progress; // Pourcentage de progression de l'élève

    public static EnrolledCourseSummaryDTO fromEnrollment(Enrollment enrollment) {
        EnrolledCourseSummaryDTO dto = new EnrolledCourseSummaryDTO();
        dto.setId(enrollment.getCourse().getId());
        dto.setTitle(enrollment.getCourse().getTitle());
        dto.setDescription(enrollment.getCourse().getDescription());
        dto.setImageUrl(enrollment.getCourse().getImageUrl());
        dto.setInstructorName(enrollment.getCourse().getInstructor().getName());
        dto.setProgress(enrollment.getProgress());
        return dto;
    }
}