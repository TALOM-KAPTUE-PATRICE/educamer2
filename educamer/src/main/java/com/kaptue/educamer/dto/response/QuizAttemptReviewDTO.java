package com.kaptue.educamer.dto.response;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter @Setter
public class QuizAttemptReviewDTO extends QuizAttemptResultDTO {
    // Clé: ID de la question, Valeur: ID de la réponse correcte
    private Map<Long, Long> correctAnswers;

    public QuizAttemptReviewDTO(Long attemptId, int score, int total, double percent, String msg, Map<Long, Long> correct) {
        super(attemptId, score, total, percent, msg);
        this.correctAnswers = correct;
    }
}