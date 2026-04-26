package com.pet.businessdomain.jobservice.repository.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxWithholdingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxWithholdingRepository extends JpaRepository<TaxWithholdingEntity, Long> {

    // Búsquedas básicas
    List<TaxWithholdingEntity> findByCharacterIdAndYear(Long characterId, Integer year);

    List<TaxWithholdingEntity> findByCharacterIdAndYearOrderByMonthAsc(Long characterId, Integer year);

    Optional<TaxWithholdingEntity> findByPayrollId(Long payrollId);

    List<TaxWithholdingEntity> findByAccountId(Long accountId);

    // Verificar si existe retención para una nómina
    boolean existsByPayrollId(Long payrollId);

    // Sumatorios
    @Query("SELECT SUM(t.irpfWithheld) FROM TaxWithholdingEntity t WHERE t.characterId = :characterId AND t.year = :year")
    BigDecimal sumWithheldByCharacterAndYear(@Param("characterId") Long characterId, @Param("year") Integer year);

    @Query("SELECT SUM(t.grossIncome) FROM TaxWithholdingEntity t WHERE t.characterId = :characterId AND t.year = :year")
    BigDecimal sumGrossIncomeByCharacterAndYear(@Param("characterId") Long characterId, @Param("year") Integer year);

    // Para múltiples personajes (procesamiento batch)
    @Query("SELECT t.characterId, SUM(t.irpfWithheld) FROM TaxWithholdingEntity t WHERE t.year = :year GROUP BY t.characterId")
    List<Object[]> sumWithheldGroupByCharacter(@Param("year") Integer year);

    // Eliminar por payrollId
    void deleteByPayrollId(Long payrollId);

    // Obtener años con retenciones
    @Query("SELECT DISTINCT t.year FROM TaxWithholdingEntity t WHERE t.characterId = :characterId ORDER BY t.year DESC")
    List<Integer> findYearsByCharacterId(@Param("characterId") Long characterId);
}