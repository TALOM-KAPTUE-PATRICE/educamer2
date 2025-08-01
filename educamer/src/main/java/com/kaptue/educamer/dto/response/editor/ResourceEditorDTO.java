package com.kaptue.educamer.dto.response.editor;

import com.kaptue.educamer.entity.Resource;
import com.kaptue.educamer.entity.enums.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceEditorDTO {
    private Long id;
    private String name;
    private ResourceType type;
    private String url;
    private String publicId;

    public static ResourceEditorDTO fromEntity(Resource resource) {
        ResourceEditorDTO dto = new ResourceEditorDTO();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setType(resource.getType());
        dto.setUrl(resource.getUrl());
        dto.setPublicId(resource.getPublicId());
        return dto;
    }
}