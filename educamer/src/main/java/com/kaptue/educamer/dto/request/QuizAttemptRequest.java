package com.kaptue.educamer.dto.request;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class QuizAttemptRequest {
    // Clé: ID de la question, Valeur: ID de la réponse choisie
    private Map<Long, Long> answers;
}