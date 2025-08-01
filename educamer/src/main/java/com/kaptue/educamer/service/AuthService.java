package com.kaptue.educamer.service;
// ... imports ...

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.exception.ResourceNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaptue.educamer.dto.request.ForgotPasswordRequest;
import com.kaptue.educamer.dto.request.LoginRequest;
import com.kaptue.educamer.dto.request.ResetPasswordRequest;
import com.kaptue.educamer.dto.request.SignUpRequest;
import com.kaptue.educamer.dto.response.JwtResponse;
import com.kaptue.educamer.entity.Instructor;
import com.kaptue.educamer.entity.Parent;
import com.kaptue.educamer.entity.Student;
import com.kaptue.educamer.entity.Admin;
import com.kaptue.educamer.entity.Tutor;
import com.kaptue.educamer.jwt.JwtUtil;
import com.kaptue.educamer.repository.UserRepository;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private TokenBlacklistService tokenBlacklistService; // Assurez-vous d'injecter ce service
    @Autowired private EmailService emailService;
    @Autowired private PermissionMappingService permissionMappingService;

    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Erreur: Utilisateur non trouvé après authentification."));
        
        Set<String> permissions = permissionMappingService.getPermissionsForUser(user);
        String role = user.getClass().getSimpleName().toUpperCase();
        
        String jwt = jwtUtil.generateToken(userDetails, role, permissions);
        
        return new JwtResponse(jwt, user.getId(), user.getName(), user.getEmail(), role, user.getAvatarUrl());
    }

    
    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Erreur: Cet email est déjà utilisé !");
        }

        User user;
        String role = signUpRequest.getRole().toUpperCase();

        if ("ELEVE".equals(role)) {
            user = new Student();
        }else if ("INSTRUCTEUR".equals(role)) {
            user = new Instructor();
        } else if("PARENT".equals((role))){
             user = new Parent();
        }else if("TUTEUR".equals(role)){
            user = new Tutor();
        }else if("ADMIN".equals(role)){
            user = new Admin();
        }else {
            throw new RuntimeException("Erreur: Rôle non valide spécifié.");
        }
        
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        
        return userRepository.save(user);
    }
    
    
    @Transactional
    public void handleForgotPassword(ForgotPasswordRequest request) {
        // On cherche l'utilisateur. Si on ne le trouve pas, on ne fait rien
        // pour ne pas révéler si un email existe ou non (sécurité).
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            String token = jwtUtil.generatePasswordResetToken(user.getEmail());
            emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), token);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (jwtUtil.isTokenExpired(request.getToken()) || !jwtUtil.isPasswordResetToken(request.getToken())) {
            throw new IllegalArgumentException("Le token est invalide ou a expiré.");
        }

        // NOUVEAU: Vérifier si le token est déjà blacklisté
        if (tokenBlacklistService.isTokenBlacklisted(request.getToken())) {
            throw new IllegalArgumentException("Ce lien de réinitialisation a déjà été utilisé.");
        }
        
        String email = jwtUtil.extractUsername(request.getToken());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le token fourni."));
            
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        // Optionnel mais recommandé : blacklister le token de reset pour qu'il ne soit pas réutilisé.
        // On blackliste le token APRÈS que le mot de passe a été changé avec succès
        tokenBlacklistService.blacklistToken(request.getToken());
    }

    /**
     * Valide un token de réinitialisation de mot de passe.
     * Vérifie l'expiration, le type de token, et s'il n'est pas blacklisté.
     * Ne lève pas d'exception si l'utilisateur n'est pas trouvé pour des raisons de sécurité.
     *
     * @param token Le token JWT à valider.
     * @return true si le token est valide, false sinon.
     */
    public boolean validateResetToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                return false;
            }
            // Vérifie l'expiration, le type, et la blacklist.
            return !jwtUtil.isTokenExpired(token)
                   && jwtUtil.isPasswordResetToken(token)
                   && !tokenBlacklistService.isTokenBlacklisted(token);
        } catch (Exception e) {
            // Toute exception de parsing signifie que le token est invalide.
            return false;
        }
    }

}