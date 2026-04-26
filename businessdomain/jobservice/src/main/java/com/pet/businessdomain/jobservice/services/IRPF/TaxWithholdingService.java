package com.pet.businessdomain.jobservice.services.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxWithholdingEntity;
import com.pet.businessdomain.jobservice.entities.PayrollEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TaxWithholdingService {

    // Crear retención a partir de una nómina
    TaxWithholdingEntity createWithholdingFromPayroll(PayrollEntity payroll);

    // Obtener retenciones de un personaje por año
    List<TaxWithholdingEntity> getWithholdingsByCharacterAndYear(Long characterId, Integer year);

    // Obtener retención por payrollId
    TaxWithholdingEntity getWithholdingByPayrollId(Long payrollId);

    // Calcular total retenido en el año
    BigDecimal getTotalWithheldByCharacterAndYear(Long characterId, Integer year);

    // Calcular ingreso bruto anual
    BigDecimal getTotalGrossIncomeByCharacterAndYear(Long characterId, Integer year);

    // Procesar retenciones para todos los personajes en un año (batch)
    void processYearlyWithholdings(Integer year);

    // Actualizar retención manualmente
    TaxWithholdingEntity updateWithholding(Long id, BigDecimal newRate, BigDecimal newWithheld);

    // Eliminar retención (por si se anula una nómina)
    void deleteWithholdingByPayrollId(Long payrollId);
}
