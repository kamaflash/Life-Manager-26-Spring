package com.pet.businessdomain.personservice.dto;

import com.pet.businessdomain.personservice.entities.enumentities.SEnumAccount;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SCreateFinanceAccountRequestDto {
    private SEnumAccount.OwnerType ownerType;
    private Long ownerId;
    private BigDecimal balance;
}
