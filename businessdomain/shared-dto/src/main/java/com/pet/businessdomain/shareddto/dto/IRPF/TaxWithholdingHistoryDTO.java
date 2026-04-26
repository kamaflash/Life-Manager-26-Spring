package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Data
public class TaxWithholdingHistoryDTO {
    private Long characterId;
    private Integer year;
    private List<MonthlyWithholdingDTO> monthlyWithholdings;
    private BigDecimal totalAnnualGross;
    private BigDecimal totalAnnualWithheld;
    private BigDecimal averageRate;
}
