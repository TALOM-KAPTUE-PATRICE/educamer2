package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.Lesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonSummaryDTO {
    private Long id;
    private String title;
    private Integer lessonOrder;
    private boolean isCompleted;
    private boolean isLocked;

    public static LessonSummaryDTO fromEntity(Lesson lesson) {
        LessonSummaryDTO dto = new LessonSummaryDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setLessonOrder(lesson.getLessonOrder());
        // Les champs isCompleted et isLocked seront définis dans le service.
        return dto;
    }
}