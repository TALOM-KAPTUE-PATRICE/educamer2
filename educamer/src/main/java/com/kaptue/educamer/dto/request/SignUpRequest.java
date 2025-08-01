package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class SignUpRequest {
    @NotBlank @Size(min = 3, max = 50) private String name;
    @NotBlank @Email @Size(max = 100) private String email;
    @NotBlank @Size(min = 6, max = 40) private String password;
    @NotNull private String role; // "ELEVE" ou "INSTRUCTEUR"
}