package com.kaptue.educamer.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.kaptue.educamer.entity.enums.ApplicationRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequest {

    @NotBlank @Size(min = 3)
    private String name;

    @NotBlank @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String specializations;

    @NotBlank @Size(min = 50)
    private String motivation;

    @NotNull(message = "Vous devez choisir un rôle.")
    private ApplicationRole desiredRole;

    @NotNull(message = "Le CV est obligatoire.")
    private MultipartFile resume; // Le fichier CV
}