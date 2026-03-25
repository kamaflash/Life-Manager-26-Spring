package com.pet.businessdomain.notificationservice.controller;

import com.pet.businessdomain.notificationservice.services.NotificationService;
import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private final NotificationService service;
    private static final int DEFAULT_SIZE = 10;

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
    @PostMapping("/post")
    public NotificationDTO createNotificationFull(
            @RequestBody NotificationDTO notificationDTO) {
        return service.createNotification(notificationDTO);
    }

    // Obtener todas las notificaciones de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserNotifications(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notificationPage =service.getUserNotifications(userId,pageable);
        if (notificationPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notificationPage.getContent());
        response.put("currentPage", notificationPage.getNumber());
        response.put("totalItems", notificationPage.getTotalElements());
        response.put("totalPages", notificationPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Obtener notificaciones no leídas
    @GetMapping("/unread/{userId}")
    public ResponseEntity<?> getUnreadNotifications(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notificationPage =service.getUnreadNotifications(userId,pageable);
        if (notificationPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notificationPage.getContent());
        response.put("currentPage", notificationPage.getNumber());
        response.put("totalItems", notificationPage.getTotalElements());
        response.put("totalPages", notificationPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(
            @PathVariable(name = "userId") Long userId) {

        return ResponseEntity.ok(service.getUnreadNotifications(userId));
    }

    // Contador de no leídas (para badge)
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable(name = "userId") Long userId) {

        return ResponseEntity.ok(service.getUnreadCount(userId));
    }

    // Marcar una notificación como leída
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(
            @PathVariable(name = "notificationId") Long notificationId) {

        service.markAsRead(notificationId);

        return ResponseEntity.ok().build();
    }

    // Marcar todas como leídas
    @PutMapping("/read-all/{userId}")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable(name = "userId") Long userId) {

        service.markAllAsRead(userId);

        return ResponseEntity.ok().build();
    }

}
