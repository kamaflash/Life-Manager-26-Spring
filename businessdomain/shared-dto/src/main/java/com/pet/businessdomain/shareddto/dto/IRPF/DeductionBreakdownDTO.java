package com.pet.businessdomain.shareddto.dto.IRPF;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeductionBreakdownDTO {
    private String concept; // MINIMUM_PERSONAL, WORK_EXPENSE, FAMILY, etc.
    private BigDecimal amount;
    private String description;
}
