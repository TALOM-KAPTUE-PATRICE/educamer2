package com.kaptue.educamer.dto.response.quiz;

import com.kaptue.educamer.entity.Quiz;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class QuizForStudentDTO {
    private Long id;
    private String title;
    private List<QuestionForStudentDTO> questions;

    public static QuizForStudentDTO fromEntity(Quiz quiz) {
        if (quiz == null) return null;
        QuizForStudentDTO dto = new QuizForStudentDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        if (quiz.getQuestions() != null) {
            dto.setQuestions(quiz.getQuestions().stream()
                .map(QuestionForStudentDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}