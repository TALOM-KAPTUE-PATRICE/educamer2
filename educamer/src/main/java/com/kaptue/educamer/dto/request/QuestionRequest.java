package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter @Setter
public class QuestionRequest {
    private Long id; // Pour la mise à jour
    @NotBlank private String text;
    @NotEmpty @Size(min = 2) private List<AnswerRequest> answers;
}