package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class AssignmentRequest {
    @NotBlank @Size(max = 150)
    private String title;

    @NotBlank
    private String description;

    @Future
    private LocalDateTime dueDate;
}