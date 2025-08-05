package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter

public class LessonRequest {
    @NotBlank @Size(max = 200)
    private String title;

    @NotNull @Min(1)
    private Integer lessonOrder;

    @NotBlank
    private String content;

    
}