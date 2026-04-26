package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollEntity, Long>,
        JpaSpecificationExecutor<PayrollEntity> {

    // Búsquedas básicas
    List<PayrollEntity> findByCharacterId(Long characterId);
    List<PayrollEntity> findByJobId(Long jobId);
    List<PayrollEntity> findByCharacterIdAndJobId(Long characterId, Long jobId);

    // Búsquedas por período
    List<PayrollEntity> findByCharacterIdAndYearAndMonth(Long characterId, Integer year, Integer month);
    List<PayrollEntity> findByYearAndMonth(Integer year, Integer month);

    // Búsquedas por estado
    List<PayrollEntity> findByCharacterIdAndStatus(Long characterId, String status);
    List<PayrollEntity> findByStatus(String status);

    // Verificar si ya existe nómina en el período
    boolean existsByCharacterIdAndJobIdAndYearAndMonth(Long characterId, Long jobId, Integer year, Integer month);

    // Búsqueda por rango de fechas
    List<PayrollEntity> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);
    List<PayrollEntity> findByCharacterIdAndPaymentDateBetween(Long characterId, LocalDateTime start, LocalDateTime end);

    // Obtener la última nómina de un trabajo
    Optional<PayrollEntity> findFirstByJobIdOrderByYearDescMonthDesc(Long jobId);

    // Obtener la última nómina de un personaje
    Optional<PayrollEntity> findFirstByCharacterIdOrderByYearDescMonthDesc(Long characterId);

    // JPQL: Suma de salarios netos por período
    @Query("SELECT SUM(p.netSalary) FROM PayrollEntity p WHERE p.characterId = :characterId AND p.year = :year AND p.month = :month AND p.status = 'PAID'")
    BigDecimal sumNetSalaryByCharacterAndPeriod(@Param("characterId") Long characterId, @Param("year") Integer year, @Param("month") Integer month);

    // JPQL: Total pagado a un personaje en un año
    @Query("SELECT SUM(p.netSalary) FROM PayrollEntity p WHERE p.characterId = :characterId AND p.year = :year AND p.status = 'PAID'")
    BigDecimal sumNetSalaryByCharacterAndYear(@Param("characterId") Long characterId, @Param("year") Integer year);

    // JPQL: Nóminas pendientes de pago
    @Query("SELECT p FROM PayrollEntity p WHERE p.status = 'PENDING' AND p.paymentDate IS NULL ORDER BY p.year DESC, p.month DESC")
    List<PayrollEntity> findPendingPayrolls();
}

