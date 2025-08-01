package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.PedagogicalApplication;
import lombok.Getter;
import lombok.Setter;
import java.util.Base64;
import java.time.LocalDateTime;

@Getter
@Setter
public class InstructorApplicationDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specializations;
    private String motivation;
    private String encodedResumePublicId; // <-- NOUVEAU CHAMP
    private String resumeUrl; // URL pour que l'admin puisse voir/télécharger le CV    
    private String status;
    private LocalDateTime createdAt;

    public static InstructorApplicationDTO fromEntity(PedagogicalApplication application) {
        InstructorApplicationDTO dto = new InstructorApplicationDTO();
        dto.setId(application.getId());
        dto.setName(application.getName());
        dto.setEmail(application.getEmail());
        dto.setPhone(application.getPhone());
        dto.setSpecializations(application.getSpecializations());
        dto.setMotivation(application.getMotivation());
        dto.setResumeUrl(application.getResumeUrl());

        if (application.getResumePublicId() != null) {
            // Encoder le public_id en Base64 URL-safe
            dto.setEncodedResumePublicId(
                    Base64.getUrlEncoder().encodeToString(application.getResumePublicId().getBytes())
            );
        }
        dto.setStatus(application.getStatus().name());
        dto.setCreatedAt(application.getCreatedAt());
        return dto;
    }
}
