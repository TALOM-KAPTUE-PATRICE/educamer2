package com.kaptue.educamer.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonOrderRequest {
    @NotEmpty
    private List<Long> lessonIds; // La liste des IDs de leçons dans le nouvel ordre
}