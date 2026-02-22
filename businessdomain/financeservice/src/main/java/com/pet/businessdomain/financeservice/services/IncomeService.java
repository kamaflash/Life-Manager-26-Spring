package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.*;

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
