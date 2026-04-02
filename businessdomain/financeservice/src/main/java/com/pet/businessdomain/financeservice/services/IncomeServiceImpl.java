package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.IncomeEntity;
import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.financeservice.mapper.FinanceAccountMapper;
import com.pet.businessdomain.financeservice.mapper.IncomeMapper;
import com.pet.businessdomain.financeservice.mapper.TransactionMapper;
import com.pet.businessdomain.financeservice.repository.FinanceAccountRepository;
import com.pet.businessdomain.financeservice.repository.IncomeRepository;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import com.pet.businessdomain.shareddto.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.shareddto.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.shareddto.dto.IncomeResponseDto;
import com.pet.businessdomain.shareddto.dto.TransactionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private FinanceAccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public IncomeResponseDto addIncome(Long accountId, CreateIncomeRequestDto dto) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        if (dto == null || dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Income amount must be positive");
        }

        FinanceAccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Finance account not found with id: " + accountId));

        IncomeEntity income = incomeMapper.fromCreate(dto);
        income.setAccount(account);
        incomeRepository.save(income);

        TransactionEntity tx = new TransactionEntity();
        tx.setAccount(account);
        tx.setType(EnumAll.TransactionType.INCOME);
        tx.setAmount(income.getAmount());
        tx.setDescription(income.getSource());
        tx.setExecutedAt(LocalDateTime.now());
        tx.setCategory(income.getCategory());
        transactionRepository.save(tx);

        account.setBalance(account.getBalance().add(income.getAmount()));
        accountRepository.save(account);

        return incomeMapper.toDto(income);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreateIncomeRequestDto> getIncomes(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        return incomeRepository.findByAccount_Id(accountId)
                .stream()
                .map(incomeMapper::toDtoCreate)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransaction(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        return transactionRepository.findByAccount_Id(accountId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public void deactivateIncome(Long incomeId) {
        if (incomeId == null) {
            throw new IllegalArgumentException("Income id is required");
        }

        IncomeEntity income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + incomeId));

        if (!income.isActive()) {
            throw new RuntimeException("Income already deactivated");
        }

        income.setActive(false);
        incomeRepository.save(income);
    }
    @Override

    @Transactional
    public CreateIncomeRequestDto mapperCreateIncomesInit(
            FinanceAccountResponseDto accountDto,
            BigDecimal income
    ) {

        FinanceAccountEntity account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        CreateIncomeRequestDto incomeDto = new CreateIncomeRequestDto();
        incomeDto.setAmount(income);
        incomeDto.setFrequency(EnumAll.Frequency.MONTHLY);
        incomeDto.setSource("Ayuda familiar");
        incomeDto.setExternalRefId(accountDto.getId());
        incomeDto.setExternalRefType("");
        incomeDto.setCategory( EnumAll.ExpenseCategory.OTHER);
        IncomeEntity entity = incomeMapper.fromCreate(incomeDto);
        entity.setAccount(account);      // 🔥 AQUÍ
        entity.setActive(true);

        incomeRepository.save(entity);
        TransactionEntity tx = new TransactionEntity();
        tx.setAccount(account);
        tx.setType(EnumAll.TransactionType.INCOME);
        tx.setAmount(incomeDto.getAmount());
        tx.setDescription(incomeDto.getSource());
        tx.setExecutedAt(LocalDateTime.now());
        tx.setCategory(entity.getCategory());
        transactionRepository.save(tx);

        return incomeDto;
    }

    @Override
    public List<IncomeResponseDto> getIncomesByExternalRefId(Long externalRefId) {
        return incomeRepository.findByExternalRefId(externalRefId);
    }
}
