package com.kaptue.educamer.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter @Setter
public class QuizRequest {
    private String title;
    private List<QuestionRequest> questions; 
}