package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateIncomeRequestDto {
    private String concept;
    private BigDecimal amount;
    private EnumAll.IncomeCategory category;
    private EnumAll.Frequency frequency;
    private LocalDate endDate;
    private boolean active;
}
