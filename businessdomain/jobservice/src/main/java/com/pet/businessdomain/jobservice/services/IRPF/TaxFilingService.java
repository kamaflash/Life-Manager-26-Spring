package com.pet.businessdomain.jobservice.services.IRPF;


import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TaxFilingService {

    // Preparar declaración (calcular sin guardar)
    TaxFilingEntity prepareFiling(Long characterId, Integer taxYear);

    // Crear o actualizar borrador de declaración
    TaxFilingEntity createOrUpdateDraft(Long characterId, Integer taxYear);

    // Presentar declaración
    TaxFilingEntity submitFiling(Long filingId, Long characterId);

    // Obtener declaración por personaje y año
    TaxFilingEntity getFilingByCharacterAndYear(Long characterId, Integer taxYear);

    // Obtener todas las declaraciones de un personaje
    List<TaxFilingEntity> getFilingsByCharacter(Long characterId);

    // Procesar pago de declaración
    TaxFilingEntity processPayment(Long filingId, Long accountId, boolean fullPayment);

    // Procesar devolución (cuando el resultado es a favor del personaje)
    TaxFilingEntity processRefund(Long filingId, Long accountId);

    // Procesar declaraciones masivas (batch para todos los personajes)
    void processMassiveFilings(Integer taxYear, boolean simulateOnly);

    // Obtener estadísticas por año
    Object[] getStatisticsByYear(Integer taxYear);

    // Marcar pagos atrasados
    int markOverduePayments();
}
