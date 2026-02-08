package com.pet.businessdomain.financeservice.controller;

import com.pet.businessdomain.financeservice.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.financeservice.dto.IncomeResponseDto;
import com.pet.businessdomain.financeservice.repository.IncomeRepository;
import com.pet.businessdomain.financeservice.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {
    @Autowired
    private final IncomeService incomeService;
    @Autowired
    private final IncomeRepository incomeRepository;

    @PostMapping("/{accountId}")
    public IncomeResponseDto addIncome(
            @PathVariable(name = "accountId") Long accountId,
            @RequestBody CreateIncomeRequestDto dto
    ) {
        return incomeService.addIncome(accountId, dto);
    }

    @GetMapping("/{accountId}")
    public List<CreateIncomeRequestDto> getIncomes(@PathVariable(name = "accountId") Long accountId) {
        return incomeService.getIncomes(accountId);
    }

    @PutMapping("/{incomeId}/deactivate")
    public void deactivateIncome(@PathVariable(name = "incomeId") Long incomeId) {
        incomeService.deactivateIncome(incomeId);
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        incomeRepository.deleteAll();
    }
}

