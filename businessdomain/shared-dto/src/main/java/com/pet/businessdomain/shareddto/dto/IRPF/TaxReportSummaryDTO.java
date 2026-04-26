package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaxReportSummaryDTO {
    private BigDecimal totalGrossIncomeDeclared;
    private BigDecimal totalTaxCalculated;
    private BigDecimal totalWithheld;
    private BigDecimal netTaxRevenue; // Total a pagar - total a devolver
    private Long countToPay;
    private Long countToReceive;
    private BigDecimal averagePayment;
    private BigDecimal averageRefund;
}
