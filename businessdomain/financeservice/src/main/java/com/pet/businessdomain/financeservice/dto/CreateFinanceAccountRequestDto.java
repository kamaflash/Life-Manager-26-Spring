package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateFinanceAccountRequestDto {
    private Enum.OwnerType ownerType;
    private Long ownerId;
    private BigDecimal balance;
}
