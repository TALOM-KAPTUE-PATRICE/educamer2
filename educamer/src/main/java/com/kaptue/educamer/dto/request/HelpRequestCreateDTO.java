package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class HelpRequestCreateDTO {
    @NotBlank @Size(max = 200) private String subject;
    @NotBlank @Size(min = 20) private String description;
}