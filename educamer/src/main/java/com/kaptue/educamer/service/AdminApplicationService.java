package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.response.InstructorApplicationDTO;
import com.kaptue.educamer.entity.Instructor;
import com.kaptue.educamer.entity.PedagogicalApplication;
import com.kaptue.educamer.entity.Tutor;
import com.kaptue.educamer.entity.enums.ApplicationRole;
import com.kaptue.educamer.entity.enums.ApplicationStatus;
import com.kaptue.educamer.repository.InstructorApplicationRepository;
import com.kaptue.educamer.repository.InstructorRepository;
import com.kaptue.educamer.repository.TutorRepository;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AdminApplicationService {


    private static final Logger logger = LoggerFactory.getLogger(AdminApplicationService.class);

    @Autowired private InstructorApplicationRepository applicationRepository;
    @Autowired private InstructorRepository instructorRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;
    @Autowired private TutorRepository tutorRepository;

    @Transactional(readOnly = true)
    public List<InstructorApplicationDTO> getPendingApplications() {
        return applicationRepository.findByStatus(ApplicationStatus.PENDING).stream()
            .map(InstructorApplicationDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional
    public void approveApplication(Long applicationId) {
        PedagogicalApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Candidature non trouvée"));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Cette candidature a déjà été traitée.");
        }

        String tempPassword = RandomStringUtils.randomAlphanumeric(10);
        
        // --- LOGIQUE DE CRÉATION BASÉE SUR LE RÔLE ---
        if (ApplicationRole.INSTRUCTOR.equals(application.getDesiredRole())) {
            createInstructorAccount(application, tempPassword);
        } else if (ApplicationRole.TUTOR.equals(application.getDesiredRole())) {
            createTutorAccount(application, tempPassword);
        } else {
            throw new IllegalStateException("Rôle de candidature non supporté : " + application.getDesiredRole());
        }

        application.setStatus(ApplicationStatus.APPROVED);
        applicationRepository.save(application);
        
        // Envoyer l'email de bienvenue
        emailService.sendWelcomeEmail(application.getEmail(), application.getName(), tempPassword, application.getDesiredRole().name());
    }

    private void createInstructorAccount(PedagogicalApplication app, String password) {
        Instructor newInstructor = new Instructor();
        newInstructor.setName(app.getName());
        newInstructor.setEmail(app.getEmail());
        newInstructor.setPassword(passwordEncoder.encode(password));
        newInstructor.setBiography(app.getMotivation());
        instructorRepository.save(newInstructor);
        logPassword(app.getEmail(), password);
    }
    
    private void createTutorAccount(PedagogicalApplication app, String password) {
        Tutor newTutor = new Tutor();
        newTutor.setName(app.getName());
        newTutor.setEmail(app.getEmail());
        newTutor.setPassword(passwordEncoder.encode(password));
        newTutor.setSpecializations(app.getSpecializations()); // Le champ "specializations" est plus pertinent pour un tuteur
        tutorRepository.save(newTutor);
        logPassword(app.getEmail(), password);
    }
    
    private void logPassword(String email, String password) {
        logger.info("******************************************************************");
        logger.info("Génération du compte pour : {}", email);
        logger.info("Mot de passe temporaire (en clair) : {}", password);
        logger.info("******************************************************************");
    }

    @Transactional
    public void rejectApplication(Long applicationId, String reason) {
        PedagogicalApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Candidature non trouvée"));
            
        application.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(application);
        
        // Optionnel: envoyer un email de rejet au candidat
         emailService.sendApplicationRejectionEmail(application.getEmail(), application.getName(), reason);
    }

    @Transactional(readOnly = true)
    public InstructorApplicationDTO getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
            .map(InstructorApplicationDTO::fromEntity)
            .orElseThrow(() -> new ResourceNotFoundException("Candidature non trouvée avec l'ID: " + applicationId));
    }
    

    
}