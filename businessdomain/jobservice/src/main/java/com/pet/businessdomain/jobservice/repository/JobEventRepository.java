package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.JobEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobEventRepository extends JpaRepository<JobEventEntity, Long> {

    // ===== BÚSQUEDAS BÁSICAS =====
    List<JobEventEntity> findByCharacterJobId(Long characterJobId);
    List<JobEventEntity> findByType(String type);  // ✅ Cambiado de eventType a type
    List<JobEventEntity> findByResolvedFalse();

    // ===== EVENTOS RECIENTES =====
    List<JobEventEntity> findTop5ByCharacterJobIdOrderByOccurredAtDesc(Long characterJobId);

    // ===== EVENTOS POSITIVOS =====
    @Query("SELECT e FROM JobEventEntity e WHERE e.characterJobId = :jobId " +
            "AND e.type IN ('BONUS', 'PROMOTION', 'PROJECT_SUCCESS', 'OFFER_FROM_RIVAL') " +  // ✅ type en lugar de eventType
            "ORDER BY e.occurredAt DESC")
    List<JobEventEntity> findPositiveEvents(@Param("jobId") Long jobId);

    // ===== EVENTOS NEGATIVOS =====
    @Query("SELECT e FROM JobEventEntity e WHERE e.characterJobId = :jobId " +
            "AND e.type IN ('CONFLICT', 'PROJECT_FAILURE', 'BURNOUT', 'DEMOTION', 'TEAM_RESTRUCTURE') " +  // ✅ type en lugar de eventType
            "ORDER BY e.occurredAt DESC")
    List<JobEventEntity> findNegativeEvents(@Param("jobId") Long jobId);

    // ===== EVENTOS POR RANGO DE FECHAS =====
    List<JobEventEntity> findByOccurredAtBetween(LocalDateTime start, LocalDateTime end);

    // ===== ÚLTIMO EVENTO =====
    Optional<JobEventEntity> findFirstByCharacterJobIdOrderByOccurredAtDesc(Long characterJobId);

    // ===== CONTADOR POR TIPO =====
    @Query("SELECT e.type, COUNT(e) FROM JobEventEntity e " +  // ✅ type en lugar de eventType
            "WHERE e.characterJobId = :jobId GROUP BY e.type")
    List<Object[]> countEventsByType(@Param("jobId") Long characterJobId);
}