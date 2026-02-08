package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.financeservice.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.financeservice.dto.IncomeResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeService {

    IncomeResponseDto addIncome(Long accountId, CreateIncomeRequestDto dto);

    List<CreateIncomeRequestDto> getIncomes(Long accountId);

    void deactivateIncome(Long incomeId);

    CreateIncomeRequestDto mapperCreateIncomesInit(FinanceAccountResponseDto sfinanceAccountResponseDto, BigDecimal income);

    List<IncomeResponseDto> getIncomesByExternalRefId(Long externalRefId);
}
