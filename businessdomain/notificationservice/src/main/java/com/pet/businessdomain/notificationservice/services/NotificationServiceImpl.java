package com.pet.businessdomain.notificationservice.services;

import com.pet.businessdomain.notificationservice.entities.Notification;
import com.pet.businessdomain.notificationservice.mapper.NotificationMapper;
import com.pet.businessdomain.notificationservice.repository.NotificationRepository;
import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import com.pet.businessdomain.shareddto.enumentities.NotificationPriority;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = mapper.toEntity(notificationDTO);

        // Set default values for new fields if not provided
        if (notification.getPriority() == null) {
            notification.setPriority(NotificationPriority.MEDIUM);
        }
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        if (notification.getReadAt() == null && notification.isRead()) {
            notification.setReadAt(LocalDateTime.now());
        }
        // updatedAt will be set by @UpdateTimestamp in entity, but we can leave it null initially
        notification.setRead(false);
        notification.setDeleted(false);

        Notification saved = repository.save(notification);
        return mapper.toDTO(saved);
    }
    @Override
    public List<NotificationDTO> getUserNotificationsAll( ) {
        List<Notification> notifications = repository.findAll();
        return mapper.toDTOList(notifications);
    }
    @Override
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable,
                                                      NotificationType type,
                                                      NotificationPriority priority,
                                                      Boolean read) {
        Page<Notification> notifications;

        // Build query dynamically based on filters
        if (type != null && priority != null && read != null) {
            notifications = repository.findByUserIdAndTypeAndPriorityAndRead(userId, type, priority, read, pageable);
        } else if (type != null && priority != null) {
            notifications = repository.findByUserIdAndTypeAndPriority(userId, type, priority, pageable);
        } else if (type != null && read != null) {
            notifications = repository.findByUserIdAndTypeAndRead(userId, type, read, pageable);
        } else if (priority != null && read != null) {
            notifications = repository.findByUserIdAndPriorityAndRead(userId, priority, read, pageable);
        } else if (type != null) {
            notifications = repository.findByUserIdAndType(userId, type, pageable);
        } else if (priority != null) {
            notifications = repository.findByUserIdAndPriority(userId, priority, pageable);
        } else if (read != null) {
            notifications = repository.findByUserIdAndRead(userId, read, pageable);
        } else {
            notifications = repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }

        return notifications.map(mapper::toDTO);
    }

    @Override
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        return getUserNotifications(userId, pageable, null, null, null);
    }

    @Override
    public Page<NotificationDTO> getUnreadNotifications(Long userId, Pageable pageable) {
        Page<Notification> notifications = repository.findByUserIdAndReadFalse(userId, pageable);
        return notifications.map(mapper::toDTO);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return repository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            repository.save(notification);
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = repository.findByUserIdAndReadFalse(userId);
        if (!unreadNotifications.isEmpty()) {
            unreadNotifications.forEach(n -> {
                n.setRead(true);
                n.setReadAt(LocalDateTime.now());
            });
            repository.saveAll(unreadNotifications);
        }
    }
}