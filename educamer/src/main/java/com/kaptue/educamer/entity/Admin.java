package com.kaptue.educamer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter @Setter
public class Admin extends User {
    // L'Admin n'a pas forcément de champs spécifiques,
    // son rôle est défini par la sécurité.
}