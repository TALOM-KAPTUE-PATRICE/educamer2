package com.kaptue.educamer.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class JwtResponse {
    private String token;
    private Long id;
    private String name;
    private String email;
    private String role;
    private String avatarUrl; // <-- AJOUTER
}