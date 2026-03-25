package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SIncomeResponseDto {
    private Long id;
    private String source;
    private BigDecimal amount;
    private EnumAll.ExpenseCategory category;
    private EnumAll.Frequency frequency;
    private boolean active;
    private String externalRefType;
    private Long externalRefId;
}
