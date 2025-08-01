package com.kaptue.educamer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception personnalisée pour représenter les erreurs survenant lors de la communication
 * avec une API externe (comme Gemini, Cloudinary, etc.).
 *
 * L'annotation @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) indique à Spring
 * de retourner un code HTTP 503 par défaut si cette exception n'est pas
 * gérée par un @ExceptionHandler spécifique.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ExternalApiException extends RuntimeException {

    /**
     * Constructeur avec un message d'erreur simple.
     * @param message Le message décrivant l'erreur.
     */
    public ExternalApiException(String message) {
        super(message);
    }

    /**
     * Constructeur qui encapsule une autre exception (la cause racine).
     * C'est utile pour le débogage, car on conserve la trace de l'erreur originale.
     * @param message Le message décrivant l'erreur.
     * @param cause L'exception originale qui a causé ce problème.
     */
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}