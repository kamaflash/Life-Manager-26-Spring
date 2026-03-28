package com.pet.businessdomain.notificationservice.controller;

import com.pet.businessdomain.notificationservice.services.NotificationService;
import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import com.pet.businessdomain.shareddto.enumentities.NotificationPriority;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;
    private static final int DEFAULT_SIZE = 10;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Crear notificación
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        NotificationDTO created = service.createNotification(notificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/post")
    public NotificationDTO createNotificationPost(@RequestBody NotificationDTO notificationDTO) {
        NotificationDTO created = service.createNotification(notificationDTO);
        return created;
    }

    // Obtener todas las notificaciones de un usuario con filtros opcionales
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserNotifications(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size,
            @RequestParam(name = "type", required = false) NotificationType type,
            @RequestParam(name = "priority", required = false) NotificationPriority priority,
            @RequestParam(name = "read", required = false) Boolean read) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notificationPage = service.getUserNotifications(userId, pageable, type, priority, read);

        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notificationPage.getContent());
        response.put("currentPage", notificationPage.getNumber());
        response.put("totalItems", notificationPage.getTotalElements());
        response.put("totalPages", notificationPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getUserNotificationsAll() {

        List<NotificationDTO> notificationPage = service.getUserNotificationsAll();

        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notificationPage);

        return ResponseEntity.ok(response);
    }

    // Obtener notificaciones no leídas (versión paginada)
    @GetMapping("/unread/{userId}")
    public ResponseEntity<Map<String, Object>> getUnreadNotifications(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notificationPage = service.getUnreadNotifications(userId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notificationPage.getContent());
        response.put("currentPage", notificationPage.getNumber());
        response.put("totalItems", notificationPage.getTotalElements());
        response.put("totalPages", notificationPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Contador de no leídas (para badge)
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable(name = "userId") Long userId) {
        long count = service.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }

    // Marcar una notificación como leída
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(@PathVariable(name = "notificationId") Long notificationId) {
        service.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    // Marcar todas como leídas
    @PutMapping("/read-all/{userId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable(name = "userId") Long userId) {
        service.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}