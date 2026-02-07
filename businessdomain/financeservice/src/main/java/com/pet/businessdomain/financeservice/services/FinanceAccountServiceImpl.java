package com.pet.businessdomain.financeservice.services;

import com.pet.businessdomain.financeservice.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import com.pet.businessdomain.financeservice.mapper.FinanceAccountMapper;
import com.pet.businessdomain.financeservice.repository.FinanceAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return accountRepository
                .findAllByOwnerId(ownerId)
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }
}
