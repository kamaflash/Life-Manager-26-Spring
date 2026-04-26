package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.PayrollDTO;
import com.pet.businessdomain.shareddto.dto.PayrollSearchFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PayrollService {

    // CRUD básico
    PayrollDTO createPayroll(PayrollDTO payrollDTO);
    PayrollDTO getPayrollById(Long id);
    List<PayrollDTO> getAllPayrolls();
    PayrollDTO updatePayroll(Long id, PayrollDTO payrollDTO);
    void deletePayroll(Long id);

    // Búsquedas por personaje
    List<PayrollDTO> getPayrollsByCharacterId(Long characterId);

    // Búsquedas por trabajo
    List<PayrollDTO> getPayrollsByJobId(Long jobId);

    // Búsquedas por período
    List<PayrollDTO> getPayrollsByCharacterAndPeriod(Long characterId, Integer year, Integer month);
    List<PayrollDTO> getPayrollsByPeriod(Integer year, Integer month);

    // Búsquedas por estado
    List<PayrollDTO> getPayrollsByStatus(String status);

    // Acciones de negocio
    PayrollDTO processPayroll(Long id);
    PayrollDTO markAsPaid(Long id, Long accountId);
    PayrollDTO cancelPayroll(Long id);

    // Verificaciones
    boolean existsPayrollForPeriod(Long characterId, Long jobId, Integer year, Integer month);

    // Estadísticas
    BigDecimal getTotalPaidByCharacterAndPeriod(Long characterId, Integer year, Integer month);
    BigDecimal getTotalPaidByCharacterAndYear(Long characterId, Integer year);
    Page<PayrollDTO> search(PayrollSearchFiltersDTO filters, Pageable pageable);
}
