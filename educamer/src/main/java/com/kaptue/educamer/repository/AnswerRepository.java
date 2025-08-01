package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Answer;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndQuestion_Id(Long answerId, Long questionId);
}