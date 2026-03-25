package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SFinanceAccountResponseDto {

    private Long id;
    private EnumAll.OwnerType ownerType;
    private Long ownerId;
    private BigDecimal balance;
    private BigDecimal savings;
    private BigDecimal debt;
    private List<SIncomeResponseDto> incomes;
    private List<SExpenseResponseDto> expenses;
    private List<TransactionResponseDto> transactions;

}
