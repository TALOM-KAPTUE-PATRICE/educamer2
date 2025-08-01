package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class AnswerRequest {
    private Long id; // Pour la mise à jour
    @NotBlank private String text;
    private boolean isCorrect;
}