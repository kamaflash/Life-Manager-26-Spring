package com.pet.businessdomain.formationservice.dto;

import com.pet.businessdomain.formationservice.entities.enumentities.SEnumAccount;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SFinanceAccountResponseDto {

    private Long id;
    private SEnumAccount.OwnerType ownerType;
    private Long ownerId;
    private BigDecimal balance;
    private BigDecimal savings;
    private BigDecimal debt;
    private List<SIncomeResponseDto> incomes;
    private List<SExpenseResponseDto> expenses;
}
