package com.pet.businessdomain.financeservice.repository;

import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByAccount_Id(Long accountId);
    Page<TransactionEntity> findByAccount_Id(Long accountId, Pageable pageable);
}
