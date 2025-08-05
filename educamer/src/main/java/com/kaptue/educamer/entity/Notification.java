package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;



@Entity 
@Table(name = "notifications") 
@Getter 
@Setter
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name = "recipient_id") private User recipient;
    private String message;
    private String link; // Lien vers la ressource concernée
    private boolean isRead = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}