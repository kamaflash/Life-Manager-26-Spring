package com.pet.businessdomain.notificationservice.services;

import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import com.pet.businessdomain.shareddto.enumentities.NotificationPriority;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    NotificationDTO createNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> getUserNotificationsAll( );
    // Método con filtros opcionales
    Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable,
                                               NotificationType type,
                                               NotificationPriority priority,
                                               Boolean read);
    Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable);

    Page<NotificationDTO> getUnreadNotifications(Long userId, Pageable pageable);

    long getUnreadCount(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);
}