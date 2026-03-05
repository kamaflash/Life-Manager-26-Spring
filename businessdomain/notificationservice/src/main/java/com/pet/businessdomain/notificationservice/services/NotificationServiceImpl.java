package com.pet.businessdomain.notificationservice.services;

import com.pet.businessdomain.notificationservice.dto.NotificationDTO;
import com.pet.businessdomain.notificationservice.entities.Notification;
import com.pet.businessdomain.notificationservice.mapper.NotificationMapper;
import com.pet.businessdomain.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {

        Notification notification = mapper.toEntity(notificationDTO);

        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification saved = repository.save(notification);

        return mapper.toDTO(saved);
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {

        List<Notification> notifications = repository
                .findByUserIdOrderByCreatedAtDesc(userId);

        return mapper.toDTOList(notifications);
    }

    @Override
    public List<NotificationDTO> getUnreadNotifications(Long userId) {

        List<Notification> notifications = repository
                .findByUserIdAndReadFalse(userId);

        return mapper.toDTOList(notifications);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return repository.countByUserIdAndReadFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {

        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);

        repository.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {

        List<Notification> notifications = repository.findByUserIdAndReadFalse(userId);

        notifications.forEach(n -> n.setRead(true));

        repository.saveAll(notifications);
    }
}
