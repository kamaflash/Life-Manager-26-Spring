package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SExpenseResponseDto {
    private Long id;
    private String concept;
    private BigDecimal amount;
    private EnumAll.ExpenseCategory category;
    private EnumAll.Frequency frequency;
    private boolean essential;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String externalRefType;
    private Long externalRefId;
}
