package com.pet.businessdomain.financeservice.controller;

import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.financeservice.mapper.TransactionMapper;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import com.pet.businessdomain.financeservice.services.TransactionService;
import com.pet.businessdomain.shareddto.dto.TransactionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private static final int DEFAULT_SIZE = 10;

    @Autowired
    private final TransactionService transactionService;
    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final TransactionMapper transactionMapper;

    @GetMapping
    public void findAll() {
        transactionRepository.findAll();
    }

    @GetMapping("/{accountId}")
    public List<TransactionResponseDto> getTransactions(@PathVariable(name = "accountId") Long accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }
    @GetMapping("/full/{accountId}")
    public ResponseEntity<?> getTransactionsFull(
            @PathVariable(name = "accountId") Long accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        List<TransactionResponseDto> trasactionsList = transactionService.getTransactionsByAccount(accountId);
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionResponseDto> trasactionPage = transactionService.getTransactionsByAccount(accountId,pageable);

        if (trasactionPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("trasactions", trasactionPage.getContent());
        response.put("list", trasactionsList);
        response.put("incomes", transactionService.sum(trasactionsList, EnumAll.TransactionType.INCOME));
        response.put("expenses", transactionService.sum(trasactionsList, EnumAll.TransactionType.EXPENSE));
        response.put("balance", transactionService.sum(trasactionsList, EnumAll.TransactionType.INCOME).subtract(transactionService.sum(trasactionsList, EnumAll.TransactionType.EXPENSE)));
        response.put("currentPage", trasactionPage.getNumber());
        response.put("totalItems", trasactionPage.getTotalElements());
        response.put("totalPages", trasactionPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/all")
    public void deleteAll() {
        transactionRepository.deleteAll();
    }
}

