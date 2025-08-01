package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.HelpRequest;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;



@Getter
@Setter
public class HelpRequestDTO {
    private Long id;
    private String subject;
    private String description;
    private String status;
    private Long studentId;
    private String studentName;
    private Long tutorId;
    private String tutorName;
    private LocalDateTime createdAt;

    public static HelpRequestDTO fromEntity(HelpRequest request) {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setId(request.getId());
        dto.setSubject(request.getSubject());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus().name());
        dto.setStudentId(request.getStudent().getId());
        dto.setStudentName(request.getStudent().getName());
        if (request.getTutor() != null) {
            dto.setTutorId(request.getTutor().getId());
            dto.setTutorName(request.getTutor().getName());
        }
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }
}