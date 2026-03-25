package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private EnumAll.TransactionType type;
    private EnumAll.ExpenseCategory category;
    private String description;
    private LocalDateTime executedAt;
}
