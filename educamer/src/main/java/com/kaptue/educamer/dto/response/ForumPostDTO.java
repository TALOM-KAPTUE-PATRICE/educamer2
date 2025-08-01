package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.ForumPost;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ForumPostDTO {
    private Long id;
    private String content;
    private Long authorId;
    private String authorName;
    private String authorAvatarUrl;
    private LocalDateTime createdAt;

    public static ForumPostDTO fromEntity(ForumPost post) {
        ForumPostDTO dto = new ForumPostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        if (post.getAuthor() != null) {
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorName(post.getAuthor().getName());
            dto.setAuthorAvatarUrl(post.getAuthor().getAvatarUrl());
        }
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}