package com.pet.businessdomain.financeservice.controller;

import com.pet.businessdomain.financeservice.dto.TransactionResponseDto;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import com.pet.businessdomain.financeservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    private final TransactionService transactionService;
    @Autowired
    private final TransactionRepository transactionRepository;

    @GetMapping("/{accountId}")
    public List<TransactionResponseDto> getTransactions(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }
    @DeleteMapping("/all")
    public void deleteAll() {
        transactionRepository.deleteAll();
    }
}

