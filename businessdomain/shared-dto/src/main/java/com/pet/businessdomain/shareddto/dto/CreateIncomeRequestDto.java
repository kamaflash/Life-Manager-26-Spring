package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateIncomeRequestDto {
    private String source;
    private BigDecimal amount;
    private EnumAll.Frequency frequency;
    private EnumAll.ExpenseCategory category;
    private String externalRefType;
    private Long externalRefId;
}
