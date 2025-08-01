package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {}