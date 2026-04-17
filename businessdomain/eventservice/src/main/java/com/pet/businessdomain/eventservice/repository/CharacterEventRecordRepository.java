package com.pet.businessdomain.eventservice.repository;

import com.pet.businessdomain.eventservice.entities.CharacterEventRecord;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterEventRecordRepository extends JpaRepository<CharacterEventRecord, Long> {
    List<CharacterEventRecord> findByCharacterId(Long characterId);
    Optional<CharacterEventRecord> findByCharacterIdAndEventId(Long characterId, Long eventId);
    boolean existsByCharacterIdAndEventId(Long characterId, Long eventId);
    long countByEventId(Long eventId);
    long countByEventIdAndStatusIn(Long eventId, List<EnumAll.EventParticipationStatus> statuses);
}