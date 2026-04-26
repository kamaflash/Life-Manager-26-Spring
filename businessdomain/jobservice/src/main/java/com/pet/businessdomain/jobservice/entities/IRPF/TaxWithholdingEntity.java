package com.pet.businessdomain.jobservice.entities.IRPF;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_withholdings")
@Data
public class TaxWithholdingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long payrollId;
    private Long characterId;
    private Long accountId;

    private Integer year;
    private Integer month;

    private BigDecimal grossIncome;      // Ingreso bruto del período
    private BigDecimal irpfRate;          // Porcentaje de retención aplicado
    private BigDecimal irpfWithheld;      // Cantidad retenida

    // Base imponible acumulada (para cálculos progresivos)
    private BigDecimal accumulatedTaxBase;
    private BigDecimal accumulatedWithheld;

    private LocalDateTime createdAt;
}