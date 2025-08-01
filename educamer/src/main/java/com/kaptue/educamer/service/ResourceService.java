package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.ResourceRequest;
import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.entity.Lesson;
import com.kaptue.educamer.entity.Resource;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.repository.LessonRepository;
import com.kaptue.educamer.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CloudinaryService cloudinaryService; // J'ai renommé pour correspondre à votre fichier

    @Transactional
    public Lesson addResourceToLesson(Long lessonId, ResourceRequest request, MultipartFile file) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Leçon non trouvée avec l'ID: " + lessonId));

        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setType(request.getType());
        resource.setLesson(lesson);

        // Dossier de stockage sur Cloudinary
        String folder = "courses/" + lesson.getCourse().getId() + "/lessons/" + lessonId + "/resources";

        FileUploadResponse uploadResponse;

        // On utilise un switch pour appeler la bonne méthode de CloudinaryService
        switch (request.getType()) {
            case LINK:
                if (request.getUrl() == null || request.getUrl().isBlank()) {
                    throw new IllegalArgumentException("Une URL est requise pour le type de ressource LINK.");
                }
                resource.setUrl(request.getUrl());
                break;

            case IMAGE:
                if (file == null || file.isEmpty()) {
                    throw new IllegalArgumentException("Un fichier image est requis.");
                }
                uploadResponse = cloudinaryService.uploadImage(file, folder);
                resource.setUrl(uploadResponse.getSecureUrl());
                resource.setPublicId(uploadResponse.getPublicId());
                break;

            case VIDEO:
                if (file == null || file.isEmpty()) {
                    throw new IllegalArgumentException("Un fichier vidéo est requis.");
                }
                uploadResponse = cloudinaryService.uploadVideo(file, folder);
                resource.setUrl(uploadResponse.getSecureUrl());
                resource.setPublicId(uploadResponse.getPublicId());
                break;

            case PDF:
                if (file == null || file.isEmpty()) {
                    throw new IllegalArgumentException("Un fichier PDF est requis.");
                }
                // Pour les PDF, on les traite comme des fichiers "bruts" et privés
                uploadResponse = cloudinaryService.uploadRawFile(file, folder);
                // On NE stocke PAS l'URL directe car elle est privée.
                // On stocke le publicId pour générer une URL signée à la demande.
                resource.setPublicId(uploadResponse.getPublicId());
                // L'URL pourrait être un placeholder ou une URL interne de notre API
                // qui générera le lien signé. ex: /api/resources/{id}/download

                resource.setPrivate(true); // <-- MARQUER COMME PRIVÉ
                break;

            default:
                throw new IllegalArgumentException("Type de ressource non supporté: " + request.getType());
        }

        Resource savedResource = resourceRepository.save(resource);

        // 2. Si la ressource est privée, on construit l'URL avec l'ID maintenant disponible et on met à jour
        if (savedResource.isPrivate()) {
            savedResource.setUrl("/api/files/resource/" + savedResource.getId() + "/access");
            resourceRepository.save(savedResource); // Mise à jour de l'entité avec l'URL correcte
        }

        return lessonRepository.findById(lessonId).get();
    }

    /**
     * Supprime une ressource, y compris le fichier associé sur Cloudinary.
     */
    @Transactional
    public void deleteResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource non trouvée"));

        // Si la ressource a un publicId, on la supprime de Cloudinary
        if (resource.getPublicId() != null && !resource.getPublicId().isBlank()) {
            cloudinaryService.deleteFile(resource.getPublicId());
        }

        // On supprime l'entrée de notre base de données
        resourceRepository.delete(resource);
    }
}
