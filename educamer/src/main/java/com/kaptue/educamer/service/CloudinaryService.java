package com.kaptue.educamer.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.List;




@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Uploade un fichier brut (PDF, DOCX, etc.) qui sera privé par défaut.
     * C'est la méthode que vous utilisez déjà.
     */
    
    public FileUploadResponse uploadRawFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Le fichier à uploader ne peut pas être vide.");
        }
        try {
            String publicId = folder + "/" + UUID.randomUUID().toString();
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", publicId,
                    "resource_type", "raw" // Spécifie que c'est un fichier brut
            ));
            return new FileUploadResponse(
                    (String) uploadResult.get("public_id"),
                    (String) uploadResult.get("secure_url")
            );

            
        } catch (IOException e) {
            throw new FileStorageException("Impossible d'uploader le fichier. Erreur: " + e.getMessage(), e);
        }
    }


    /**
     * Uploade une image. Les images sont publiques par défaut et optimisées. On
     * peut appliquer une transformation à l'upload (ex: créer un thumbnail).
     */
    public FileUploadResponse uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("L'image à uploader ne peut pas être vide.");
        }
        try {
            String publicId = folder + "/" + UUID.randomUUID().toString();
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", publicId,
                    "resource_type", "image", // Spécifie que c'est une image
                    "overwrite", true,
                    // Optionnel : crée une version optimisée à l'upload
                    "eager", List.of(new Transformation<>()
                            .width(500)
                            .height(500)
                            .crop("limit")
                            .quality("auto"))
            ));
            return new FileUploadResponse(
                    (String) uploadResult.get("public_id"),
                    (String) uploadResult.get("secure_url")
            );
        } catch (IOException e) {
            throw new FileStorageException("Impossible d'uploader l'image. Erreur: " + e.getMessage(), e);
        }
    }

    /**
     * Uploade une vidéo. Les vidéos sont publiques par défaut. Le traitement
     * peut être long, Cloudinary le fait en arrière-plan.
     */
    public FileUploadResponse uploadVideo(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("La vidéo à uploader ne peut pas être vide.");
        }
        try {
            String publicId = folder + "/" + UUID.randomUUID().toString();
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", publicId,
                    "resource_type", "video" // Spécifie que c'est une vidéo
            ));
            return new FileUploadResponse(
                    (String) uploadResult.get("public_id"),
                    (String) uploadResult.get("secure_url")
            );
        } catch (IOException e) {
            throw new FileStorageException("Impossible d'uploader la vidéo. Erreur: " + e.getMessage(), e);
        }
    }

    /**
     * Génère une URL signée pour accéder à une ressource PRIVÉE (comme les
     * PDF).
     */
    public String generateSignedUrl(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            throw new IllegalArgumentException("Le public_id ne peut pas être vide.");
        }
        try {
            return cloudinary.url()
                    .resourceType("raw")
                    .secure(true)
                    .signed(true)
                    .generate(publicId);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de générer l'URL d'accès au fichier.", e);
        }
    }

    /**
     * Supprime un fichier de Cloudinary en utilisant son public_id.
     *
     * @param publicId L'identifiant public du fichier à supprimer.
     */
    public void deleteFile(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return; // Ne rien faire si l'ID est vide
        }
        try {
            // "resource_type" auto n'est pas supporté pour la suppression,
            // mais Cloudinary peut souvent le déduire du public_id.
            // Pour être sûr, on pourrait stocker le resource_type avec le public_id.
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            // On ne lance pas d'exception bloquante, on logue simplement l'erreur.
            // L'échec de la suppression d'un fichier ne devrait pas faire échouer une transaction plus importante.
            System.err.println("Échec de la suppression du fichier sur Cloudinary: " + publicId + ". Erreur: " + e.getMessage());
        }
    }

}
