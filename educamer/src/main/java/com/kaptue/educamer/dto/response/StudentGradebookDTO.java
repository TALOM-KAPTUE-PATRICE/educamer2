package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.Student;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class StudentGradebookDTO {
    private Long studentId;
    private String studentName;
    private double averageGrade; // Note moyenne calculée
    private List<GradebookEntryDTO> grades;
    
    public static StudentGradebookDTO fromStudent(Student student) {
        StudentGradebookDTO dto = new StudentGradebookDTO();
        dto.setStudentId(student.getId());
        dto.setStudentName(student.getName());
        // 'averageGrade' et 'grades' seront remplis par le service
        return dto;
    }
}