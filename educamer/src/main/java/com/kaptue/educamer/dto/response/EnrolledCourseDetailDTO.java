package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.Course;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;
import com.kaptue.educamer.dto.response.editor.AssignmentEditorDTO;

@Getter
@Setter
public class EnrolledCourseDetailDTO {
    private Long id;
    private String title;
    private String description;
    private String instructorName;
    private List<LessonForStudentDTO> lessons;

    private List<AssignmentEditorDTO> assignments; // <-- AJOUTER
    // ... On pourrait ajouter ici les devoirs, le lien vers le forum, etc.

    public static EnrolledCourseDetailDTO fromEntity(Course course) {
        EnrolledCourseDetailDTO dto = new EnrolledCourseDetailDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorName(course.getInstructor().getName());
        
        if (course.getLessons() != null) {
            dto.setLessons(course.getLessons().stream()
                .map(lesson -> {
                    LessonForStudentDTO lessonDto = new LessonForStudentDTO();
                    lessonDto.setId(lesson.getId());
                    lessonDto.setTitle(lesson.getTitle());
                    lessonDto.setLessonOrder(lesson.getLessonOrder());
                    // On ne renvoie pas le contenu complet ici pour alléger la charge
                    // On le récupérera via un appel dédié à /lessons/{id}
                    lessonDto.setContent(lesson.getContent().substring(0, Math.min(lesson.getContent().length(), 100)) + "...");
                    return lessonDto;
                })
                .collect(Collectors.toList()));
        }

        if (course.getAssignments() != null) {
            dto.setAssignments(course.getAssignments().stream()
                .map(AssignmentEditorDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}