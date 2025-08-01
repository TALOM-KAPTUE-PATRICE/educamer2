package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.CourseFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseFeedbackRepository extends JpaRepository<CourseFeedback, Long> {
     boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}