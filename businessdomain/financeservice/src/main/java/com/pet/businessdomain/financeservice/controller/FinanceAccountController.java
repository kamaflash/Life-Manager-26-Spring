package com.pet.businessdomain.financeservice.controller;

import com.pet.businessdomain.financeservice.dto.*;
import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import com.pet.businessdomain.financeservice.mapper.FinanceAccountMapper;
import com.pet.businessdomain.financeservice.repository.FinanceAccountRepository;
import com.pet.businessdomain.financeservice.services.ExpenseService;
import com.pet.businessdomain.financeservice.services.FinanceAccountService;
import com.pet.businessdomain.financeservice.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class FinanceAccountController {
    private static final int DEFAULT_SIZE = 10;

    @Autowired
    private final FinanceAccountService accountService;

    @Autowired
    private final IncomeService incomeService;

    @Autowired
    private final ExpenseService expenseService;

    @Autowired
    private final FinanceAccountRepository financeAccountRepository;


    @Autowired
    private final FinanceAccountMapper financeAccountMapper;

    @PostMapping
    public FinanceAccountResponseDto createAccount(@RequestBody FinanceAccountResponseDto dto) {
        FinanceAccountResponseDto resp = accountService.createAccount(dto);
        return resp;
    }
    @PostMapping("/{income}/{expense}")
    public FinanceAccountResponseDto createAccount(@RequestBody FinanceAccountResponseDto dto,@PathVariable(name = "income") BigDecimal income,@PathVariable(name = "expense") BigDecimal expense) {
        FinanceAccountResponseDto resp = accountService.createAccount(dto);
        CreateIncomeRequestDto dtoIncome = incomeService.mapperCreateIncomesInit(resp,income);
        CreateExpenseRequestDto dtoExpense = expenseService.mapperCreateExpense(expense, Enum.ExpenseCategory.HOUSING,"Habitación",resp);
        CreateExpenseRequestDto dtoExpense2 = expenseService.mapperCreateExpense(BigDecimal.valueOf(200), Enum.ExpenseCategory.FOOD,"Alimentación",resp);

        List<CreateIncomeRequestDto> listIncomes = new ArrayList();
        listIncomes.add(dtoIncome);
        resp.setIncomes(listIncomes);
        List<CreateExpenseRequestDto> listExpenses = new ArrayList();
        listExpenses.add(dtoExpense);
        listExpenses.add(dtoExpense2);
        resp.setExpenses(listExpenses);
        return resp;
    }
    @PostMapping("/full")
    public FinanceAccountResponseDto createAccountFull(@RequestBody FinanceAccountResponseDto dto) {
        return accountService.createAccount(dto);
    }

    @GetMapping
    public ResponseEntity<?> getAllFormations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FinanceAccountEntity> formationsPage = financeAccountRepository.findAll(pageable);

        if (formationsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("finances", financeAccountMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}")
    public List<FinanceAccountResponseDto> getAccountsByOwnerId(
            @PathVariable(name = "ownerId") Long ownerId
    ) {
        return accountService.getAccountsByOwnerId(ownerId);
    }
    @GetMapping("/owner/full/{ownerId}")
    public List<FinanceAccountResponseDto> getAccountsByOwnerIdfull(
            @PathVariable(name = "ownerId") Long ownerId
    ) {
        return accountService.getAccountsByOwnerId(ownerId);
    }

    @GetMapping("/{ownerType}/{ownerId}")
    public FinanceAccountResponseDto getAccount(
            @PathVariable(name = "ownerType")  Enum.OwnerType ownerType,
            @PathVariable(name = "ownerId")  Long ownerId
    ) {
        return accountService.getAccountByOwner(ownerType, ownerId);
    }




    @PutMapping("/{accountId}")
    public FinanceAccountResponseDto updateAccount(
            @PathVariable(name = "accountId") Long accountId,
            @RequestBody FinanceAccountResponseDto dto
    ) {
        return accountService.updateAccount(accountId, dto);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable(name = "accountId") Long accountId) {
        accountService.deleteAccount(accountId);
    }
    @DeleteMapping("/all")
    public void deleteAccountAll() {
        financeAccountRepository.deleteAll();
    }
}
