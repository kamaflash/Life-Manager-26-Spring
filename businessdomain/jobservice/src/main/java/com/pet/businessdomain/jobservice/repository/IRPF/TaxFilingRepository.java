package com.pet.businessdomain.jobservice.repository.IRPF;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxFilingRepository extends JpaRepository<TaxFilingEntity, Long> {

    // Búsquedas por personaje
    List<TaxFilingEntity> findByCharacterIdOrderByTaxYearDesc(Long characterId);

    Optional<TaxFilingEntity> findByCharacterIdAndTaxYear(Long characterId, Integer taxYear);

    boolean existsByCharacterIdAndTaxYear(Long characterId, Integer taxYear);

    // Búsquedas por estado
    List<TaxFilingEntity> findByStatus(String status);

    List<TaxFilingEntity> findByStatusAndPaymentStatus(String status, String paymentStatus);

    @Query("SELECT f FROM TaxFilingEntity f WHERE f.status = 'SUBMITTED' AND f.paymentStatus = 'PENDING' AND f.paymentDeadline < :date")
    List<TaxFilingEntity> findOverduePayments(@Param("date") LocalDate date);

    // Búsquedas por resultado
    List<TaxFilingEntity> findByResultTypeAndStatus(String resultType, String status);

    @Query("SELECT f FROM TaxFilingEntity f WHERE f.resultType = 'TO_PAY' AND f.amountToPay > :threshold AND f.status = 'SUBMITTED'")
    List<TaxFilingEntity> findHighPayments(@Param("threshold") java.math.BigDecimal threshold);

    // Estadísticas por año
    @Query("SELECT COUNT(f), SUM(f.amountToPay), SUM(f.amountToReceive) FROM TaxFilingEntity f WHERE f.taxYear = :year AND f.status = 'COMPLETED'")
    Object[] getStatisticsByYear(@Param("year") Integer year);

    @Query("SELECT f.resultType, COUNT(f), SUM(f.result) FROM TaxFilingEntity f WHERE f.taxYear = :year AND f.status = 'COMPLETED' GROUP BY f.resultType")
    List<Object[]> getResultDistributionByYear(@Param("year") Integer year);

    // Actualizaciones masivas
    @Modifying
    @Transactional
    @Query("UPDATE TaxFilingEntity f SET f.status = 'PROCESSING', f.updatedAt = :now WHERE f.status = 'SUBMITTED' AND f.taxYear = :year")
    int markSubmittedAsProcessing(@Param("year") Integer year, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE TaxFilingEntity f SET f.paymentStatus = 'OVERDUE', f.updatedAt = :now WHERE f.paymentStatus = 'PENDING' AND f.paymentDeadline < :date")
    int markOverduePayments(@Param("date") LocalDate date, @Param("now") LocalDateTime now);

    // Totales por personaje
    @Query("SELECT COALESCE(SUM(f.amountToReceive), 0) FROM TaxFilingEntity f WHERE f.characterId = :characterId AND f.status = 'COMPLETED' AND f.resultType = 'TO_RECEIVE'")
    java.math.BigDecimal sumReceivedByCharacter(@Param("characterId") Long characterId);

    @Query("SELECT COALESCE(SUM(f.amountToPay), 0) FROM TaxFilingEntity f WHERE f.characterId = :characterId AND f.status = 'COMPLETED' AND f.resultType = 'TO_PAY'")
    java.math.BigDecimal sumPaidByCharacter(@Param("characterId") Long characterId);

    // Obtener años con declaraciones
    @Query("SELECT DISTINCT f.taxYear FROM TaxFilingEntity f WHERE f.characterId = :characterId ORDER BY f.taxYear DESC")
    List<Integer> findYearsByCharacterId(@Param("characterId") Long characterId);
}