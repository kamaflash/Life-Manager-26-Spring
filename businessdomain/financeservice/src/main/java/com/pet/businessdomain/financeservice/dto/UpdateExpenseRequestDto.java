package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateExpenseRequestDto {
    private String concept;
    private BigDecimal amount;
    private Enum.ExpenseCategory category;
    private Enum.Frequency frequency;
    private boolean essential;
    private LocalDate endDate;
    private boolean active;
}
