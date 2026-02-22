package com.pet.businessdomain.personservice.dto;

import com.pet.businessdomain.personservice.entities.enumentities.SEnumAccount;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private SEnumAccount.TransactionType type;
    private String description;
    private LocalDateTime executedAt;
}
