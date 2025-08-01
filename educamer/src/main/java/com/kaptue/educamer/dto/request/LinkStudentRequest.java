package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class LinkStudentRequest {
    @NotBlank @Email
    private String studentEmail;
    @NotBlank @Size(min = 6, max = 6)
    private String linkingCode;
}