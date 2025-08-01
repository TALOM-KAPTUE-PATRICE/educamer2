package com.kaptue.educamer.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuizAttemptResultDTO {
    private Long attemptId;
    private int score;
    private int totalQuestions;
    private double percentage;
    private String message;
}