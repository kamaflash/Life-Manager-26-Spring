package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsDTO {
    private Long totalToPay;
    private Long totalToReceive;
    private Long totalNeutral;
    private BigDecimal totalAmountToPay;
    private BigDecimal totalAmountToReceive;
    private BigDecimal averageRefund;
    private BigDecimal averagePayment;
}
