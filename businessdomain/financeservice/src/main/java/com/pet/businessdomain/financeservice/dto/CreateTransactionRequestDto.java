package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequestDto {
    private BigDecimal amount;
    private Enum.TransactionType type;
    private String description;
}
