package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
        boolean existsByIdAndLesson_Course_Instructor_Email(Long resourceId, String email);
}