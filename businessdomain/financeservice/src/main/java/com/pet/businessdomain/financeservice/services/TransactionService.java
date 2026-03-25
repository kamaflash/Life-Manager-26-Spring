package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.dto.TransactionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    List<TransactionResponseDto> getTransactionsByAccount(Long accountId);
    Page<TransactionResponseDto> getTransactionsByAccount(Long accountId, Pageable pageable);
    BigDecimal sum(List<TransactionResponseDto> transactions, EnumAll.TransactionType type);
}
