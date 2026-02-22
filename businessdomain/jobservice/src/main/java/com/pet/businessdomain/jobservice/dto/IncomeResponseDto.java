package com.pet.businessdomain.jobservice.dto;

import com.pet.businessdomain.jobservice.entities.enumjobs.EnumIncome;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncomeResponseDto {
    private Long id;
    private String source;
    private BigDecimal amount;
    private EnumIncome.Frequency frequency;
    private boolean active;
    private String externalRefType;
    private Long externalRefId;
}
