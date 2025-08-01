package com.kaptue.educamer.dto.response;

import com.kaptue.educamer.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserManagementDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String avatarUrl;
    private boolean enabled; // <-- AJOUTER

    public static UserManagementDTO fromEntity(User user) {
        UserManagementDTO dto = new UserManagementDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled()); // <-- AJOUTER
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRole(user.getClass().getSimpleName().toUpperCase());
        return dto;
    }
}