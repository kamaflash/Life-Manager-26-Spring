package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaxReportBracketDTO {
    private String bracketRange; // "0-12.450€"
    private Long characterCount;
    private BigDecimal totalTax;
    private BigDecimal averageTax;
}
