package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobVacancyRepository extends JpaRepository<JobVacancyEntity, Long> {

    // ===== BÚSQUEDAS BÁSICAS CON JOIN FETCH =====

    @Query("SELECT DISTINCT v FROM JobVacancyEntity v " +
            "LEFT JOIN FETCH v.position p " +
            "LEFT JOIN FETCH p.company c")
    List<JobVacancyEntity> findAllWithRelations();

    @Query("SELECT DISTINCT v FROM JobVacancyEntity v " +
            "LEFT JOIN FETCH v.position p " +
            "LEFT JOIN FETCH p.company c " +
            "WHERE v.id = :id")
    JobVacancyEntity findByIdWithRelations(@Param("id") Long id);
    @Query("SELECT DISTINCT jv FROM JobVacancyEntity jv " +
            "LEFT JOIN FETCH jv.requirements " +
            "WHERE jv.id = :id")
    Optional<JobVacancyEntity> findByIdWithRequirements(@Param("id") Long id);
    @Query("SELECT DISTINCT v FROM JobVacancyEntity v " +
            "LEFT JOIN FETCH v.position p " +
            "LEFT JOIN FETCH p.company c " +
            "WHERE v.position.id = :positionId")
    List<JobVacancyEntity> findByPositionIdWithRelations(@Param("positionId") Long positionId);

    @Query("SELECT DISTINCT v FROM JobVacancyEntity v " +
            "LEFT JOIN FETCH v.position p " +
            "LEFT JOIN FETCH p.company c " +
            "WHERE v.active = true")
    List<JobVacancyEntity> findByActiveTrueWithRelations();

    @Query("SELECT DISTINCT v FROM JobVacancyEntity v " +
            "LEFT JOIN FETCH v.position p " +
            "LEFT JOIN FETCH p.company c " +
            "WHERE v.position.id = :positionId AND v.active = true")
    List<JobVacancyEntity> findByPositionIdAndActiveTrueWithRelations(@Param("positionId") Long positionId);

    @Query("SELECT DISTINCT v FROM JobVacancyEntity v " +
            "LEFT JOIN FETCH v.position p " +
            "LEFT JOIN FETCH p.company c " +
            "WHERE p.company.id = :companyId AND v.active = true")
    List<JobVacancyEntity> findByCompanyIdWithRelations(@Param("companyId") Long companyId);

    // ===== MÉTODOS ORIGINALES SIN JOIN FETCH (para operaciones que no necesitan los datos) =====
    List<JobVacancyEntity> findByPositionId(Long positionId);
    List<JobVacancyEntity> findByPositionIdAndActiveTrue(Long positionId);
    List<JobVacancyEntity> findByActiveTrue();
    List<JobVacancyEntity> findByContractType(String contractType);
    List<JobVacancyEntity> findByWorkModality(String workModality);
    List<JobVacancyEntity> findByMinSalaryBetween(BigDecimal min, BigDecimal max);

    // ===== VACANTES POR EMPRESA =====
    @Query("SELECT v FROM JobVacancyEntity v " +
            "WHERE v.position.company.id = :companyId AND v.active = true")
    List<JobVacancyEntity> findByCompanyId(@Param("companyId") Long companyId);

    // ===== VACANTES NO EXPIRADAS =====
    @Query("SELECT v FROM JobVacancyEntity v " +
            "WHERE v.active = true AND (v.closingDate IS NULL OR v.closingDate >= CURRENT_DATE)")
    List<JobVacancyEntity> findActiveNotExpired();

    // ===== VACANTES CON SLOTS DISPONIBLES =====
    @Query("SELECT v FROM JobVacancyEntity v " +
            "WHERE v.active = true AND v.availableSlots > 0")
    List<JobVacancyEntity> findWithAvailableSlots();

    // ===== BÚSQUEDA AVANZADA =====
    @Query("""
        SELECT DISTINCT jv FROM JobVacancyEntity jv
        JOIN FETCH jv.position p
        JOIN FETCH p.company c
        WHERE jv.active = true
        AND jv.availableSlots > 0
        AND (:keyword IS NULL OR :keyword = '' OR 
             LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:contractType IS NULL OR :contractType = '' OR jv.contractType = :contractType)
        AND (:workModality IS NULL OR :workModality = '' OR jv.workModality = :workModality)
        AND (:maxSalary IS NULL OR jv.maxSalary >= :maxSalary)
        AND (:minSalary IS NULL OR jv.minSalary <= :minSalary)
        """)
    List<JobVacancyEntity> searchVacancies(
            @Param("keyword") String keyword,
            @Param("contractType") String contractType,
            @Param("workModality") String workModality,
            @Param("maxSalary") BigDecimal maxSalary,
            @Param("minSalary") BigDecimal minSalary
    );

    @Query("""
    SELECT DISTINCT jv FROM JobVacancyEntity jv
    LEFT JOIN FETCH jv.position p
    LEFT JOIN FETCH p.company c
    WHERE jv.active = true
    AND jv.availableSlots > 0
    AND (
        :keyword IS NULL OR :keyword = '' OR 
        LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    AND (
        :positionTitles IS NULL OR :positionTitles = '' OR
        LOWER(p.title) LIKE LOWER(CONCAT('%', :positionTitles, '%'))
    )
    AND (
        :companyNames IS NULL OR :companyNames = '' OR
        LOWER(c.name) LIKE LOWER(CONCAT('%', :companyNames, '%'))
    )
    AND (
        :categories IS NULL OR :categories = '' OR
        LOWER(p.category) LIKE LOWER(CONCAT('%', :categories, '%'))
        OR LOWER(p.category) = 'other'
    )
    AND (
        :contractTypes IS NULL OR :contractTypes = '' OR
        LOWER(jv.contractType) LIKE LOWER(CONCAT('%', :contractTypes, '%'))
    )
    AND (
        :workModalities IS NULL OR :workModalities = '' OR
        LOWER(jv.workModality) LIKE LOWER(CONCAT('%', :workModalities, '%'))
    )
    AND (
        :weeklyHours IS NULL OR :weeklyHours = '' OR
        CAST(jv.weeklyHours AS string) LIKE CONCAT('%', :weeklyHours, '%')
    )
    AND (
        :schedule IS NULL OR :schedule = '' OR 
        CONCAT(jv.startTime, ' - ', jv.endTime) LIKE CONCAT('%', :schedule, '%')
    )
    AND (:minAvailableSlots IS NULL OR jv.availableSlots >= :minAvailableSlots)
    AND (:minSalary IS NULL OR jv.minSalary >= :minSalary)
    AND (:maxSalary IS NULL OR jv.maxSalary <= :maxSalary)
    """)
    Page<JobVacancyEntity> searchVacanciesPage(
            @Param("keyword") String keyword,
            @Param("positionTitles") String positionTitles,
            @Param("companyNames") String companyNames,
            @Param("categories") String categories,
            @Param("contractTypes") String contractTypes,
            @Param("workModalities") String workModalities,
            @Param("weeklyHours") String weeklyHours,
            @Param("schedule") String schedule,
            @Param("minAvailableSlots") Integer minAvailableSlots,
            @Param("minSalary") BigDecimal minSalary,
            @Param("maxSalary") BigDecimal maxSalary,
            Pageable pageable
    );

    // ===== CONTADOR DE POSTULANTES =====
    @Query("SELECT v.id, COUNT(a) FROM JobVacancyEntity v " +
            "LEFT JOIN JobApplicationEntity a ON a.vacancy.id = v.id " +
            "GROUP BY v.id")
    List<Object[]> countApplicantsByVacancy();

    // ===== VACANTES POR FECHA =====
    List<JobVacancyEntity> findByClosingDateBetween(LocalDate startDate, LocalDate endDate);

    // ===== VACANTES PRÓXIMAS A EXPIRAR =====
    @Query(value = "SELECT * FROM job_vacancies v WHERE v.active = true AND v.closing_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)", nativeQuery = true)
    List<JobVacancyEntity> findExpiringSoon();
}