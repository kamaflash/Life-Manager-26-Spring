package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.JobContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobContractRepository extends JpaRepository<JobContractEntity, Long>,
        JpaSpecificationExecutor<JobContractEntity> {

    Optional<JobContractEntity> findByApplicationId(Long applicationId);
    List<JobContractEntity> findByCharacterId(Long characterId);
    List<JobContractEntity> findByCharacterIdAndStatus(Long characterId, String status);
}