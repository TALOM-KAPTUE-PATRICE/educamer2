package com.kaptue.educamer.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.Map;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired private JavaMailSender mailSender;
    @Autowired private SpringTemplateEngine thymeleafTemplateEngine; 

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail; // Pour le champ "From"

    /**
     * Envoie un email texte simple.
     * Utilisé pour la notification à l'admin.
     */
    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8"); // false car c'est du texte
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
            logger.info("Email simple envoyé avec succès à {}", to);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email simple à {}: {}", to, e.getMessage());
        }
    }

    /**
     * Envoie l'email de bienvenue à un instructeur approuvé.
     * @param to L'email du nouvel instructeur.
     * @param instructorName Le nom de l'instructeur.
     * @param generatedPassword Le mot de passe temporaire généré.
     */
    @Async
    public void sendWelcomeEmail(String to, String name, String generatedPassword, String role) {
        String subject = "Bienvenue sur EduCamer ! Votre compte Instructeur est prêt.";
        
        // Préparer les variables pour le template Thymeleaf
        Map<String, Object> templateModel = Map.of(
            "name", name,
            "role", role.toLowerCase(),
            "loginEmail", to,
            "tempPassword", generatedPassword,
            "loginUrl", "http://localhost:4200/auth" // Mettez votre URL de frontend
        );

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("email/instructor-welcome-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);      
            helper.setText(htmlBody, true);
            mailSender.send(message);
            logger.info("Email HTML envoyé avec succès à {}", to);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email HTML à {}: {}", to, e.getMessage());
        }
    }

        /**
     * Envoie un email informant un candidat du rejet de sa candidature.
     * @param to L'email du candidat.
     * @param candidateName Le nom du candidat.
     * @param reason La raison du rejet fournie par l'admin.
     */
    @Async
    public void sendApplicationRejectionEmail(String to, String candidateName, String reason) {
        String subject = "Suite à votre candidature sur EduCamer";
        
        Map<String, Object> templateModel = Map.of(
            "candidateName", candidateName,
            "rejectionReason", reason
        );

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("email/instructor-rejection-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @Async
    public void sendPasswordResetEmail(String to, String name, String token) {
        String subject = "Réinitialisation de votre mot de passe sur EduCamer";
        String resetUrl = frontendUrl + "/auth/reset-password?token=" + token;
        
        Map<String, Object> templateModel = Map.of(
            "name", name,
            "resetUrl", resetUrl
        );

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("email/password-reset-template.html", thymeleafContext);
        
        sendHtmlMessage(to, subject, htmlBody);
    }

}