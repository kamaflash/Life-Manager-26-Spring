package com.pet.businessdomain.financeservice.repository;

import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinanceAccountRepository extends JpaRepository<FinanceAccountEntity, Long> {

    Optional<FinanceAccountEntity> findByOwnerTypeAndOwnerId(
            Enum.OwnerType ownerType,
            Long ownerId
    );
    Optional<FinanceAccountEntity> findByOwnerId(
            Long ownerId
    );
    List<FinanceAccountEntity> findAllByOwnerId(Long ownerId);


}
