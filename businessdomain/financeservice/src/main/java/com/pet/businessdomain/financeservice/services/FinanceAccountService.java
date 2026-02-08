package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.financeservice.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.financeservice.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;

import java.math.BigDecimal;
import java.util.List;

public interface FinanceAccountService {

    FinanceAccountResponseDto createAccount(FinanceAccountResponseDto dto);

    FinanceAccountResponseDto getAccountById(Long id);   
    
    FinanceAccountResponseDto getAccountByOwner(Enum.OwnerType ownerType, Long ownerId);

    List<FinanceAccountResponseDto> getAllAccounts();

    FinanceAccountResponseDto updateAccount(Long id, FinanceAccountResponseDto dto);

    void deleteAccount(Long id);

    List<FinanceAccountResponseDto> getAccountsByOwnerId(Long ownerId);

    CreateIncomeRequestDto setIncome(FinanceAccountResponseDto account, BigDecimal income);

    CreateExpenseRequestDto setExpense(Enum.ExpenseCategory category, BigDecimal expense, FinanceAccountResponseDto account, String type);
}
