package com.kaptue.educamer.dto.response;
import com.kaptue.educamer.entity.Course;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter 
@Setter
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String status;
    private String instructorName;
    private LocalDateTime createdAt;

    public static CourseResponse fromEntity(Course course) {
        CourseResponse dto = new CourseResponse();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setImageUrl(course.getImageUrl());
        dto.setStatus(course.getStatus().name());
        dto.setInstructorName(course.getInstructor().getName());
        dto.setCreatedAt(course.getCreatedAt());
        return dto;
    }
}