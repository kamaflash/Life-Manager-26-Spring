package com.pet.businessdomain.notificationservice.controller;

import com.pet.businessdomain.notificationservice.dto.NotificationDTO;
import com.pet.businessdomain.notificationservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Crear notificación
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @RequestBody NotificationDTO notificationDTO) {

        NotificationDTO created = service.createNotification(notificationDTO);

        return ResponseEntity.ok(created);
    }

    // Obtener todas las notificaciones de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(service.getUserNotifications(userId));
    }

    // Obtener notificaciones no leídas
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(service.getUnreadNotifications(userId));
    }

    // Contador de no leídas (para badge)
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long userId) {

        return ResponseEntity.ok(service.getUnreadCount(userId));
    }

    // Marcar una notificación como leída
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId) {

        service.markAsRead(notificationId);

        return ResponseEntity.ok().build();
    }

    // Marcar todas como leídas
    @PutMapping("/read-all/{userId}")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable Long userId) {

        service.markAllAsRead(userId);

        return ResponseEntity.ok().build();
    }

}
