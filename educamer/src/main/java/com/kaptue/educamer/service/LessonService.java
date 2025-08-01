package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.LessonOrderRequest;
import com.kaptue.educamer.dto.request.LessonRequest;
import com.kaptue.educamer.dto.response.LessonResponse;
import com.kaptue.educamer.entity.Course;
import com.kaptue.educamer.entity.Lesson;
import com.kaptue.educamer.repository.CourseRepository;
import com.kaptue.educamer.repository.LessonRepository;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kaptue.educamer.entity.Resource;


@Service
public class LessonService {

    @Autowired private LessonRepository lessonRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CloudinaryService cloudinaryService;

    /**
     * Ajoute une nouvelle leçon à un cours existant.
     */
    @Transactional
    public LessonResponse addLessonToCourse(Long courseId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setLessonOrder(request.getLessonOrder());
        lesson.setCourse(course); // Lier la leçon au cours

        Lesson savedLesson = lessonRepository.save(lesson);
        return LessonResponse.fromEntity(savedLesson);
    }

        // ... (injections et méthode addLessonToCourse)

    /**
     * Met à jour l'ordre des leçons pour un cours donné.
     * C'est une opération optimisée pour éviter de multiples requêtes.
     * @param courseId L'ID du cours concerné.
     * @param request Le DTO contenant la liste ordonnée des IDs de leçons.
     */
    @Transactional
    public void updateLessonOrder(Long courseId, LessonOrderRequest request) {
        // 1. Récupérer toutes les leçons du cours en une seule requête
        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
        
        // 2. Créer une Map pour un accès rapide aux leçons par leur ID
        Map<Long, Lesson> lessonMap = lessons.stream()
            .collect(Collectors.toMap(Lesson::getId, lesson -> lesson));
            
        // 3. Valider que toutes les leçons envoyées appartiennent bien au cours
        if (!lessonMap.keySet().containsAll(request.getLessonIds()) || lessonMap.size() != request.getLessonIds().size()) {
            throw new IllegalArgumentException("La liste des leçons fournie ne correspond pas aux leçons du cours.");
        }

        // 4. Mettre à jour l'ordre en une seule boucle
        int order = 1;
        for (Long lessonId : request.getLessonIds()) {
            Lesson lesson = lessonMap.get(lessonId);
            lesson.setLessonOrder(order++);
        }
        
        // 5. La transaction se chargera de persister les changements à la fin de la méthode
        // pas besoin de save explicite car les entités sont managées.
    }

      /**
     * Supprime une leçon et toutes ses ressources associées.
     * @param lessonId L'ID de la leçon à supprimer.
     */
    @Transactional
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Leçon non trouvée avec l'ID: " + lessonId));

        // 1. Supprimer les fichiers Cloudinary associés à la leçon
        if (lesson.getResources() != null) {
            for (Resource resource : lesson.getResources()) {
                if (resource.getPublicId() != null) {
                    cloudinaryService.deleteFile(resource.getPublicId());
                }
            }
        }

        // 2. Supprimer l'entité Leçon
        // `cascade = CascadeType.ALL` sur `Lesson.resources` et `Lesson.quiz`
        // s'occupera de supprimer les enfants.
        lessonRepository.delete(lesson);
    }
}