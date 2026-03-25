package com.pet.businessdomain.notificationservice.services;

import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    NotificationDTO createNotification(NotificationDTO notificationDTO);

    List<NotificationDTO> getUserNotifications(Long userId);
    Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable);

    List<NotificationDTO> getUnreadNotifications(Long userId);
    Page<NotificationDTO> getUnreadNotifications(Long userId, Pageable pageable);

    long getUnreadCount(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

}
