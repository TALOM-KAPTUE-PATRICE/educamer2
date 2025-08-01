package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {}