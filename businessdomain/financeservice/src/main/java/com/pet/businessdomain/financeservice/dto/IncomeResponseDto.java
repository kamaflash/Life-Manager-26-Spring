package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IncomeResponseDto {
    private Long id;
    private String source;
    private BigDecimal amount;
    private Enum.Frequency frequency;
    private boolean active;
    private String externalRefType;
    private Long externalRefId;
}
