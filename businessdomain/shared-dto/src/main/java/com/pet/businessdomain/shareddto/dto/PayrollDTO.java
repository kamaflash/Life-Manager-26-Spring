package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor  // ← IMPORTANTE: Añadir esto
@AllArgsConstructor
public class PayrollDTO {

    private Long id;
    private Long characterId;
    private Long jobId;
    private Long accountId;  // ← AÑADE ESTE CAMPO

    private String jobTitle;
    private String companyName;

    private Integer year;
    private Integer month;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal deductions;
    private BigDecimal netSalary;

    private Integer hoursWorked;
    private Integer daysWorked;
    private Integer performance;
    private Integer satisfaction;

    private String status;
    private LocalDateTime paymentDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String processedBy;
}