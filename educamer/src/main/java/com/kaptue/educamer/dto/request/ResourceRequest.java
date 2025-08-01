package com.kaptue.educamer.dto.request;

import com.kaptue.educamer.entity.enums.ResourceType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResourceRequest {
    @NotBlank private String name;
    @NotNull private ResourceType type;
    private String url; // Pour les liens externes
}