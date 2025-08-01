package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.UpdateProfileRequest;
import com.kaptue.educamer.dto.response.UserManagementDTO;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.security.CurrentUser;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.kaptue.educamer.service.TokenBlacklistService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()") // Tous les utilisateurs authentifiés
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired 
    private TokenBlacklistService tokenBlacklistService;



    // Endpoint pour que l'utilisateur récupère ses propres informations
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('" + Permission.PROFILE_READ + "')")
    public ResponseEntity<UserManagementDTO> getMyProfile(@CurrentUser User currentUser) {
        return ResponseEntity.ok(UserManagementDTO.fromEntity(currentUser));
    }

    // Endpoint pour mettre à jour son propre profil
    @PutMapping("/me/profile")
    // @PreAuthorize("hasAuthority('" + Permission.PROFILE_UPDATE + "')")
    public ResponseEntity<UserManagementDTO> updateMyProfile(@CurrentUser User currentUser, @Valid @RequestBody UpdateProfileRequest request) {
        UserManagementDTO updatedUser = userService.updateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(updatedUser);
    }

    // Endpoint pour mettre à jour son propre avatar
    @PostMapping("/me/avatar")
    @PreAuthorize("hasAuthority('" + Permission.PROFILE_UPDATE + "')")
    public ResponseEntity<UserManagementDTO> updateMyAvatar(@CurrentUser User currentUser, @RequestParam("file") MultipartFile avatarFile) {
        UserManagementDTO updatedUser = userService.updateUserAvatar(currentUser.getId(), avatarFile);
        return ResponseEntity.ok(updatedUser);
    }





    // Les notifications seraient dans un NotificationController
    // ex: GET /api/notifications
}
