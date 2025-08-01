package com.kaptue.educamer.dto.response.quiz;

import com.kaptue.educamer.entity.Answer;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnswerForStudentDTO {
    private Long id;
    private String text;
    // Note: On n'inclut PAS le champ 'isCorrect' volontairement

    public static AnswerForStudentDTO fromEntity(Answer answer) {
        AnswerForStudentDTO dto = new AnswerForStudentDTO();
        dto.setId(answer.getId());
        dto.setText(answer.getText());
        return dto;
    }
}