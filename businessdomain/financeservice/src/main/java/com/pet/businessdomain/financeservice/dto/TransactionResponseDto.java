package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private Enum.TransactionType type;
    private String description;
    private LocalDateTime executedAt;
}
