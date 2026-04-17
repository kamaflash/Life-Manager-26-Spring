package com.pet.businessdomain.eventservice.repository;

import com.pet.businessdomain.eventservice.entities.MissionEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    Optional<MissionEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<MissionEntity> findByHiddenFalse();
    List<MissionEntity> findByCategory(EnumAll.MissionCategory category);
    List<MissionEntity> findByType(EnumAll.MissionType type);
    List<MissionEntity> findByAutoAcceptTrue();
    List<MissionEntity> findByUnlocksMissionCodesContaining(String missionCode);

    @Query("SELECT MAX(m.orderInChain) FROM MissionEntity m WHERE m.chainId = :chainId")
    Integer findMaxOrderInChain(Long chainId);
}