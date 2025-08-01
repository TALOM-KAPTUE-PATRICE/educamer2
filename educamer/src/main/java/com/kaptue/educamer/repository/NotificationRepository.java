package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Trouver toutes les notifications pour un utilisateur, les plus récentes d'abord
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}