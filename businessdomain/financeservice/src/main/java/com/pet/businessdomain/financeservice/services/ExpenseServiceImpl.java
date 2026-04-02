package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.shareddto.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.shareddto.dto.ExpenseResponseDto;
import com.pet.businessdomain.shareddto.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.financeservice.entities.ExpenseEntity;
import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.financeservice.mapper.ExpenseMapper;
import com.pet.businessdomain.financeservice.repository.ExpenseRepository;
import com.pet.businessdomain.financeservice.repository.FinanceAccountRepository;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private FinanceAccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponseDto addExpense(Long accountId, CreateExpenseRequestDto dto) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        if (dto == null || dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }

        FinanceAccountEntity account = accountRepository.findByOwnerId(accountId)
                .orElseThrow(() -> new RuntimeException("Finance account not found with account id: " + accountId));

        ExpenseEntity expense = expenseMapper.fromCreate(dto);
        expense.setAccount(account);
        expenseRepository.save(expense);

        TransactionEntity tx = new TransactionEntity();
        tx.setAccount(account);
        tx.setType(EnumAll.TransactionType.EXPENSE);
        tx.setAmount(expense.getAmount());
        tx.setDescription(expense.getConcept());
        tx.setExecutedAt(LocalDateTime.now());
        tx.setCategory(expense.getCategory());
        transactionRepository.save(tx);

        account.setBalance(account.getBalance().subtract(expense.getAmount()));
        accountRepository.save(account);

        return expenseMapper.toDto(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreateExpenseRequestDto> getExpenses(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        return expenseRepository.findByAccount_Id(accountId)
                .stream()
                .map(expenseMapper::toDtoCreate)
                .toList();
    }

    @Override
    public void deactivateExpense(Long expenseId) {
        if (expenseId == null) {
            throw new IllegalArgumentException("Expense id is required");
        }

        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));

        expense.setActive(false);
        expenseRepository.save(expense);
    }
    @Override
    @Transactional
    public CreateExpenseRequestDto mapperCreateExpense(
            BigDecimal amount,
            EnumAll.ExpenseCategory category,
            String concept,
            FinanceAccountResponseDto accountDto
    ) {

        FinanceAccountEntity account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        CreateExpenseRequestDto expenseDto = new CreateExpenseRequestDto();
        expenseDto.setAmount(amount);
        expenseDto.setFrequency(EnumAll.Frequency.MONTHLY);
        expenseDto.setCategory(category);
        expenseDto.setConcept(concept);
        expenseDto.setStartDate(LocalDate.now());
        expenseDto.setEssential(true);
        expenseDto.setEndDate(null);
        expenseDto.setExternalRefId(accountDto.getId());
        expenseDto.setExternalRefType("");

        ExpenseEntity expenseEntity = expenseMapper.fromCreate(expenseDto);

        expenseEntity.setAccount(account); // 🔥 CLAVE
        TransactionEntity tx = new TransactionEntity();
        tx.setAccount(account);
        tx.setType(EnumAll.TransactionType.EXPENSE);
        tx.setCategory(expenseDto.getCategory());
        tx.setAmount(expenseDto.getAmount());
        tx.setDescription(expenseDto.getConcept());
        tx.setExecutedAt(LocalDateTime.now());
        transactionRepository.save(tx);

        expenseRepository.save(expenseEntity);

        return expenseDto;
    }

}
