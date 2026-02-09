package com.pet.businessdomain.formationservice.dto;

import com.pet.businessdomain.formationservice.entities.enumentities.SEnumAccount;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SCreateExpenseRequestDto {
    private String concept;
    private BigDecimal amount;
    private SEnumAccount.ExpenseCategory category;
    private SEnumAccount.Frequency frequency;
    private boolean essential;
    private LocalDate startDate;
    private LocalDate endDate;
    private String externalRefType;
    private Long externalRefId;
}
