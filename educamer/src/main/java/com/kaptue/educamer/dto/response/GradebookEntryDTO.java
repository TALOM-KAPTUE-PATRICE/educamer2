package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.Submission;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
// Ce DTO affichera les notes d'un élève pour les différents devoirs d'un cours.
@Getter
@Setter
public class GradebookEntryDTO {
    private Long submissionId;
    private Long assignmentId;
    private String assignmentTitle;
    private Double grade; // La note obtenue
    private LocalDateTime submittedAt; // Date de soumission
    private boolean isGraded; // Indique si le devoir a été noté

    public static GradebookEntryDTO fromSubmission(Submission submission) {
        GradebookEntryDTO dto = new GradebookEntryDTO();
        dto.setSubmissionId(submission.getId());
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setAssignmentTitle(submission.getAssignment().getTitle());
        dto.setGrade(submission.getGrade());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setGraded(submission.getGrade() != null);
        return dto;
    }
}