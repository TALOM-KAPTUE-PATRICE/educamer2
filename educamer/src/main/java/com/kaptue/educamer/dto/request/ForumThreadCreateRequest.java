package com.kaptue.educamer.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForumThreadCreateRequest {
    @NotBlank private String title;
    @NotBlank private String firstPostContent;
}