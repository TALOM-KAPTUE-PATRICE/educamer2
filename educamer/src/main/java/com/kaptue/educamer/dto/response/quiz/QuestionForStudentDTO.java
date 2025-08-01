package com.kaptue.educamer.dto.response.quiz;

import com.kaptue.educamer.entity.Question;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class QuestionForStudentDTO {
    private Long id;
    private String text;
    private List<AnswerForStudentDTO> answers;

    public static QuestionForStudentDTO fromEntity(Question question) {
        QuestionForStudentDTO dto = new QuestionForStudentDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        if (question.getAnswers() != null) {
            dto.setAnswers(question.getAnswers().stream()
                .map(AnswerForStudentDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}