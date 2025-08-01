package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GradeRequest {
    @NotNull @Min(0) @Max(20) private Double grade;
    @Size(max = 2000) private String feedback;
}