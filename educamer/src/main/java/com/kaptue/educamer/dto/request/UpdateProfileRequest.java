package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    @NotBlank @Size(min = 3, max = 100)
    private String name;
    // L'avatar sera géré par un endpoint séparé (upload de fichier)
}