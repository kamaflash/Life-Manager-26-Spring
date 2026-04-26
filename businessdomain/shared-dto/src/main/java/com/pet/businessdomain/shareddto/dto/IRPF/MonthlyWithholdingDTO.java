package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class MonthlyWithholdingDTO {
    private YearMonth period;
    private BigDecimal grossIncome;
    private BigDecimal irpfRate;
    private BigDecimal irpfWithheld;
}
