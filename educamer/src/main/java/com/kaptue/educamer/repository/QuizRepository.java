package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q.lesson.course.id FROM Quiz q WHERE q.id = :quizId")
    Long findCourseIdByQuizId(Long quizId);
}
