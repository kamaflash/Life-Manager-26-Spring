package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payrolls")
@Data
public class PayrollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relaciones
    private Long characterId;
    private Long jobId;
    private Long accountId;

    // Información básica
    private String jobTitle;
    private String companyName;

    // Período
    private Integer year;
    private Integer month;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    // Financiero
    private BigDecimal baseSalary;      // Salario base mensual
    private BigDecimal bonus;           // Bonificaciones extras
    private BigDecimal deductions;      // Deducciones (impuestos, etc)
    private BigDecimal netSalary;       // Salario neto final

    // Métricas del período
    private Integer hoursWorked;
    private Integer daysWorked;
    private Integer performance;        // Rendimiento (0-100)
    private Integer satisfaction;       // Satisfacción (0-100)

    // Estado
    private String status;              // PENDING, PAID, CANCELLED
    private LocalDateTime paymentDate;

    // Auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String processedBy;
}
