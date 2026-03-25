package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private Long id;

    private Long userId;

    private Long fromUserId;

    private NotificationType type;

    private String title;
    private String subTitle;
    private String message;

    // 🔥 Nuevo: tipo de recurso
    private EnumAll.NotificationResourceType resourceType;

    // 🔥 Nuevo: id del recurso
    private Long resourceId;

    // 🔥 Nuevo: navegación directa en frontend
    private String actionUrl;

    // 🔥 Nuevo: metadata (JSON en string)
    private String metadata;

    private boolean read;

    private LocalDateTime createdAt;

    // 🔥 Opcional pero muy útil
    private LocalDateTime readAt;
}
