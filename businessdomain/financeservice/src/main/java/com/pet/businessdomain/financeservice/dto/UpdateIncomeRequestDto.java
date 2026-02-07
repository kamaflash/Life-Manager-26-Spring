package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateIncomeRequestDto {
    private String concept;
    private BigDecimal amount;
    private Enum.IncomeCategory category;
    private Enum.Frequency frequency;
    private LocalDate endDate;
    private boolean active;
}
