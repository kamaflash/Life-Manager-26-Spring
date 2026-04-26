package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaxWithholdingCreateDTO {
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
}
