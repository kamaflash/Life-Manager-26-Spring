package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.financeservice.mapper.TransactionMapper;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import com.pet.businessdomain.shareddto.dto.TransactionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public List<TransactionResponseDto> getTransactionsByAccount(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        return transactionRepository.findByAccount_Id(accountId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByAccount(Long accountId, Pageable pageable) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        return transactionRepository
                .findByAccount_Id(accountId, pageable)
                .map(transactionMapper::toDto);
    }

    @Override
    public BigDecimal sum(List<TransactionResponseDto> transactions, EnumAll.TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(TransactionResponseDto::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
