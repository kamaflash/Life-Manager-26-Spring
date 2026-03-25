package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequestDto {
    private BigDecimal amount;
    private EnumAll.TransactionType type;
    private String description;
}
