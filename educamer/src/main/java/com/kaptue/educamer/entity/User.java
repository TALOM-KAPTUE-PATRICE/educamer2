package com.kaptue.educamer.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED) // Stratégie d'héritage
@Getter 
@Setter
public abstract class User { // Note: la classe est abstraite

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private boolean enabled = true; 

    @Column(nullable = false)
    private String password;

    @Column(length = 255)
    private String avatarUrl;

    @Column(length = 255)
    private String avatarPublicId; // <-- AJOUTEZ CE CHAMP

   
}