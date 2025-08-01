package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;  


@Getter @Setter
public class ResetPasswordRequest {
    @NotBlank private String token;
    @NotBlank @Size(min = 6) private String newPassword;
}