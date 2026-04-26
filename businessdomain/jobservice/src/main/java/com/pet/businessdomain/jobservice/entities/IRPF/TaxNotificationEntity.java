package com.pet.businessdomain.jobservice.entities.IRPF;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_notifications")
@Data
public class TaxNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;
    private Long taxFilingId;

    private String notificationType;           // FILING_REMINDER, RESULT_READY, PAYMENT_REMINDER, OVERDUE
    private String title;
    private String message;

    private String status;                     // PENDING, SENT, READ, ACTION_TAKEN

    private BigDecimal amount;                  // Monto relacionado (a pagar o devolver)

    private LocalDateTime scheduledAt;          // Cuándo enviar
    private LocalDateTime sentAt;
    private LocalDateTime readAt;

    private LocalDateTime createdAt;
}