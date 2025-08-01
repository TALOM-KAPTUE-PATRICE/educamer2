package com.kaptue.educamer.dto.response.editor;

import com.kaptue.educamer.entity.Assignment;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentEditorDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String attachmentUrl;

    public static AssignmentEditorDTO fromEntity(Assignment assignment) {
        AssignmentEditorDTO dto = new AssignmentEditorDTO();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setAttachmentUrl(assignment.getAttachmentUrl());
        return dto;
    }
}