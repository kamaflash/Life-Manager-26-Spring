package com.pet.businessdomain.userservice.dto;

import com.pet.businessdomain.userservice.entities.enumentities.SEnumAccount;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SIncomeResponseDto {
    private Long id;
    private String source;
    private BigDecimal amount;
    private SEnumAccount.Frequency frequency;
    private boolean active;
    private String externalRefType;
    private Long externalRefId;
}
