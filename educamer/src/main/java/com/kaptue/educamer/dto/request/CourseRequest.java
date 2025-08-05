package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class CourseRequest {
    @NotBlank @Size(min = 5, max = 150)
    private String title;
    
    @NotBlank @Size(min = 20)
    private String description;
}