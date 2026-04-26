package com.pet.businessdomain.jobservice.entities.IRPF;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_filing_periods")
@Data
public class TaxFilingPeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer taxYear;
    private String name;                       // ej: "Campaña Renta 2024"

    private LocalDateTime filingStartDate;
    private LocalDateTime filingEndDate;

    private String status;                     // UPCOMING, ACTIVE, CLOSED

    // Reglas fiscales del período
    private String applicableRules;             // JSON con tramos, deducciones, etc.
    private BigDecimal minimumTaxable;          // Mínimo exento
    private Boolean lateFilingAllowed;
    private BigDecimal lateFilingPenalty;       // Recargo por presentación tardía

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}