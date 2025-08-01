package com.kaptue.educamer.dto.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LessonForStudentDTO {
    private Long id;
    private String title;
    private String content;
    private Integer lessonOrder;
    // ... On pourrait ajouter ici des DTOs pour les ressources, quiz, etc.
}