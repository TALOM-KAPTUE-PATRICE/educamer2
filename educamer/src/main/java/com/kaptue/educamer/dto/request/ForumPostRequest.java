package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumPostRequest {
    @NotBlank
    private String content;
}