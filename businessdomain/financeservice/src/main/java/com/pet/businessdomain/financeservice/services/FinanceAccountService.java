package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.shareddto.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.shareddto.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.shareddto.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;

import java.math.BigDecimal;
import java.util.List;

public interface FinanceAccountService {

    FinanceAccountResponseDto createAccount(FinanceAccountResponseDto dto);

    FinanceAccountResponseDto getAccountById(Long id);   
    
    FinanceAccountResponseDto getAccountByOwner(EnumAll.OwnerType ownerType, Long ownerId);

    List<FinanceAccountResponseDto> getAllAccounts();

    FinanceAccountResponseDto updateAccount(Long id, FinanceAccountResponseDto dto);

    void deleteAccount(Long id);

    List<FinanceAccountResponseDto> getAccountsByOwnerId(Long ownerId);

    CreateIncomeRequestDto setIncome(FinanceAccountResponseDto account, BigDecimal income);

    CreateExpenseRequestDto setExpense(EnumAll.ExpenseCategory category, BigDecimal expense, FinanceAccountResponseDto account, String type);
}
