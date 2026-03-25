package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.shareddto.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.shareddto.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.shareddto.dto.IncomeResponseDto;
import com.pet.businessdomain.shareddto.dto.TransactionResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeService {

    IncomeResponseDto addIncome(Long accountId, CreateIncomeRequestDto dto);

    List<CreateIncomeRequestDto> getIncomes(Long accountId);

    List<TransactionResponseDto> getTransaction(Long accountId);
    void deactivateIncome(Long incomeId);

    CreateIncomeRequestDto mapperCreateIncomesInit(FinanceAccountResponseDto sfinanceAccountResponseDto, BigDecimal income);

    List<IncomeResponseDto> getIncomesByExternalRefId(Long externalRefId);
}
