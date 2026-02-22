package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.*;
import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.IncomeEntity;
import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import com.pet.businessdomain.financeservice.mapper.FinanceAccountMapper;
import com.pet.businessdomain.financeservice.mapper.IncomeMapper;
import com.pet.businessdomain.financeservice.mapper.TransactionMapper;
import com.pet.businessdomain.financeservice.repository.FinanceAccountRepository;
import com.pet.businessdomain.financeservice.repository.IncomeRepository;
import com.pet.businessdomain.financeservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private final IncomeRepository incomeRepository;

    @Autowired
    private final FinanceAccountRepository accountRepository;

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final IncomeMapper incomeMapper;


    @Autowired
    private final TransactionMapper transactionMapper;


    @Autowired
    private final FinanceAccountMapper financeAccountMapper;

    @Override
    public IncomeResponseDto addIncome(Long accountId, CreateIncomeRequestDto dto) {
        FinanceAccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Finance account not found"));

        IncomeEntity income = incomeMapper.fromCreate(dto);
        income.setAccount(account);
        incomeRepository.save(income);

        TransactionEntity tx = new TransactionEntity();
        tx.setAccount(account);
        tx.setType(Enum.TransactionType.INCOME);
        tx.setAmount(income.getAmount());
        tx.setDescription(income.getSource());
        tx.setExecutedAt(LocalDateTime.now());
        transactionRepository.save(tx);

        account.setBalance(account.getBalance().add(income.getAmount()));
        accountRepository.save(account);

        return incomeMapper.toDto(income);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreateIncomeRequestDto> getIncomes(Long accountId) {
        return incomeRepository.findByAccount_Id(accountId)
                .stream()
                .map(incomeMapper::toDtoCreate)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransaction(Long accountId) {
        return transactionRepository.findByAccount_Id(accountId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public void deactivateIncome(Long incomeId) {
        IncomeEntity income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

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
        incomeDto.setFrequency(Enum.Frequency.MONTHLY);
        incomeDto.setSource("Ayuda familiar");
        incomeDto.setExternalRefId(accountDto.getId());
        incomeDto.setExternalRefType("");

        IncomeEntity entity = incomeMapper.fromCreate(incomeDto);
        entity.setAccount(account);      // 🔥 AQUÍ
        entity.setActive(true);

        incomeRepository.save(entity);
        TransactionEntity tx = new TransactionEntity();
        tx.setAccount(account);
        tx.setType(Enum.TransactionType.INCOME);
        tx.setAmount(incomeDto.getAmount());
        tx.setDescription(incomeDto.getSource());
        tx.setExecutedAt(LocalDateTime.now());
        transactionRepository.save(tx);

        return incomeDto;
    }

    @Override
    public List<IncomeResponseDto> getIncomesByExternalRefId(Long externalRefId) {
        return incomeRepository.findByExternalRefId(externalRefId);
    }
}
