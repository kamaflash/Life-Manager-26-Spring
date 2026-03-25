package com.pet.businessdomain.notificationservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
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

    // Tipo de notificación
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // Título y contenido
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    private String subTitle;

    // 🔥 NUEVO: tipo de recurso (COURSE, PRODUCT, POST...)
    @Enumerated(EnumType.STRING)
    private EnumAll.NotificationResourceType resourceType;

    // 🔥 NUEVO: id del recurso
    private Long resourceId;

    // 🔥 NUEVO: URL o ruta frontend
    private String actionUrl;

    // 🔥 NUEVO: JSON con datos extra (imagen, nombre, etc)
    @Column(columnDefinition = "TEXT")
    private String metadata;

    // Estado
    @Column(nullable = false)
    private boolean read = false;

    @Column(nullable = false)
    private boolean deleted = false;

    // Fecha
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }
}