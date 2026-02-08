package com.pet.businessdomain.financeservice.controller;

import com.pet.businessdomain.financeservice.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.financeservice.dto.ExpenseResponseDto;
import com.pet.businessdomain.financeservice.entities.ExpenseEntity;
import com.pet.businessdomain.financeservice.entities.IncomeEntity;
import com.pet.businessdomain.financeservice.repository.ExpenseRepository;
import com.pet.businessdomain.financeservice.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    @Autowired
    private final ExpenseService expenseService;
    @Autowired
    private final ExpenseRepository expenseRepository;

    @PostMapping("/{accountId}")
    public ExpenseResponseDto addExpense(
            @PathVariable(name = "accountId") Long accountId,
            @RequestBody CreateExpenseRequestDto dto
    ) {
        return expenseService.addExpense(accountId, dto);
    }
    @GetMapping
    public List<ExpenseEntity> getIncomes() {
        return expenseRepository.findAll();
    }
    @GetMapping("/{accountId}")
    public List<CreateExpenseRequestDto> getExpenses(@PathVariable(name = "accountId") Long accountId) {
        return expenseService.getExpenses(accountId);
    }

    @PutMapping("/{expenseId}/deactivate")
    public void deactivateExpense(@PathVariable(name = "expenseId") Long expenseId) {
        expenseService.deactivateExpense(expenseId);
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        expenseRepository.deleteAll();
    }
}

