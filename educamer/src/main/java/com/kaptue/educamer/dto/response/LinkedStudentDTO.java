package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkedStudentDTO {
    private Long id;
    private String name;
    private String avatarUrl;
    
    private ChildActivitySummaryDTO activitySummary; // <-- AJOUTER

    public static LinkedStudentDTO fromEntity(Student student) {
        LinkedStudentDTO dto = new LinkedStudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setAvatarUrl(student.getAvatarUrl());

        return dto;
    }
}