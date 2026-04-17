package com.pet.businessdomain.eventservice.repository;

import com.pet.businessdomain.eventservice.entities.EventEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<EventEntity> findByStatus(EnumAll.EventStatus status);
    List<EventEntity> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            EnumAll.EventStatus status, LocalDateTime now1, LocalDateTime now2);
    List<EventEntity> findByCity(String city);
    List<EventEntity> findByType(EnumAll.EventType type);
    List<EventEntity> findByAutoTriggerTrue();
}