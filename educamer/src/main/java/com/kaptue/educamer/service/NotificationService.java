package com.kaptue.educamer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaptue.educamer.entity.Notification;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.repository.NotificationRepository;

import jakarta.transaction.Transactional;

// ... imports ...
@Service
public class NotificationService {
    @Autowired private NotificationRepository notificationRepository;
    
    // Méthode générique pour créer une notification
    @Transactional
    public void createNotification(User recipient, String message, String link) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setLink(link); // ex: "/student/course/123/assignment/45"
        notificationRepository.save(notification);
    }
    
    // ... Autres méthodes pour marquer comme lu, etc.
}