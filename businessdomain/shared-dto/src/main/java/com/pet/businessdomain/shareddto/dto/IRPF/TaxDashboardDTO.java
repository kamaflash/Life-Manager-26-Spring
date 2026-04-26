package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TaxDashboardDTO {
    private Long characterId;
    private String characterName;
    private BigDecimal totalWithheldCurrentYear;
    private BigDecimal estimatedTax;
    private BigDecimal estimatedResult;
    private Integer daysUntilDeadline;

    // Opcional: si quieres incluir histórico de retenciones
    private TaxWithholdingHistoryDTO withholdingHistory;

    // Opcional: si quieres incluir cálculos detallados
    private TaxCalculationResponseDTO currentCalculation;
}