package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import com.pet.businessdomain.shareddto.enumentities.NotificationPriority;
import com.pet.businessdomain.shareddto.enumentities.NotificationResourceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private Long id;

    // Destinatario
    private Long userId;

    // Usuario que genera la acción (opcional)
    private Long fromUserId;

    // Tipo de notificación (determina plantilla frontend)
    private NotificationType type;

    // Prioridad (HIGH, MEDIUM, LOW)
    private NotificationPriority priority;

    // Título y contenido
    private String title;
    private String subTitle;
    private String message;

    // Tipo de recurso asociado (COURSE, POST, etc.)
    private NotificationResourceType resourceType;

    // ID del recurso asociado
    private Long resourceId;

    // URL de acción (ruta frontend)
    private String actionUrl;

    // Metadatos en JSON (datos específicos del tipo)
    private String metadata;

    // Estado de lectura
    private boolean read;

    // Fecha de lectura (si aplica)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime readAt;

    // Fecha de expiración (para notificaciones temporales)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;

    // Fecha de creación
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    // Fecha de última modificación (opcional, útil para tracking)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}