package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.financeservice.dto.ExpenseResponseDto;
import com.pet.businessdomain.financeservice.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseService {

    ExpenseResponseDto addExpense(Long accountId, CreateExpenseRequestDto dto);

    List<ExpenseResponseDto> getExpenses(Long accountId);

    void deactivateExpense(Long expenseId);
    CreateExpenseRequestDto mapperCreateExpense(BigDecimal income, Enum.ExpenseCategory exp, String concep,
                                                FinanceAccountResponseDto accountDto
    );
}
