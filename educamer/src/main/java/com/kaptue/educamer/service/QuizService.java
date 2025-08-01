package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.QuizAttemptRequest;
import com.kaptue.educamer.dto.response.QuizAttemptResultDTO;
import com.kaptue.educamer.dto.response.QuizAttemptReviewDTO;
import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.*;
import com.kaptue.educamer.exception.ResourceNotFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuizService {

    @Autowired private QuizRepository quizRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private QuizAttemptRepository quizAttemptRepository;
    @Autowired private AnswerRepository answerRepository;
    @Autowired private ProgressTrackingService progressTrackingService; // <-- INJECTER LE SERVICE

    // Définir un seuil de réussite (en pourcentage)
    private static final double PASSING_PERCENTAGE = 50.0;

    @Transactional
    public QuizAttemptReviewDTO submitQuizAttempt(Long quizId, Long studentId, QuizAttemptRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz non trouvé"));
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé"));

        // Empêcher de refaire un quiz si la leçon associée est déjà complétée
        // (Logique métier à discuter : autorise-t-on plusieurs tentatives ?)
        if (quiz.getLesson() != null && progressTrackingService.isLessonCompleted(studentId, quiz.getLesson().getId())) {
            // Vous pourriez retourner le résultat précédent ou lancer une exception.
            throw new IllegalStateException("Vous avez déjà validé la leçon associée à ce quiz.");
        }

        int score = 0;
        int totalQuestions = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;

        if (totalQuestions > 0) {
            for (Question question : quiz.getQuestions()) {
                Long selectedAnswerId = request.getAnswers().get(question.getId());
                if (selectedAnswerId != null) {
                    // Vérifier que la réponse appartient bien à la question pour la sécurité
                    Answer selectedAnswer = answerRepository.findByIdAndQuestion_Id(selectedAnswerId, question.getId())
                        .orElse(null);
                    if (selectedAnswer != null && selectedAnswer.isCorrect()) {
                        score++;
                    }
                }
            }
        } 
        
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setScore(score);
        attempt.setTotalQuestions(totalQuestions);

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);
        double percentage = (totalQuestions > 0) ? ((double) score / totalQuestions) * 100.0 : 0.0;
        
        if (percentage >= PASSING_PERCENTAGE && quiz.getLesson() != null) {
            progressTrackingService.markLessonAsCompleted(studentId, quiz.getLesson().getId());
        }

        // Construire la map des réponses correctes
        Map<Long, Long> correctAnswersMap = quiz.getQuestions().stream()
            .collect(Collectors.toMap(
                Question::getId,
                q -> q.getAnswers().stream().filter(Answer::isCorrect).findFirst().get().getId()
            ));

        String message = percentage >= PASSING_PERCENTAGE ? "Félicitations, vous avez réussi !" : "Vous pouvez faire mieux, révisez la leçon !";

        return new QuizAttemptReviewDTO(savedAttempt.getId(), score, totalQuestions, Math.round(percentage), message, correctAnswersMap);
    }
}