package com.pet.businessdomain.eventservice.repository;

import com.pet.businessdomain.eventservice.entities.MissionChainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionChainRepository extends JpaRepository<MissionChainEntity, Long> {
    Optional<MissionChainEntity> findByCode(String code);
    boolean existsByCode(String code);
}