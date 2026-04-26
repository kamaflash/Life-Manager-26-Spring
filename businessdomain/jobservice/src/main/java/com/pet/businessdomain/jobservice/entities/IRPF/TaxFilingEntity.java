package com.pet.businessdomain.jobservice.entities.IRPF;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_filings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"characterId", "taxYear"}))
@Data
public class TaxFilingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;
    private Long accountId;
    private Integer taxYear;

    // Datos fiscales agregados
    private BigDecimal totalGrossIncome;      // Ingresos brutos totales
    private BigDecimal totalDeductions;       // Deducciones aplicables
    private BigDecimal taxableBase;            // Base imponible

    // Cálculo del impuesto
    private BigDecimal calculatedTax;          // Impuesto calculado según tramos
    private BigDecimal totalWithheld;          // Total retenido durante el año
    private BigDecimal result;                 // Positivo = a pagar, Negativo = a devolver

    private String resultType;                 // TO_PAY, TO_RECEIVE, NEUTRAL

    // Estado de la declaración
    private String status;                     // DRAFT, SUBMITTED, PROCESSING, COMPLETED, CANCELLED
    private LocalDate filingDate;              // Fecha de presentación
    private LocalDate paymentDeadline;         // Fecha límite de pago (si aplica)

    // Gestión de deuda/devolución
    private BigDecimal amountToPay;             // Cantidad a pagar (si >0)
    private BigDecimal amountToReceive;         // Cantidad a recibir (si >0)
    private String paymentStatus;               // PENDING, PAID, OVERDUE, REFUNDED
    private LocalDateTime paymentProcessedAt;

    // Si se eligió fraccionamiento
    private Boolean paymentFractionated;
    private Integer numberOfInstallments;
    private BigDecimal installmentAmount;

    // Auditoría
    private LocalDateTime submittedAt;
    private String submittedBy;                 // Usuario/sistema que presentó
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}