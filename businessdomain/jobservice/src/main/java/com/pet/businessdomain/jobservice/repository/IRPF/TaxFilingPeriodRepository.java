package com.pet.businessdomain.jobservice.repository.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingPeriodEntity;
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
public interface TaxFilingPeriodRepository extends JpaRepository<TaxFilingPeriodEntity, Long> {

    // Búsquedas por año
    Optional<TaxFilingPeriodEntity> findByTaxYear(Integer taxYear);

    // Período activo actual
    @Query("SELECT p FROM TaxFilingPeriodEntity p WHERE p.status = 'ACTIVE' AND p.filingStartDate <= :now AND p.filingEndDate >= :now")
    Optional<TaxFilingPeriodEntity> findActivePeriod(@Param("now") LocalDateTime now);

    // Próximo período
    @Query("SELECT p FROM TaxFilingPeriodEntity p WHERE p.status = 'UPCOMING' AND p.filingStartDate > :now ORDER BY p.filingStartDate ASC")
    List<TaxFilingPeriodEntity> findUpcomingPeriods(@Param("now") LocalDateTime now);

    // Períodos por estado
    List<TaxFilingPeriodEntity> findByStatusOrderByTaxYearDesc(String status);

    // Verificar si hay período activo
    @Query("SELECT COUNT(p) > 0 FROM TaxFilingPeriodEntity p WHERE p.status = 'ACTIVE' AND p.filingStartDate <= :now AND p.filingEndDate >= :now")
    boolean hasActivePeriod(@Param("now") LocalDateTime now);

    // Actualización automática de estados
    @Modifying
    @Transactional
    @Query("UPDATE TaxFilingPeriodEntity p SET p.status = 'ACTIVE' WHERE p.status = 'UPCOMING' AND p.filingStartDate <= :now")
    int activateUpcomingPeriods(@Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE TaxFilingPeriodEntity p SET p.status = 'CLOSED' WHERE p.status = 'ACTIVE' AND p.filingEndDate <= :now")
    int closeExpiredPeriods(@Param("now") LocalDateTime now);

    // Histórico
    List<TaxFilingPeriodEntity> findByTaxYearGreaterThanEqualOrderByTaxYearDesc(Integer year);
}
