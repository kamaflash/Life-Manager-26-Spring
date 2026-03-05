package com.pet.businessdomain.notificationservice.services;

import com.pet.businessdomain.notificationservice.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    NotificationDTO createNotification(NotificationDTO notificationDTO);

    List<NotificationDTO> getUserNotifications(Long userId);

    List<NotificationDTO> getUnreadNotifications(Long userId);

    long getUnreadCount(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

}
