package com.pet.businessdomain.notificationservice.repository;

import com.pet.businessdomain.notificationservice.entities.Notification;
import com.pet.businessdomain.shareddto.enumentities.NotificationPriority;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Notification> findByUserIdAndReadFalse(Long userId, Pageable pageable);
    long countByUserIdAndReadFalse(Long userId);
    List<Notification> findByUserIdAndReadFalse(Long userId);

    // Métodos para filtros combinados
    Page<Notification> findByUserIdAndType(Long userId, NotificationType type, Pageable pageable);
    Page<Notification> findByUserIdAndPriority(Long userId, NotificationPriority priority, Pageable pageable);
    Page<Notification> findByUserIdAndRead(Long userId, boolean read, Pageable pageable);
    Page<Notification> findByUserIdAndTypeAndPriority(Long userId, NotificationType type, NotificationPriority priority, Pageable pageable);
    Page<Notification> findByUserIdAndTypeAndRead(Long userId, NotificationType type, boolean read, Pageable pageable);
    Page<Notification> findByUserIdAndPriorityAndRead(Long userId, NotificationPriority priority, boolean read, Pageable pageable);
    Page<Notification> findByUserIdAndTypeAndPriorityAndRead(Long userId, NotificationType type, NotificationPriority priority, boolean read, Pageable pageable);
}
