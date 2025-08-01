package com.kaptue.educamer.dto.response;
import com.kaptue.educamer.entity.Assignment;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class AssignmentResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String attachmentUrl; 

    public static AssignmentResponse fromEntity(Assignment assignment) {
        AssignmentResponse dto = new AssignmentResponse();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setAttachmentUrl(assignment.getAttachmentUrl()); // <-- AJOUTER
        return dto;
    }
}