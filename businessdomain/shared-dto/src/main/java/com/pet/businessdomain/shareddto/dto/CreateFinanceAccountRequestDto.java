package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateFinanceAccountRequestDto {
    private EnumAll.OwnerType ownerType;
    private Long ownerId;
    private BigDecimal balance;
}
