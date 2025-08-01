package com.kaptue.educamer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileUploadResponse {
    private String publicId; // L'identifiant unique pour les suppressions futures
    private String secureUrl; // L'URL pour l'affichage
}