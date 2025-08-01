package com.kaptue.educamer.dto.response.editor;

import com.kaptue.educamer.entity.Course;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CourseEditorDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private List<LessonEditorDTO> lessons;
    private List<AssignmentEditorDTO> assignments; // Utilise le nouveau DTO

    public static CourseEditorDTO fromEntity(Course course) {
        CourseEditorDTO dto = new CourseEditorDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setStatus(course.getStatus().name());
       
        if (course.getLessons() != null) {
            dto.setLessons(course.getLessons().stream().map(LessonEditorDTO::fromEntity).collect(Collectors.toList()));
        }
        if (course.getAssignments() != null) {
            // Mappe vers le nouveau DTO qui contient toutes les infos
            dto.setAssignments(course.getAssignments().stream().map(AssignmentEditorDTO::fromEntity).collect(Collectors.toList()));
        }
        return dto;
    }
}