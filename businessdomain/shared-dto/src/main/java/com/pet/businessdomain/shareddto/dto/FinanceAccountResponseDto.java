package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceAccountResponseDto {

    private Long id;
    private EnumAll.OwnerType ownerType;
    private Long ownerId;
    private BigDecimal balance;
    private BigDecimal savings;
    private BigDecimal debt;
    private List<CreateIncomeRequestDto> incomes;
    private List<CreateExpenseRequestDto> expenses;
    private List<TransactionResponseDto> transactions;
}
