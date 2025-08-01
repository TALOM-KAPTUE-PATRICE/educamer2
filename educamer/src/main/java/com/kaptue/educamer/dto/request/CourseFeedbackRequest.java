package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseFeedbackRequest {
    @NotNull
    @Min(value = 1, message = "La note doit être au minimum de 1.")
    @Max(value = 5, message = "La note doit être au maximum de 5.")
    private Integer rating;

    @Size(max = 2000, message = "Le commentaire ne peut pas dépasser 2000 caractères.")
    private String comment;
}