package com.pet.businessdomain.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;

    private Long userId;

    private Long fromUserId;

    private String type;

    private String message;

    private Long referenceId;

    private boolean read;

    private LocalDateTime createdAt;
}
