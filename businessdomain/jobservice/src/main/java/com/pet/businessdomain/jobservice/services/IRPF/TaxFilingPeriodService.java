package com.pet.businessdomain.jobservice.services.IRPF;


import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingPeriodEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TaxFilingPeriodService {

    // Crear nuevo período de declaración
    TaxFilingPeriodEntity createPeriod(Integer taxYear, LocalDateTime startDate, LocalDateTime endDate);

    // Obtener período activo actual
    TaxFilingPeriodEntity getActivePeriod();

    // Obtener período por año
    TaxFilingPeriodEntity getPeriodByYear(Integer taxYear);

    // Obtener todos los períodos
    List<TaxFilingPeriodEntity> getAllPeriods();

    // Actualizar estado de períodos automáticamente
    void updatePeriodsStatus();

    // Verificar si se puede presentar declaración
    boolean canFileTaxes();

    // Cerrar período manualmente
    TaxFilingPeriodEntity closePeriod(Integer taxYear);

    // Abrir período manualmente
    TaxFilingPeriodEntity openPeriod(Integer taxYear);
}
