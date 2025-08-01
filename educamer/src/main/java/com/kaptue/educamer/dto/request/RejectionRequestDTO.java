package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RejectionRequestDTO {
    @NotBlank(message = "Une raison pour le rejet est obligatoire.")
    private String reason;
}