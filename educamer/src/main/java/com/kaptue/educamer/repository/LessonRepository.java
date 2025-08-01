package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    /**
     * Trouve toutes les leçons associées à un cours spécifique,
     * et les trie par leur numéro d'ordre croissant.
     * C'est utile pour afficher les leçons dans le bon ordre sur l'interface.
     *
     * @param courseId L'ID du cours.
     * @return Une liste de leçons triées.
     */

    List<Lesson> findByCourseIdOrderByLessonOrderAsc(Long courseId);

        /**
     * Trouve toutes les leçons associées à un cours spécifique.
     * C'est la méthode que nous utilisons dans la logique de mise à jour de l'ordre.
     * Il n'est pas nécessaire de les trier ici car nous allons les manipuler en mémoire.
     *
     * @param courseId L'ID du cours.
     * @return Une liste de leçons non triées.
     */
    List<Lesson> findByCourseId(Long courseId); // <-- MÉTHODE MANQUANTE À AJOUTER
        // Pour la sécurité
    boolean existsByIdAndCourse_Instructor_Email(Long lessonId, String email);

    Optional<Lesson> findByCourseIdAndLessonOrder(Long courseId, int order);

    long countByCourseId(Long courseId);
}