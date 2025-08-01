package com.kaptue.educamer.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Gestionnaire global des exceptions pour l'application.
 * Capture les exceptions spécifiques et retourne des réponses HTTP structurées.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les exceptions de type ResourceNotFoundException.
     * @return ResponseEntity avec un statut 404 NOT_FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource non trouvée: {} pour la requête {}", ex.getMessage(), request.getDescription(false));
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les exceptions de type DuplicateResourceException.
     * @return ResponseEntity avec un statut 409 CONFLICT.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        logger.warn("Conflit de ressource: {} pour la requête {}", ex.getMessage(), request.getDescription(false));
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * Gère les exceptions de validation des arguments de méthode (ex: @Valid sur @RequestBody).
     * @return ResponseEntity avec un statut 400 BAD_REQUEST et les détails des erreurs de validation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Erreur de validation: {} pour la requête {}", ex.getMessage(), request.getDescription(false));
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erreur de validation");
        body.put("message", "Un ou plusieurs champs sont invalides.");
        body.put("errors", errors);
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions de type AccessDeniedException (Spring Security).
     * @return ResponseEntity avec un statut 403 FORBIDDEN.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        logger.warn("Accès refusé: {} pour la requête {}", ex.getMessage(), request.getDescription(false));
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Accès refusé. Vous n'avez pas les permissions nécessaires.", request.getDescription(false), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }


    /**
     * Gère les exceptions de type BadCredentialsException (Spring Security).
     * @return ResponseEntity avec un statut 401 UNAUTHORIZED.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        logger.warn("Identifiants invalides: {} pour la requête {}", ex.getMessage(), request.getDescription(false));
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Identifiants invalides.", request.getDescription(false), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gère les exceptions de type FileStorageException.
     * @return ResponseEntity avec un statut 500 INTERNAL_SERVER_ERROR ou 400 BAD_REQUEST.
     */
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorDetails> handleFileStorageException(FileStorageException ex, WebRequest request) {
        logger.error("Erreur de stockage de fichier: {} pour la requête {}", ex.getMessage(), request.getDescription(false), ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.value());
        // Vous pourriez vouloir retourner BAD_REQUEST si l'erreur est due à un fichier invalide de l'utilisateur
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère spécifiquement les erreurs de communication avec les API externes.
     * Renvoie un statut 503 (Service Unavailable) pour indiquer que le problème
     * ne vient pas de notre application, mais d'un service tiers.
     */
    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorDetails> handleExternalApiException(ExternalApiException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(),
            "Le service externe est temporairement indisponible.",
            ex.getMessage(), // Le message que nous avons défini dans le service (ex: "Erreur de communication avec Gemini...")
            HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Gère toutes les autres exceptions non capturées spécifiquement.
     * @return ResponseEntity avec un statut 500 INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Erreur interne du serveur: {} pour la requête {}", ex.getMessage(), request.getDescription(false), ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Une erreur interne est survenue. Veuillez réessayer plus tard.", request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}