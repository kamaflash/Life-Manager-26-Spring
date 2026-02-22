package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.TransactionResponseDto;
import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    List<TransactionResponseDto> getTransactionsByAccount(Long accountId);
    Page<TransactionResponseDto> getTransactionsByAccount(Long accountId, Pageable pageable);
    BigDecimal sum(List<TransactionResponseDto> transactions, Enum.TransactionType type);
}
