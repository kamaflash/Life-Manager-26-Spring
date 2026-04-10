package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

    // ===== BÚSQUEDAS BÁSICAS =====
    List<MissionEntity> findByCharacterJobId(Long characterJobId);

    // ===== MISIONES ACTIVAS (simplificada - sin cálculo de tiempo) =====
    List<MissionEntity> findByCharacterJobIdAndCompletedFalse(Long characterJobId);

    // ===== MISIONES COMPLETADAS =====
    List<MissionEntity> findByCharacterJobIdAndCompletedTrue(Long characterJobId);

    // ===== MISIONES POR DIFICULTAD =====
    List<MissionEntity> findByDifficulty(String difficulty);

    // ===== MISIONES EXPIRADAS (versión simplificada - sin INTERVAL) =====
    @Query("SELECT m FROM MissionEntity m WHERE m.completed = false AND m.deadlineHours IS NOT NULL " +
            "AND m.assignedAt < :expirationTime")
    List<MissionEntity> findExpiredMissions(@Param("expirationTime") LocalDateTime expirationTime);

    // ===== MISIONES PRÓXIMAS A EXPIRAR =====
    @Query("SELECT m FROM MissionEntity m WHERE m.completed = false AND m.deadlineHours IS NOT NULL " +
            "AND m.assignedAt BETWEEN :startTime AND :endTime")
    List<MissionEntity> findMissionsExpiringSoon(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    // ===== CONTADOR DE COMPLETADAS =====
    @Query("SELECT COUNT(CASE WHEN m.completed = true THEN 1 END) * 100.0 / COUNT(m) " +
            "FROM MissionEntity m WHERE m.characterJobId = :jobId")
    Double calculateCompletionRate(@Param("jobId") Long characterJobId);

    // ===== RECOMPENSAS TOTALES =====
    @Query("SELECT SUM(m.xpReward), SUM(m.salaryBonus), SUM(m.reputationGain) " +
            "FROM MissionEntity m WHERE m.characterJobId = :jobId AND m.completed = true")
    List<Object[]> getTotalRewards(@Param("jobId") Long characterJobId);

    // ===== COMPLETAR MISIÓN =====
    @Modifying
    @Transactional
    @Query("UPDATE MissionEntity m SET m.completed = true, m.completedAt = CURRENT_TIMESTAMP WHERE m.id = :id")
    void completeMission(@Param("id") Long id);
}