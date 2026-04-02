package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.financeservice.mapper.FinanceAccountMapper;
import com.pet.businessdomain.financeservice.repository.FinanceAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountRepository accountRepository;

    @Autowired
    private FinanceAccountMapper accountMapper;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public FinanceAccountResponseDto createAccount(FinanceAccountResponseDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Account data is required");
        }

        FinanceAccountEntity entity = accountMapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        FinanceAccountEntity saved = accountRepository.save(entity);
        return accountMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FinanceAccountResponseDto getAccountById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        FinanceAccountEntity entity = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finance account not found with id " + id));
        return accountMapper.toDto(entity);
    }

    @Override
    public FinanceAccountResponseDto getAccountByOwner(EnumAll.OwnerType ownerType, Long ownerId) {
        if (ownerType == null || ownerId == null) {
            throw new IllegalArgumentException("Owner type and owner id are required");
        }
        FinanceAccountEntity entity = accountRepository.findByOwnerTypeAndOwnerId(ownerType, ownerId)
                .orElseThrow(() -> new RuntimeException("Finance account not found with owner id " + ownerId));
        return accountMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanceAccountResponseDto> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FinanceAccountResponseDto updateAccount(Long id, FinanceAccountResponseDto dto) {
        FinanceAccountEntity entity = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finance account not found with id " + id));

        // Actualizamos los campos permitidos
        entity.setBalance(calculateMonthlyBalance(dto));
        entity.setDebt(dto.getDebt());
        entity.setSavings(dto.getSavings());
        entity.setUpdatedAt(LocalDateTime.now());

        FinanceAccountEntity updated = accountRepository.save(entity);
        return accountMapper.toDto(updated);
    }

    @Override
    public void deleteAccount(Long id) {
        FinanceAccountEntity entity = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finance account not found with id " + id));
        accountRepository.delete(entity);
    }

    @Override
    public List<FinanceAccountResponseDto> getAccountsByOwnerId(Long ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner id is required");
        }

        List<FinanceAccountResponseDto> list = accountRepository
                .findAllByOwnerId(ownerId)
                .stream()
                .map(accountMapper::toDto)
                .toList();

        if (list.isEmpty()) {
            return List.of();
        }

        FinanceAccountResponseDto accountDto = list.get(0);
        accountDto.setExpenses(expenseService.getExpenses(accountDto.getId()));
        accountDto.setIncomes(incomeService.getIncomes(accountDto.getId()));
        accountDto.setTransactions(transactionService.getTransactionsByAccount(accountDto.getId()));
        accountDto.setBalance(calculateMonthlyBalance(accountDto));
        return list;
    }

    @Override
    public CreateIncomeRequestDto setIncome(FinanceAccountResponseDto account, BigDecimal income) {
        CreateIncomeRequestDto dtoIncome = incomeService.mapperCreateIncomesInit(account,income);
        return dtoIncome;
    }

    @Override
    public CreateExpenseRequestDto setExpense(EnumAll.ExpenseCategory category, BigDecimal expense,FinanceAccountResponseDto account, String type) {
        CreateExpenseRequestDto dtoExpense = expenseService.mapperCreateExpense(expense, category,type,account);
        return dtoExpense;
    }

    private BigDecimal calculateMonthlyBalance(
            FinanceAccountResponseDto dto
    ) {
        List<TransactionResponseDto> incomess = dto.getTransactions().stream()
                .filter(t -> t.getType() == EnumAll.TransactionType.INCOME)
                .toList();
        List<TransactionResponseDto> expensess = dto.getTransactions().stream()
                .filter(t -> t.getType() == EnumAll.TransactionType.EXPENSE)
                .toList();
// Suma de ingresos
        BigDecimal totalIncome = incomess.stream()
                .map(TransactionResponseDto::getAmount)  // obtenemos amount
                .reduce(BigDecimal.ZERO, BigDecimal::add);

// Suma de gastos
        BigDecimal totalExpense = expensess.stream()
                .map(TransactionResponseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIncome.subtract(totalExpense);
    }
}
