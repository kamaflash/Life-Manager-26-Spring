package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    List<JobVacancyEntity> findByLocationContainingIgnoreCase(String location);
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
    @Query("SELECT v FROM JobVacancyEntity v " +
            "WHERE v.active = true AND v.availableSlots > 0 " +
            "AND (:keyword IS NULL OR LOWER(v.position.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.position.company.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:location IS NULL OR LOWER(v.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:contractType IS NULL OR v.contractType = :contractType) " +
            "AND (:workModality IS NULL OR v.workModality = :workModality) " +
            "AND (:minSalary IS NULL OR v.maxSalary >= :minSalary) " +
            "AND (:maxSalary IS NULL OR v.minSalary <= :maxSalary)")
    List<JobVacancyEntity> searchVacancies(@Param("keyword") String keyword,
                                           @Param("location") String location,
                                           @Param("contractType") String contractType,
                                           @Param("workModality") String workModality,
                                           @Param("minSalary") BigDecimal minSalary,
                                           @Param("maxSalary") BigDecimal maxSalary);

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