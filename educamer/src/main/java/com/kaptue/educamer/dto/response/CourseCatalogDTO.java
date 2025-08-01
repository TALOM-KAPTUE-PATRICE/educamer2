package com.kaptue.educamer.dto.response;


import com.kaptue.educamer.entity.Course;
import com.kaptue.educamer.entity.CourseFeedback; 
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CourseCatalogDTO {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String instructorName;
    private int lessonCount;
    private int enrolledStudentCount;
    private double averageRating; // <-- NOUVEAU CHAMP

    public static CourseCatalogDTO fromEntity(Course course) {
        CourseCatalogDTO dto = new CourseCatalogDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription().length() > 150 ? course.getDescription().substring(0, 150) + "..." : course.getDescription());
        dto.setImageUrl(course.getImageUrl());
        dto.setInstructorName(course.getInstructor().getName());
        dto.setLessonCount(course.getLessons() != null ? course.getLessons().size() : 0);
        dto.setEnrolledStudentCount(course.getEnrollments() != null ? course.getEnrollments().size() : 0);
        
        // Calculer la note moyenne
        if (course.getFeedbacks() != null && !course.getFeedbacks().isEmpty()) {
            double avg = course.getFeedbacks().stream()
                .mapToInt(CourseFeedback::getRating)
                .average()
                .orElse(0.0);
            dto.setAverageRating(Math.round(avg * 10.0) / 10.0); // Arrondi à 1 décimale
        } else {
            dto.setAverageRating(0.0);
        }

        return dto;
    }
}