package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.CharacterJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterJobRepository extends JpaRepository<CharacterJobEntity, Long> {

    // Trabajos activos de un personaje
    List<CharacterJobEntity> findByCharacterIdAndActiveTrue(Long characterId);

    // Todos los trabajos de un personaje (historial)
    List<CharacterJobEntity> findByCharacterId(Long characterId);

    // Trabajo actual de un personaje (el más reciente activo)
    Optional<CharacterJobEntity> findFirstByCharacterIdAndActiveTrueOrderByStartDateDesc(Long characterId);

    // Verificar si un personaje tiene trabajo activo
    boolean existsByCharacterIdAndActiveTrue(Long characterId);

    // Trabajos por vacante
    List<CharacterJobEntity> findByVacancyId(Long vacancyId);

    // Trabajos por empresa
    @Query("SELECT cj FROM CharacterJobEntity cj " +
            "WHERE cj.vacancy.position.company.id = :companyId AND cj.active = true")
    List<CharacterJobEntity> findByCompanyId(@Param("companyId") Long companyId);

    // Trabajos con bajo rendimiento
    List<CharacterJobEntity> findByActiveTrueAndPerformanceLessThan(Integer performance);

    // Trabajos con alto estrés
    List<CharacterJobEntity> findByActiveTrueAndStressLevelGreaterThan(Integer stressLevel);

    // Trabajos con alta satisfacción
    List<CharacterJobEntity> findByActiveTrueAndSatisfactionGreaterThan(Integer satisfaction);

    // Contar empleados por empresa
    @Query("SELECT COUNT(DISTINCT cj.characterId) FROM CharacterJobEntity cj " +
            "WHERE cj.vacancy.position.company.id = :companyId AND cj.active = true")
    Integer countEmployeesByCompany(@Param("companyId") Long companyId);

    // Actualizar rendimiento
    @Modifying
    @Transactional
    @Query("UPDATE CharacterJobEntity cj SET cj.performance = :performance WHERE cj.id = :id")
    void updatePerformance(@Param("id") Long id, @Param("performance") Integer performance);

    // Actualizar satisfacción
    @Modifying
    @Transactional
    @Query("UPDATE CharacterJobEntity cj SET cj.satisfaction = :satisfaction WHERE cj.id = :id")
    void updateSatisfaction(@Param("id") Long id, @Param("satisfaction") Integer satisfaction);

    // Incrementar promociones
    @Modifying
    @Transactional
    @Query("UPDATE CharacterJobEntity cj SET cj.promotionsReceived = cj.promotionsReceived + 1, " +
            "cj.lastPromotionDate = CURRENT_TIMESTAMP WHERE cj.id = :id")
    void incrementPromotions(@Param("id") Long id);

    // Finalizar trabajo (despedido o renuncia)
    @Modifying
    @Transactional
    @Query("UPDATE CharacterJobEntity cj SET cj.active = false, cj.endDate = :endDate WHERE cj.id = :id")
    void terminateJob(@Param("id") Long id, @Param("endDate") LocalDate endDate);
}