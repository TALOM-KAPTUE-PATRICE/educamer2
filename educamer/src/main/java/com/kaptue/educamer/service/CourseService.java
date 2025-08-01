package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.CourseRequest;
import com.kaptue.educamer.dto.response.CourseCatalogDTO;
import com.kaptue.educamer.dto.response.CourseResponse;
import com.kaptue.educamer.dto.response.CourseStatisticDTO;
import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.dto.response.editor.CourseEditorDTO;
import com.kaptue.educamer.entity.Course;
import com.kaptue.educamer.entity.Instructor;
import com.kaptue.educamer.entity.Lesson;
import com.kaptue.educamer.entity.Resource;
import com.kaptue.educamer.entity.enums.CourseStatus;
import com.kaptue.educamer.repository.CourseRepository;
import com.kaptue.educamer.repository.EnrollmentRepository;
import com.kaptue.educamer.repository.InstructorRepository;
import com.kaptue.educamer.repository.SubmissionRepository;
import com.kaptue.educamer.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CloudinaryService cloudinaryService; // Le service Cloudinary

    /**
     * Crée un nouveau cours pour l'instructeur actuellement authentifié.
     */
    @Transactional
    public CourseResponse createCourse(CourseRequest request, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructeur non trouvé avec l'ID: " + instructorId));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructor(instructor);
        course.setStatus(CourseStatus.DRAFT); // Un nouveau cours est toujours un brouillon
        course.setCreatedAt(LocalDateTime.now()); // Initialiser la date de création

        Course savedCourse = courseRepository.save(course);
        return CourseResponse.fromEntity(savedCourse);
    }

    /**
     * Met à jour l'image de couverture d'un cours.
     */
    @Transactional
    public CourseResponse updateCourseImage(Long courseId, MultipartFile imageFile) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));

        FileUploadResponse response = cloudinaryService.uploadImage(imageFile, "course_covers");
        course.setImageUrl(response.getSecureUrl());
        course.setUpdatedAt(LocalDateTime.now());
        course.setCoursePublicId(response.getPublicId());
        Course updatedCourse = courseRepository.save(course);
        return CourseResponse.fromEntity(updatedCourse);
    }

    // ========================================================================
    // ===                 MÉTHODE MANQUANTE À AJOUTER                      ===
    // ========================================================================
    /**
     * Met à jour les détails textuels (titre, description) d'un cours. La
     * sécurité (vérifier que c'est bien l'instructeur du cours) est déjà gérée
     * par @PreAuthorize dans le contrôleur.
     *
     * @param courseId L'ID du cours à mettre à jour.
     * @param request Le DTO contenant les nouvelles informations.
     * @return Le DTO du cours mis à jour.
     */
    @Transactional
    public CourseResponse updateCourseDetails(Long courseId, CourseRequest request) {
        // 1. Récupérer l'entité depuis la base de données
        Course courseToUpdate = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));

        // Logique métier : On ne peut pas modifier un cours s'il est archivé, par exemple.
        // Pour l'instant, nous permettons la modification tant qu'il n'est pas supprimé.
        // 2. Mettre à jour les champs de l'entité avec les valeurs du DTO
        courseToUpdate.setTitle(request.getTitle());
        courseToUpdate.setDescription(request.getDescription());
        courseToUpdate.setUpdatedAt(LocalDateTime.now()); // Toujours mettre à jour la date de modification

        // 3. Sauvegarder l'entité mise à jour
        Course updatedCourse = courseRepository.save(courseToUpdate);

        // 4. Retourner la réponse DTO
        return CourseResponse.fromEntity(updatedCourse);
    }

    @Transactional(readOnly = true)
    public CourseStatisticDTO getCourseStatistics(Long courseId) {
        long totalEnrollments = enrollmentRepository.countByCourseId(courseId);
        Double averageProgress = enrollmentRepository.findAverageProgressByCourseId(courseId).orElse(0.0);
        Double averageGrade = submissionRepository.findAverageGradeByCourseId(courseId).orElse(0.0);
        long pendingSubmissions = submissionRepository.countByAssignment_CourseIdAndGradeIsNull(courseId);

        return new CourseStatisticDTO(
            totalEnrollments, 
            Math.round(averageProgress * 100.0) / 100.0, // Arrondi
            Math.round(averageGrade * 100.0) / 100.0,   // Arrondi
            pendingSubmissions
        );
    }

    /**
     * Récupère tous les cours créés par un instructeur spécifique.
     *
     * @param instructorId L'ID de l'instructeur.
     * @return Une liste de DTOs représentant ses cours.
     */
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(CourseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseEditorDTO getCourseForEditor(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        return CourseEditorDTO.fromEntity(course);
    }

    @Transactional
    public CourseResponse publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));

        // Logique métier : un cours ne peut être publié que s'il a au moins une leçon
        if (course.getLessons() == null || course.getLessons().isEmpty()) {
            throw new IllegalStateException("Un cours doit contenir au moins une leçon pour être publié.");
        }

        course.setStatus(CourseStatus.PUBLISHED);
        course.setUpdatedAt(LocalDateTime.now());
        return CourseResponse.fromEntity(courseRepository.save(course));
    }

    /**
     * Récupère les cours publiés pour le catalogue général.
     */
    @Transactional(readOnly = true)
    public List<CourseCatalogDTO> getPublishedCourses() {
        return courseRepository.findByStatus(CourseStatus.PUBLISHED).stream()
                .map(CourseCatalogDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les cours les plus populaires (tendance), basés sur le nombre
     * d'inscriptions.
     *
     * @param limit Le nombre de cours à retourner.
     */
    @Transactional(readOnly = true)
    public List<CourseCatalogDTO> getTrendingCourses(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return courseRepository.findTrendingCourses(CourseStatus.PUBLISHED, pageable).stream()
                .map(CourseCatalogDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les cours les mieux notés, basés sur la moyenne des feedbacks.
     *
     * @param limit Le nombre de cours à retourner.
     */
    @Transactional(readOnly = true)
    public List<CourseCatalogDTO> getTopRatedCourses(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return courseRepository.findTopRatedCourses(CourseStatus.PUBLISHED, pageable).stream()
                .map(CourseCatalogDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les cours d'une catégorie spécifique.
     */
    @Transactional(readOnly = true)
    public List<CourseCatalogDTO> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategories_IdAndStatus(categoryId, CourseStatus.PUBLISHED).stream()
                .map(CourseCatalogDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
public CourseResponse findCourseById(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));
    return CourseResponse.fromEntity(course);
}

    /**
     * Supprime un cours et toutes ses ressources associées, y compris les fichiers sur Cloudinary.
     * @param courseId L'ID du cours à supprimer.
     */
    
    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + courseId));

        // 1. Supprimer les fichiers Cloudinary associés au cours
        // a) L'image de couverture du cours
        if (course.getCoursePublicId() != null) {
            cloudinaryService.deleteFile(course.getCoursePublicId());
        }

        // b) Toutes les ressources (PDF, vidéos, images) de toutes les leçons du cours
        if (course.getLessons() != null) {
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getResources() != null) {
                    for (Resource resource : lesson.getResources()) {
                        if (resource.getPublicId() != null) {
                            cloudinaryService.deleteFile(resource.getPublicId());
                        }
                    }
                }
            }
        }
        
        // 2. Supprimer l'entité Course de la base de données
        // Grâce à `cascade = CascadeType.ALL` sur les relations (lessons, assignments, etc.),
        // JPA/Hibernate s'occupera de supprimer toutes les entités enfants en cascade.
        courseRepository.delete(course);
    }

}
