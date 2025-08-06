package com.kaptue.educamer.dto.response;
import com.kaptue.educamer.entity.Lesson;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class LessonResponse {
    private Long id;
    private String title;
    private String content;
    private Integer lessonOrder;

    public static LessonResponse fromEntity(Lesson lesson) {
        LessonResponse dto = new LessonResponse();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setLessonOrder(lesson.getLessonOrder());
        return dto;
    }
}