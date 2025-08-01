package com.kaptue.educamer.dto.response;
// ... imports ...

import java.time.LocalDateTime;

import com.kaptue.educamer.entity.Submission;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubmissionResponse {
    private Long id;
    private String fileUrl;
    private String textContent;
    private Double grade;
    private String feedback;
    private LocalDateTime submittedAt;
    private Long studentId;
    private String studentName;
    private Long assignmentId;

    public static SubmissionResponse fromEntity(Submission submission) {
        SubmissionResponse dto = new SubmissionResponse();
        // ... (mapper les champs)
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getName());
        // ...
        return dto;
    }
}