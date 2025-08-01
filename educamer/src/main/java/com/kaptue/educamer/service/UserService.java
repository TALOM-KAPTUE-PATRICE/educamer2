package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.UpdateProfileRequest;
import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.dto.response.UserManagementDTO;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.repository.UserRepository;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;



@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private CloudinaryService cloudinaryService;

    @Transactional
    public UserManagementDTO updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        user.setName(request.getName());
        // user.setUpdatedAt(LocalDateTime.now()); // Si vous avez un champ updatedAt
        return UserManagementDTO.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserManagementDTO updateUserAvatar(Long userId, MultipartFile avatarFile) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        // --- Logique d'optimisation ---
        // 1. Si un ancien avatar existe, on le supprime de Cloudinary pour économiser de l'espace
        if (user.getAvatarPublicId() != null && !user.getAvatarPublicId().isBlank()) {
            cloudinaryService.deleteFile(user.getAvatarPublicId());
        }

        // 2. On uploade la nouvelle image
        FileUploadResponse response = cloudinaryService.uploadImage(avatarFile, "avatars");
        
        // 3. On met à jour l'entité User avec les nouvelles informations
        user.setAvatarUrl(response.getSecureUrl());
        user.setAvatarPublicId(response.getPublicId()); 
        
        // 4. On sauvegarde et on retourne le DTO mis à jour
        return UserManagementDTO.fromEntity(userRepository.save(user));
    }
}