package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    @Query("SELECT fp.thread.course.id FROM ForumPost fp WHERE fp.id = :postId")
    Long findCourseIdByPostId(Long postId);
}
