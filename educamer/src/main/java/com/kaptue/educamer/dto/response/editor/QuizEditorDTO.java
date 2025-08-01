package com.kaptue.educamer.dto.response.editor;

import com.kaptue.educamer.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizEditorDTO {
    private Long id;
    private String title;
    // On pourrait ajouter les questions ici si nécessaire

    public static QuizEditorDTO fromEntity(Quiz quiz) {
        if (quiz == null) {
            return null; // Important de gérer le cas où il n'y a pas de quiz
        }
        QuizEditorDTO dto = new QuizEditorDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        return dto;
    }
}