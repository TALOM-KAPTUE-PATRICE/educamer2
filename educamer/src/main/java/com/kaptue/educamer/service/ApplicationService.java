package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.request.ApplicationRequest;
import com.kaptue.educamer.dto.response.FileUploadResponse;
import com.kaptue.educamer.entity.PedagogicalApplication;
import com.kaptue.educamer.repository.InstructorApplicationRepository;
import com.kaptue.educamer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;


@Service
public class ApplicationService {

    @Value("${admin.bootstrap.email}") // <-- Injecter l'email de l'admin
    private String adminEmail;

    @Autowired private InstructorApplicationRepository applicationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CloudinaryService cloudinaryService ;
    @Autowired private EmailService emailService; // Un service pour envoyer des emails

    @Transactional
    public void submitInstructorApplication(ApplicationRequest request) {
        // 1. Vérifier si un utilisateur avec cet email n'existe pas déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Un compte avec cet email existe déjà. Veuillez vous connecter.");
        }

        // 2. Uploader le CV sur Cloudinary
        FileUploadResponse uploadResponse = cloudinaryService.uploadRawFile(request.getResume(), "instructor_resumes");

        // 3. Créer et sauvegarder l'entité de candidature
        PedagogicalApplication application = new PedagogicalApplication();
        application.setName(request.getName());
        application.setEmail(request.getEmail());
        application.setPhone(request.getPhone());
        application.setSpecializations(request.getSpecializations());
        application.setMotivation(request.getMotivation());
        application.setResumeUrl(uploadResponse.getSecureUrl());
        application.setResumePublicId(uploadResponse.getPublicId());
        application.setDesiredRole(request.getDesiredRole()); // Mapper le nouveau champ
        // Le statut est PENDING par défaut

        applicationRepository.save(application);

        // 4. Envoyer un email de notification à l'admin
        // L'email de l'admin pourrait être dans les application.properties
       
        String subject = "Nouvelle candidature d'instructeur sur EduCamer";
        String text = "Une nouvelle candidature a été soumise par " + request.getName() + " (" + request.getEmail() + "). Veuillez la consulter dans votre panel d'administration.";
        emailService.sendSimpleMessage(adminEmail, subject, text);
    }
}