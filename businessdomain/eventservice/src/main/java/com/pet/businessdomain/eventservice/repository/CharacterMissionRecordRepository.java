package com.pet.businessdomain.eventservice.repository;

import com.pet.businessdomain.eventservice.entities.CharacterMissionRecord;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterMissionRecordRepository extends JpaRepository<CharacterMissionRecord, Long> {
    List<CharacterMissionRecord> findByCharacterIdAndStatus(Long characterId, EnumAll.MissionStatus status);
    Page<CharacterMissionRecord> findByCharacterId(Long characterId, Pageable pageable);
    Optional<CharacterMissionRecord> findByCharacterIdAndMissionId(Long characterId, Long missionId);
    Optional<CharacterMissionRecord> findByCharacterIdAndMissionIdAndStatus(
            Long characterId, Long missionId, EnumAll.MissionStatus status);
    boolean existsByCharacterIdAndMissionIdAndStatus(
            Long characterId, Long missionId, EnumAll.MissionStatus status);
    long countByCharacterIdAndStatus(Long characterId, EnumAll.MissionStatus status);
}