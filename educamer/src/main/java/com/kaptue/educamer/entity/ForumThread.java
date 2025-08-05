package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Entity 
@Table(name = "forum_threads") 
@Getter 
@Setter
public class ForumThread {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String title;
    @ManyToOne @JoinColumn(name = "author_id") private User author;
    @ManyToOne @JoinColumn(name = "course_id") private Course course;
    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL) private Set<ForumPost> posts;
    private LocalDateTime createdAt = LocalDateTime.now();
}

