package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.TransactionResponseDto;
import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.financeservice.mapper.TransactionMapper;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
