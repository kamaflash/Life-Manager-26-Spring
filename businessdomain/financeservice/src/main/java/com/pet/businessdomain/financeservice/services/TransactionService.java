package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.TransactionResponseDto;

import java.util.List;

public interface TransactionService {

    List<TransactionResponseDto> getTransactionsByAccount(Long accountId);
}
