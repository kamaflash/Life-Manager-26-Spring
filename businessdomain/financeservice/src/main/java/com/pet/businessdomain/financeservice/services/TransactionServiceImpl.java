package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.TransactionResponseDto;
import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import com.pet.businessdomain.financeservice.mapper.TransactionMapper;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionResponseDto> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findByAccount_Id(accountId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByAccount(Long accountId, Pageable pageable) {
        return transactionRepository
                .findByAccount_Id(accountId, pageable)
                .map(transactionMapper::toDto);
    }

    @Override
    public BigDecimal sum(List<TransactionResponseDto> transactions, Enum.TransactionType type) {
        return transactions.stream()
                .filter(t -> type.equals(t.getType()))
                .map(TransactionResponseDto::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
