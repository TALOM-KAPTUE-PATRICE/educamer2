package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.ForumThread;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ForumThreadDTO {
    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
    private List<ForumPostDTO> posts;

    public static ForumThreadDTO fromEntity(ForumThread thread) {
        ForumThreadDTO dto = new ForumThreadDTO();
        dto.setId(thread.getId());
        dto.setTitle(thread.getTitle());
        if (thread.getAuthor() != null) {
            dto.setAuthorId(thread.getAuthor().getId());
            dto.setAuthorName(thread.getAuthor().getName());
        }
        dto.setCreatedAt(thread.getCreatedAt());
        if (thread.getPosts() != null) {
            dto.setPosts(thread.getPosts().stream()
                .map(ForumPostDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}