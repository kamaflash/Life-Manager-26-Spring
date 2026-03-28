package com.pet.businessdomain.notificationservice.entities;

import com.pet.businessdomain.shareddto.enumentities.NotificationPriority;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import com.pet.businessdomain.shareddto.enumentities.NotificationResourceType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_user_id", columnList = "userId"),
                @Index(name = "idx_notifications_user_read", columnList = "userId, read"),
                @Index(name = "idx_notifications_created_at", columnList = "createdAt")
        })
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Destinatario de la notificación
    @Column(nullable = false)
    private Long userId;

    // Usuario que origina la acción (opcional)
    private Long fromUserId;

    // Tipo de notificación (determina la plantilla frontend)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // Prioridad: HIGH, MEDIUM, LOW (para destacar visualmente)
    @Enumerated(EnumType.STRING)
    private NotificationPriority priority = NotificationPriority.MEDIUM;

    // Título y cuerpo principal
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String message;

    // Subtítulo opcional (puede usarse como complemento)
    private String subTitle;

    // Tipo de recurso asociado (COURSE, PRODUCT, POST, etc.)
    @Enumerated(EnumType.STRING)
    private NotificationResourceType resourceType;

    // ID del recurso asociado
    private Long resourceId;

    // URL de acción (por ejemplo, para navegación directa)
    private String actionUrl;

    // Metadatos flexibles en JSON (imagen, fechas, etc.)
    @Column(columnDefinition = "TEXT")
    private String metadata;

    // Estado de lectura
    @Column(nullable = false)
    private boolean read = false;

    // Fecha de lectura (si aplica)
    private LocalDateTime readAt;

    // Eliminado lógico
    @Column(nullable = false)
    private boolean deleted = false;

    // Fecha de expiración (para notificaciones temporales)
    private LocalDateTime expiresAt;

    // Fecha de creación (automática)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Última modificación (útil para tracking)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Notification() {
        // Constructor vacío requerido por JPA
    }
}