package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Spring Data JPA est assez intelligent pour comprendre "findByEmail" 
    // car Student hérite de User qui a un champ "email".
    Optional<Student> findByEmail(String email);

    // Vous pouvez ajouter ici des méthodes spécifiques aux étudiants.
    // Par exemple, trouver tous les étudiants inscrits à un cours spécifique.
    // @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE e.course.id = :courseId")
    // List<Student> findAllByCourseId(Long courseId);
}