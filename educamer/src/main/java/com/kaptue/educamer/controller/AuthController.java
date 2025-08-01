package com.kaptue.educamer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kaptue.educamer.dto.request.ForgotPasswordRequest;
import com.kaptue.educamer.dto.request.LoginRequest;
import com.kaptue.educamer.dto.request.ResetPasswordRequest;
import com.kaptue.educamer.dto.request.SignUpRequest;
import com.kaptue.educamer.dto.response.JwtResponse;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.service.AuthService;
import com.kaptue.educamer.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired private TokenBlacklistService tokenBlacklistService; // Assurez-vous d'avoir ça

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur enregistré avec succès !");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.handleForgotPassword(request);
        // On renvoie toujours un message de succès pour la sécurité.
        return ResponseEntity.ok("Si un compte existe avec cet email, un lien de réinitialisation a été envoyé.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok("Votre mot de passe a été réinitialisé avec succès.");
        } catch (IllegalArgumentException e) {
            // C'est une erreur "attendue" (token invalide, utilisé, etc.)
            // On renvoie un message générique pour ne pas donner d'indices à un attaquant.
            return ResponseEntity.badRequest().body("Le lien de réinitialisation est invalide ou a expiré.");
        } catch (ResourceNotFoundException e) {
            // L'utilisateur n'existe pas. On renvoie le même message générique.
            return ResponseEntity.badRequest().body("Le lien de réinitialisation est invalide ou a expiré.");
        }

    }

    /**
     * Endpoint public pour vérifier la validité d'un token de réinitialisation
     * AVANT d'afficher le formulaire.
     *
     * @param token Le token à vérifier.
     * @return Un statut 200 OK si valide, 400 Bad Request si invalide.
     */
    @GetMapping("/validate-reset-token")
    public ResponseEntity<Void> validateResetToken(@RequestParam("token") String token) {
        if (authService.validateResetToken(token)) {
            return ResponseEntity.ok().build(); // 200 OK
        } else {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    /**
     * Déconnecte l'utilisateur en ajoutant son token JWT actuel à la blacklist.
     * Cet endpoint est sécurisé et ne peut être appelé que par un utilisateur
     * authentifié.
     */
    
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String jwt = extractJwtFromRequest(request);
        if (jwt != null) {
            tokenBlacklistService.blacklistToken(jwt);
        }
        return ResponseEntity.ok("Déconnexion réussie. Le token a été invalidé.");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
