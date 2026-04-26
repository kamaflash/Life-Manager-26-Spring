package com.pet.businessdomain.jobservice.services.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxNotificationEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TaxNotificationService {

    // Crear notificación de resultado de declaración
    TaxNotificationEntity createFilingResultNotification(Long characterId, Long taxFilingId, BigDecimal amount, String resultType);

    // Crear recordatorio de pago
    TaxNotificationEntity createPaymentReminder(Long characterId, Long taxFilingId, BigDecimal amount, LocalDateTime deadline);

    // Crear notificación de período abierto
    TaxNotificationEntity createPeriodOpenedNotification(Long characterId, Integer taxYear, LocalDateTime deadline);

    // Obtener notificaciones no leídas de un personaje
    List<TaxNotificationEntity> getUnreadNotificationsByCharacter(Long characterId);

    // Obtener todas las notificaciones de un personaje
    List<TaxNotificationEntity> getNotificationsByCharacter(Long characterId);

    // Marcar notificación como leída
    void markAsRead(Long notificationId, Long characterId);

    // Enviar notificaciones pendientes (para scheduler)
    void sendPendingNotifications();

    // Crear notificaciones masivas para todos los personajes con declaración
    void createMassiveNotificationsForFiling(Integer taxYear);
}
