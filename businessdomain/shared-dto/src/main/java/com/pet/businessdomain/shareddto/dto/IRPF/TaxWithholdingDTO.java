package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TaxWithholdingDTO {
    private Long id;
    private Long payrollId;
    private Long characterId;
    private Long accountId;
    private Integer year;
    private Integer month;
    private BigDecimal grossIncome;
    private BigDecimal irpfRate;
    private BigDecimal irpfWithheld;
    private BigDecimal accumulatedTaxBase;
    private BigDecimal accumulatedWithheld;
    private LocalDateTime createdAt;
}
