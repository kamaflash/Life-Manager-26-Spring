package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseResponseDto {
    private Long id;
    private String concept;
    private BigDecimal amount;
    private Enum.ExpenseCategory category;
    private Enum.Frequency frequency;
    private boolean essential;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String externalRefType;
    private Long externalRefId;
}
