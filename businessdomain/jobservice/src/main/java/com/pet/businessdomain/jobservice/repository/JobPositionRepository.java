package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPositionEntity, Long> {

    // ===== BÚSQUEDAS BÁSICAS =====
    List<JobPositionEntity> findByCompanyId(Long companyId);
    List<JobPositionEntity> findByCompanyIdAndActiveTrue(Long companyId);
    List<JobPositionEntity> findByCategory(JobCategory category);
    List<JobPositionEntity> findByLevel(String level);
    List<JobPositionEntity> findByCareerPath(String careerPath);
    List<JobPositionEntity> findByTitleContainingIgnoreCase(String title);

    List<JobPositionEntity> findByActiveTrue();

    List<JobPositionEntity> findAll();

    // ===== VACANTES ACTIVAS =====
    @Query("SELECT DISTINCT p FROM JobPositionEntity p " +
            "JOIN p.vacancies v " +
            "WHERE v.active = true AND v.availableSlots > 0")
    List<JobPositionEntity> findPositionsWithActiveVacancies();

    // ===== CONTADORES =====
    @Query("SELECT p.id, COUNT(v) FROM JobPositionEntity p " +
            "LEFT JOIN p.vacancies v " +
            "WHERE v.active = true AND v.availableSlots > 0 " +
            "GROUP BY p.id")
    List<Object[]> countActiveVacanciesByPosition();

    // ===== VALIDACIONES =====
    boolean existsByCompanyIdAndTitleIgnoreCase(Long companyId, String title);
}