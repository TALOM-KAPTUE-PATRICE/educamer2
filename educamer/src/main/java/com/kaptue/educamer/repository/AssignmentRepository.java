package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByCourseId(Long courseId);
       /**
     * Vérifie l'existence d'un devoir par son ID et par l'email de l'instructeur
     * qui possède le cours auquel le devoir est rattaché.
     *
     * Spring Data JPA interprète ce nom de méthode pour créer une requête qui fait les jointures nécessaires :
     * Assignment -> Course -> Instructor -> email
     *
     * @param assignmentId L'ID du devoir.
     * @param email L'email de l'instructeur.
     * @return true si un tel enregistrement existe, false sinon.
     */
    boolean existsByIdAndCourse_Instructor_Email(Long assignmentId, String email);
}