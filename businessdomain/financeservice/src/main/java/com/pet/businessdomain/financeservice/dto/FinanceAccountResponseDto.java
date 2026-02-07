package com.pet.businessdomain.financeservice.dto;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceAccountResponseDto {

    private Long id;
    private Enum.OwnerType ownerType;
    private Long ownerId;
    private BigDecimal balance;
    private BigDecimal savings;
    private BigDecimal debt;
    private List<CreateIncomeRequestDto> incomes;
    private List<CreateExpenseRequestDto> expenses;
}
