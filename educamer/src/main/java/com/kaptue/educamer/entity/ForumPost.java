package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;



@Entity @Table(name = "forum_posts") @Getter @Setter
public class ForumPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(columnDefinition = "TEXT") private String content;
    @ManyToOne @JoinColumn(name = "author_id") private User author;
    @ManyToOne @JoinColumn(name = "thread_id") private ForumThread thread;
    private LocalDateTime createdAt = LocalDateTime.now();
}
