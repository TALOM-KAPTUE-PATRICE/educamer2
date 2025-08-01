package com.kaptue.educamer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception personnalisée pour représenter les erreurs de type "Bad Request" (400).
 *
 * L'annotation @ResponseStatus(HttpStatus.BAD_REQUEST) indique à Spring MVC
 * que si cette exception n'est pas interceptée par un @ExceptionHandler,
 * il doit automatiquement retourner une réponse HTTP avec le statut 400.
 *
 * Utiliser cette exception rend le code des contrôleurs plus propre car il
 * permet de signaler une erreur client invalide sans avoir à construire
 * manuellement une ResponseEntity.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructeur avec un message d'erreur simple.
     *
     * @param message Le message détaillant la cause de l'erreur.
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un message d'erreur et une cause (exception d'origine).
     * Utile pour encapsuler une autre exception (par exemple, une JsonProcessingException)
     * tout en fournissant un message plus clair à l'utilisateur.
     *
     * @param message Le message détaillant la cause de l'erreur.
     * @param cause   L'exception originale qui a provoqué cette erreur.
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}