package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {

    List<ForumThread> findByCourseId(Long courseId);

    @Query("SELECT ft.course.id FROM ForumThread ft WHERE ft.id = :threadId")
    Long findCourseIdById(Long threadId);
}
