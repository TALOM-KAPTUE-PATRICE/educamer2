package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.dto.response.quiz.QuizForStudentDTO;
import com.kaptue.educamer.dto.response.editor.QuizEditorDTO; // Réutiliser
import com.kaptue.educamer.dto.response.editor.ResourceEditorDTO; // Réutiliser
import com.kaptue.educamer.entity.Lesson;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;


@Getter @Setter
public class LessonDetailDTO {
    private Long id;
    private String title;
    private String content;
    private boolean completionRequired;
    private List<ResourceEditorDTO> resources;
    private QuizForStudentDTO  quiz;
    private boolean isCompletedByStudent; // <-- NOUVEAU CHAMP
    // On pourrait ajouter ici si l'élève a déjà complété la leçon

    public static LessonDetailDTO fromEntity(Lesson lesson) {
        LessonDetailDTO dto = new LessonDetailDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setCompletionRequired(lesson.isCompletionRequired());
        if (lesson.getResources() != null) {
            dto.setResources(lesson.getResources().stream().map(ResourceEditorDTO::fromEntity).collect(Collectors.toList()));
        }
        if (lesson.getQuiz() != null) {
            dto.setQuiz(QuizForStudentDTO.fromEntity(lesson.getQuiz()));
        }
        return dto;
    }
}