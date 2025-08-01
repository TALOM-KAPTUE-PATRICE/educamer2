package com.kaptue.educamer.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import java.net.URI;
import java.util.Base64;

import com.kaptue.educamer.entity.Assignment;
import com.kaptue.educamer.entity.Resource; // Importer
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.repository.AssignmentRepository;
import com.kaptue.educamer.repository.ResourceRepository;
import com.kaptue.educamer.service.CloudinaryService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import com.kaptue.educamer.service.security.CourseSecurityService;
import com.kaptue.educamer.service.security.StudentCourseSecurityService;

@RestController
@RequestMapping("/api/files")
@PreAuthorize("isAuthenticated()")
public class FileController {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RestTemplate restTemplate; // Ajouter ce bean
    @Autowired
    private ResourceRepository resourceRepository; // INJECTER
    @Autowired
    private StudentCourseSecurityService studentCourseSecurityService; // INJECTER
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private CourseSecurityService courseSecurityService;
    // ...

    /**
     * Endpoint sécurisé pour accéder aux pièces jointes privées des devoirs.
     * Vérifie que l'utilisateur est inscrit au cours du devoir avant de générer
     * l'URL.
     */
    @GetMapping("/assignment/{assignmentId}/access")
    public ResponseEntity<org.springframework.core.io.Resource> getSecureAssignmentAttachment(@PathVariable Long assignmentId, Authentication authentication) {
        // 1. Récupérer le devoir
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Devoir non trouvé"));

        if (!assignment.isAttachmentIsPrivate() || assignment.getAttachmentPublicId() == null) {
            throw new IllegalStateException("Ce devoir n'a pas de pièce jointe sécurisée.");
        }

        // 2. Vérifier les droits d'accès
        Long courseId = assignment.getCourse().getId();
        boolean isEnrolled = studentCourseSecurityService.isEnrolled(authentication, courseId);
        boolean isInstructor = courseSecurityService.isInstructorOfCourse(authentication, courseId);

        if (!isEnrolled && !isInstructor) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à accéder à cette pièce jointe.");
        }
        // 3. Générer l'URL signée
        String signedUrl = cloudinaryService.generateSignedUrl(assignment.getAttachmentPublicId());

        // 4. Renvoyer la redirection
        // Utiliser le titre du devoir comme nom de fichier par défaut
        return fetchAndReturnFile(signedUrl, assignment.getTitle());
    }

    /**
     * Endpoint générique et sécurisé pour accéder aux ressources privées (ex:
     * PDF). Il vérifie que l'utilisateur (élève) est bien inscrit au cours de
     * la ressource avant de générer une URL d'accès temporaire et signée.
     *
     * @param resourceId L'ID de la ressource demandée.
     * @param authentication L'objet d'authentification de l'utilisateur.
     * @return Une redirection HTTP 302 vers l'URL signée de Cloudinary.
     */
    @GetMapping("/resource/{resourceId}/access")
    public ResponseEntity<org.springframework.core.io.Resource> getSecureResourceUrl(@PathVariable Long resourceId, Authentication authentication) {
        // 1. Récupérer la ressource et vérifier qu'elle existe et qu'elle est privée
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource non trouvée"));

        if (!resource.isPrivate() || resource.getPublicId() == null) {
            // Si la ressource est publique, on pourrait simplement rediriger vers son URL directe.
            // Pour la sécurité, on refuse l'accès si la configuration est incohérente.
            throw new IllegalStateException("Cette ressource n'est pas configurée pour un accès sécurisé.");
        }

        // 2. Vérifier les droits d'accès de l'utilisateur
        Long courseId = resource.getLesson().getCourse().getId();
        boolean isEnrolled = studentCourseSecurityService.isEnrolled(authentication, courseId);
        // On pourrait aussi ajouter une vérification pour l'instructeur
        // boolean isInstructor = courseSecurityService.isInstructorOfCourse(authentication, courseId);

        if (!isEnrolled /* && !isInstructor */) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à accéder à cette ressource.");
        }

        // 3. Si tout est bon, générer l'URL signée
        String signedUrl = cloudinaryService.generateSignedUrl(resource.getPublicId());

        // 4. Renvoyer une redirection vers l'URL temporaire
        // 3. Télécharger le contenu depuis Cloudinary et le renvoyer au client
        return fetchAndReturnFile(signedUrl, resource.getName());
    }

    private ResponseEntity<org.springframework.core.io.Resource> fetchAndReturnFile(String url, String filename) {
        // Le type de retour est ici aussi le nom complet
        org.springframework.core.io.Resource fileResource = restTemplate.getForObject(url, org.springframework.core.io.Resource.class);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(fileResource);
    }

    /**
     * Endpoint sécurisé pour accéder aux CV des candidatures. L'ID public du CV
     * est passé en base64 pour éviter les problèmes d'URL avec les slashes.
     *
     * @param encodedPublicId Le public_id du fichier, encodé en Base64.
     * @return Une redirection HTTP 302 vers l'URL signée de Cloudinary.
     */
    @GetMapping("/resume/{encodedPublicId}")
    @PreAuthorize("hasRole('ADMIN')") // Seul un admin peut voir les CV
    public ResponseEntity<Void> getResume(@PathVariable String encodedPublicId) {
        // Décoder le public_id
        String publicId = new String(Base64.getUrlDecoder().decode(encodedPublicId));

        // Générer l'URL signée
        String signedUrl = cloudinaryService.generateSignedUrl(publicId);

        // Renvoyer une redirection
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(signedUrl))
                .build();
    }

    /**
     * Endpoint pour uploader une image de cours. Accessible uniquement aux
     * instructeurs.
     */
    @PostMapping("/upload/image/course")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<FileUploadResponse> uploadCourseImage(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = cloudinaryService.uploadImage(file, "course_images");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint pour uploader une vidéo de leçon. Accessible uniquement aux
     * instructeurs.
     */
    @PostMapping("/upload/video/lesson")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<FileUploadResponse> uploadLessonVideo(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = cloudinaryService.uploadVideo(file, "lesson_videos");
        return ResponseEntity.ok(response);
    }
}
