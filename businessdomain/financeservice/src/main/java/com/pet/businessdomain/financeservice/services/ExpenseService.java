package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.shareddto.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.shareddto.dto.ExpenseResponseDto;
import com.pet.businessdomain.shareddto.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseService {

    ExpenseResponseDto addExpense(Long accountId, CreateExpenseRequestDto dto);

    List<CreateExpenseRequestDto> getExpenses(Long accountId);

    void deactivateExpense(Long expenseId);
    CreateExpenseRequestDto mapperCreateExpense(BigDecimal income, EnumAll.ExpenseCategory exp, String concep,
                                                FinanceAccountResponseDto accountDto
    );
}
