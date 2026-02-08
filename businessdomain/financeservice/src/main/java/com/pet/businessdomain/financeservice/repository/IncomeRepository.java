package com.pet.businessdomain.financeservice.repository;

import com.pet.businessdomain.financeservice.dto.IncomeResponseDto;
import com.pet.businessdomain.financeservice.entities.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

    List<IncomeEntity> findByAccount_Id(Long accountId);
    List<IncomeResponseDto> findByExternalRefId(Long externalRefId);
}
