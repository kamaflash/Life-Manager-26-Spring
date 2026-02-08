package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.financeservice.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.financeservice.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.financeservice.dto.IncomeResponseDto;
import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import com.pet.businessdomain.financeservice.mapper.FinanceAccountMapper;
import com.pet.businessdomain.financeservice.repository.FinanceAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private final FinanceAccountRepository accountRepository;

    @Autowired
    private final FinanceAccountMapper accountMapper;


    @Autowired
    private final IncomeService incomeService;
    @Autowired
    private final ExpenseService expenseService;

    @Override
    public FinanceAccountResponseDto createAccount(FinanceAccountResponseDto dto) {
        FinanceAccountEntity entity = accountMapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        FinanceAccountEntity saved = accountRepository.save(entity);
        return accountMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FinanceAccountResponseDto getAccountById(Long id) {
        FinanceAccountEntity entity = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finance account not found with id " + id));
        return accountMapper.toDto(entity);
    }

    @Override
    public FinanceAccountResponseDto getAccountByOwner(Enum.OwnerType ownerType, Long ownerId) {
        FinanceAccountEntity entity = accountRepository.findByOwnerTypeAndOwnerId(ownerType,ownerId)
                .orElseThrow(() -> new RuntimeException("Finance account not found with id " + ownerId));
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
        entity.setBalance(dto.getBalance());
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

        List<FinanceAccountResponseDto> list = accountRepository
                .findAllByOwnerId(ownerId)
                .stream()
                .map(accountMapper::toDto)
                .toList();

        FinanceAccountResponseDto accountDto = list.getFirst();
        accountDto.setExpenses(expenseService.getExpenses(accountDto.getId()));
        accountDto.setIncomes(incomeService.getIncomes(accountDto.getId()));
        return list;
    }

    @Override
    public CreateIncomeRequestDto setIncome(FinanceAccountResponseDto account, BigDecimal income) {
        CreateIncomeRequestDto dtoIncome = incomeService.mapperCreateIncomesInit(account,income);
        return dtoIncome;
    }

    @Override
    public CreateExpenseRequestDto setExpense(Enum.ExpenseCategory category, BigDecimal expense,FinanceAccountResponseDto account, String type) {
        CreateExpenseRequestDto dtoExpense = expenseService.mapperCreateExpense(expense, category,type,account);
        return dtoExpense;
    }
}
