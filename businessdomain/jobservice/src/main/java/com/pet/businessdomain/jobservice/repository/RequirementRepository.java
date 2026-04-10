package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.RequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<RequirementEntity, Long> {

    // ✅ Usando @Query para evitar problemas de nomenclatura
    @Query("SELECT r FROM RequirementEntity r WHERE r.vacancy.id = :vacancyId")
    List<RequirementEntity> findByVacancyId(@Param("vacancyId") Long vacancyId);

    @Query("SELECT r FROM RequirementEntity r WHERE r.vacancy.id = :vacancyId AND r.mandatory = true")
    List<RequirementEntity> findByVacancyIdAndMandatoryTrue(@Param("vacancyId") Long vacancyId);

    @Query("SELECT r FROM RequirementEntity r WHERE r.vacancy.id = :vacancyId AND r.mandatory = false")
    List<RequirementEntity> findByVacancyIdAndMandatoryFalse(@Param("vacancyId") Long vacancyId);

    @Query("SELECT r FROM RequirementEntity r WHERE r.type = :type")
    List<RequirementEntity> findByType(@Param("type") String type);

    @Modifying
    @Transactional
    @Query("DELETE FROM RequirementEntity r WHERE r.vacancy.id = :vacancyId")
    void deleteByVacancyId(@Param("vacancyId") Long vacancyId);

    @Query("SELECT COUNT(r) > 0 FROM RequirementEntity r WHERE r.vacancy.id = :vacancyId AND r.type = :type")
    boolean existsByVacancyIdAndType(@Param("vacancyId") Long vacancyId, @Param("type") String type);

    @Query("SELECT r FROM RequirementEntity r WHERE r.skill_key = :skill_key")
    List<RequirementEntity> findByKey(@Param("skill_key") String skill_key);

    @Query("SELECT r FROM RequirementEntity r WHERE r.vacancy.id = :vacancyId AND r.type = :type")
    List<RequirementEntity> findByVacancyIdAndType(@Param("vacancyId") Long vacancyId, @Param("type") String type);
}