package com.pet.businessdomain.jobservice.controller.IRPF;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxNotificationEntity;
import com.pet.businessdomain.jobservice.services.IRPF.TaxNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tax/notifications")
@RequiredArgsConstructor
public class TaxNotificationController {

    private final TaxNotificationService notificationService;

    @PostMapping("/filing-result")
    public ResponseEntity<TaxNotificationEntity> createFilingResultNotification(
            @RequestParam(name = "characterId") Long characterId,
            @RequestParam(name = "taxFilingId") Long taxFilingId,
            @RequestParam(name = "amount") BigDecimal amount,
            @RequestParam(name = "resultType") String resultType) {
        TaxNotificationEntity notification = notificationService.createFilingResultNotification(characterId, taxFilingId, amount, resultType);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @PostMapping("/payment-reminder")
    public ResponseEntity<TaxNotificationEntity> createPaymentReminder(
            @RequestParam(name = "characterId") Long characterId,
            @RequestParam(name = "taxFilingId") Long taxFilingId,
            @RequestParam(name = "amount") BigDecimal amount,
            @RequestParam(name = "deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline) {
        TaxNotificationEntity notification = notificationService.createPaymentReminder(characterId, taxFilingId, amount, deadline);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @PostMapping("/period-opened")
    public ResponseEntity<TaxNotificationEntity> createPeriodOpenedNotification(
            @RequestParam(name = "characterId") Long characterId,
            @RequestParam(name = "taxYear") Integer taxYear,
            @RequestParam(name = "deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline) {
        TaxNotificationEntity notification = notificationService.createPeriodOpenedNotification(characterId, taxYear, deadline);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @GetMapping("/character/{characterId}/unread")
    public ResponseEntity<List<TaxNotificationEntity>> getUnreadNotifications(
            @PathVariable(name = "characterId") Long characterId) {
        List<TaxNotificationEntity> notifications = notificationService.getUnreadNotificationsByCharacter(characterId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<TaxNotificationEntity>> getNotificationsByCharacter(
            @PathVariable(name = "characterId") Long characterId) {
        List<TaxNotificationEntity> notifications = notificationService.getNotificationsByCharacter(characterId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable(name = "notificationId") Long notificationId,
            @RequestParam(name = "characterId") Long characterId) {
        notificationService.markAsRead(notificationId, characterId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-pending")
    public ResponseEntity<Void> sendPendingNotifications() {
        notificationService.sendPendingNotifications();
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/massive/year/{taxYear}")
    public ResponseEntity<Void> createMassiveNotifications(
            @PathVariable(name = "taxYear") Integer taxYear) {
        notificationService.createMassiveNotificationsForFiling(taxYear);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}