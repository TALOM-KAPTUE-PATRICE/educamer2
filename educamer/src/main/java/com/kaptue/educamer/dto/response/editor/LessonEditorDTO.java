package com.kaptue.educamer.dto.response.editor;

import com.kaptue.educamer.entity.Lesson;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class LessonEditorDTO {
    private Long id;
    private String title;
    private String content;
    private Integer lessonOrder;
    private List<ResourceEditorDTO> resources;
    private QuizEditorDTO quiz;
    private boolean completionRequired;

    public static LessonEditorDTO fromEntity(Lesson lesson) {
        // Crée l'objet DTO
        LessonEditorDTO dto = new LessonEditorDTO();
        
        // Mappe les champs simples
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setLessonOrder(lesson.getLessonOrder());
        dto.setCompletionRequired(lesson.isCompletionRequired());

        // Mappe la liste des ressources
        if (lesson.getResources() != null) {
            dto.setResources(lesson.getResources().stream()
                .map(ResourceEditorDTO::fromEntity)
                .collect(Collectors.toList()));
        }

        // Mappe le quiz associé
        dto.setQuiz(QuizEditorDTO.fromEntity(lesson.getQuiz()));
        
        // Retourne le DTO complet
        return dto;
    }
}