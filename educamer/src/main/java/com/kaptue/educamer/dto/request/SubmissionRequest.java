package com.kaptue.educamer.dto.request;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class SubmissionRequest {
    // Le fichier est envoyé en multipart/form-data, pas en JSON.
    private MultipartFile file;
    // On pourrait aussi avoir un champ de texte
    private String textContent;
}