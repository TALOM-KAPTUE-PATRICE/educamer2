package com.kaptue.educamer.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kaptue.educamer.dto.request.AnswerRequest;
import com.kaptue.educamer.dto.request.QuestionRequest;
import com.kaptue.educamer.dto.request.QuizRequest;
import com.kaptue.educamer.entity.Answer;
import com.kaptue.educamer.entity.Lesson;
import com.kaptue.educamer.entity.Question;
import com.kaptue.educamer.entity.Quiz;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.repository.AnswerRepository;
import com.kaptue.educamer.repository.LessonRepository;
import com.kaptue.educamer.repository.QuestionRepository;
import com.kaptue.educamer.repository.QuizRepository;
import java.util.Map;
import java.util.Objects;

@Service
public class QuizManagementService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public Lesson createOrUpdateQuizForLesson(Long lessonId, QuizRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson non trouve"));
        Quiz quiz = lesson.getQuiz() != null ? lesson.getQuiz() : new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setLesson(lesson);

        if (request.getQuestions() != null) {
            // Supprimer les anciennes questions
            if (!quiz.getQuestions().isEmpty()) {
                questionRepository.deleteAll(quiz.getQuestions());
                quiz.getQuestions().clear(); // Vider la collection en mémoire
            }

            for (QuestionRequest qRequest : request.getQuestions()) {
                Question question = new Question();
                question.setText(qRequest.getText());
                question.setQuiz(quiz);
                // question.setAnswers(new HashSet<>()); // Plus nécessaire car initialisé dans l'entité

                for (AnswerRequest aRequest : qRequest.getAnswers()) {
                    Answer answer = new Answer();
                    answer.setText(aRequest.getText());
                    answer.setCorrect(aRequest.isCorrect());
                    answer.setQuestion(question);
                    question.getAnswers().add(answer); // Fonctionne maintenant car getAnswers() n'est jamais null
                }
                quiz.getQuestions().add(question);
            }
        }

        quizRepository.save(quiz);
        return lessonRepository.findById(lessonId).get();
    }

    private void updateQuestionsAndAnswers(Quiz quiz, List<QuestionRequest> questionRequests) {
        // Stratégie de mise à jour : synchroniser la DB avec la requête
        Map<Long, Question> existingQuestionsMap = quiz.getQuestions() != null
                ? quiz.getQuestions().stream().collect(Collectors.toMap(Question::getId, Function.identity()))
                : Collections.emptyMap();

        Set<Long> requestQuestionIds = questionRequests.stream()
                .map(QuestionRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 1. Supprimer les questions qui ne sont plus dans la requête
        Set<Question> questionsToDelete = existingQuestionsMap.values().stream()
                .filter(q -> !requestQuestionIds.contains(q.getId()))
                .collect(Collectors.toSet());
        if (!questionsToDelete.isEmpty()) {
            questionRepository.deleteAll(questionsToDelete);
            quiz.getQuestions().removeAll(questionsToDelete);
        }

        // 2. Mettre à jour les questions existantes et ajouter les nouvelles
        for (QuestionRequest qRequest : questionRequests) {
            Question question = (qRequest.getId() != null)
                    ? existingQuestionsMap.get(qRequest.getId())
                    : new Question();

            if (question == null) {
                question = new Question(); // Nouvelle question
            }
            question.setText(qRequest.getText());
            question.setQuiz(quiz);

            // Synchroniser les réponses
            updateAnswers(question, qRequest.getAnswers());

            quiz.getQuestions().add(question);
        }
    }

    private void updateAnswers(Question question, List<AnswerRequest> answerRequests) {
        Map<Long, Answer> existingAnswersMap = question.getAnswers() != null
                ? question.getAnswers().stream().collect(Collectors.toMap(Answer::getId, Function.identity()))
                : Collections.emptyMap();

        Set<Long> requestAnswerIds = answerRequests.stream()
                .map(AnswerRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 1. Supprimer les réponses
        Set<Answer> answersToDelete = existingAnswersMap.values().stream()
                .filter(a -> !requestAnswerIds.contains(a.getId()))
                .collect(Collectors.toSet());
        if (!answersToDelete.isEmpty()) {
            answerRepository.deleteAll(answersToDelete);
            question.getAnswers().removeAll(answersToDelete);
        }

        // 2. Mettre à jour et ajouter les réponses
        for (AnswerRequest aRequest : answerRequests) {
            Answer answer = (aRequest.getId() != null)
                    ? existingAnswersMap.get(aRequest.getId())
                    : new Answer();

            if (answer == null) {
                answer = new Answer();
            }

            answer.setText(aRequest.getText());
            answer.setCorrect(aRequest.isCorrect());
            answer.setQuestion(question);
            question.getAnswers().add(answer);
        }
    }
}
