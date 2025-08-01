package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.CourseFeedbackRequest;
import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.*;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseFeedbackService {

    @Autowired private CourseFeedbackRepository feedbackRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private StudentRepository studentRepository;

    @Transactional
    public void submitFeedback(Long courseId, Long studentId, CourseFeedbackRequest request) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé"));
        
        // Vérifier si l'élève a déjà donné un feedback pour ce cours
        if(feedbackRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalStateException("Vous avez déjà évalué ce cours.");
        }

        CourseFeedback feedback = new CourseFeedback();
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setCourse(course);
        feedback.setStudent(student);
        
        feedbackRepository.save(feedback);
    }
}