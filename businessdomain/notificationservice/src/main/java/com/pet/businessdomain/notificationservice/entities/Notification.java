package com.pet.businessdomain.notificationservice.entities;

import com.pet.businessdomain.notificationservice.entities.enumentities.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que recibe la notificación
    @Column(nullable = false)
    private Long userId;

    // Usuario que genera la acción (opcional)
    private Long fromUserId;

    // Tipo de notificación (CHAT, FOLLOW, LIKE, SYSTEM...)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // Texto que se mostrará
    @Column(nullable = false)
    private String message;

    // Para enlazar con un recurso (ej: id del chat, post, comentario)
    private Long referenceId;

    // Si fue leída
    @Column(nullable = false)
    private boolean read = false;

    // Fecha de creación
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

}
