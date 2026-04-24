package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.PayrollEntity;
import com.pet.businessdomain.jobservice.mapper.PayrollMapper;
import com.pet.businessdomain.jobservice.repository.PayrollRepository;
import com.pet.businessdomain.shareddto.dto.PayrollDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final PayrollMapper payrollMapper;

    @Override
    public PayrollDTO createPayroll(PayrollDTO payrollDTO) {
        log.info("Creando nueva nómina para personaje: {}, trabajo: {}",
                payrollDTO.getCharacterId(), payrollDTO.getJobId());

        PayrollEntity entity = payrollMapper.toEntity(payrollDTO);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setStatus("PENDING");

        PayrollEntity saved = payrollRepository.save(entity);
        log.info("Nómina creada con ID: {}", saved.getId());

        return payrollMapper.toDto(saved);
    }

    @Override
    public PayrollDTO getPayrollById(Long id) {
        log.info("Buscando nómina por ID: {}", id);
        PayrollEntity entity = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));
        return payrollMapper.toDto(entity);
    }

    @Override
    public List<PayrollDTO> getAllPayrolls() {
        log.info("Obteniendo todas las nóminas");
        return payrollMapper.toDtoList(payrollRepository.findAll());
    }

    @Override
    public PayrollDTO updatePayroll(Long id, PayrollDTO payrollDTO) {
        log.info("Actualizando nómina con ID: {}", id);

        PayrollEntity existing = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));

        payrollMapper.updateEntity(payrollDTO, existing);
        existing.setUpdatedAt(LocalDateTime.now());

        PayrollEntity updated = payrollRepository.save(existing);
        return payrollMapper.toDto(updated);
    }

    @Override
    public void deletePayroll(Long id) {
        log.info("Eliminando nómina con ID: {}", id);
        payrollRepository.deleteById(id);
    }

    @Override
    public List<PayrollDTO> getPayrollsByCharacterId(Long characterId) {
        log.info("Obteniendo nóminas del personaje: {}", characterId);
        return payrollMapper.toDtoList(payrollRepository.findByCharacterId(characterId));
    }

    @Override
    public List<PayrollDTO> getPayrollsByJobId(Long jobId) {
        log.info("Obteniendo nóminas del trabajo: {}", jobId);
        return payrollMapper.toDtoList(payrollRepository.findByJobId(jobId));
    }

    @Override
    public List<PayrollDTO> getPayrollsByCharacterAndPeriod(Long characterId, Integer year, Integer month) {
        log.info("Obteniendo nóminas del personaje {} para {}/{}", characterId, month, year);
        return payrollMapper.toDtoList(payrollRepository.findByCharacterIdAndYearAndMonth(characterId, year, month));
    }

    @Override
    public List<PayrollDTO> getPayrollsByPeriod(Integer year, Integer month) {
        log.info("Obteniendo nóminas para {}/{}", month, year);
        return payrollMapper.toDtoList(payrollRepository.findByYearAndMonth(year, month));
    }

    @Override
    public List<PayrollDTO> getPayrollsByStatus(String status) {
        log.info("Obteniendo nóminas con estado: {}", status);
        return payrollMapper.toDtoList(payrollRepository.findByStatus(status));
    }

    @Override
    public PayrollDTO processPayroll(Long id) {
        log.info("Procesando nómina ID: {}", id);

        PayrollEntity payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));

        // Calcular neto si no está calculado
        if (payroll.getNetSalary() == null) {
            BigDecimal gross = payroll.getBaseSalary();
            if (payroll.getBonus() != null) gross = gross.add(payroll.getBonus());
            BigDecimal totalDeductions = payroll.getDeductions() != null ? payroll.getDeductions() : BigDecimal.ZERO;
            payroll.setNetSalary(gross.subtract(totalDeductions));
        }

        payroll.setStatus("PROCESSED");
        payroll.setUpdatedAt(LocalDateTime.now());

        PayrollEntity processed = payrollRepository.save(payroll);
        log.info("Nómina procesada: {}, neto: {}", processed.getId(), processed.getNetSalary());

        return payrollMapper.toDto(processed);
    }

    @Override
    public PayrollDTO markAsPaid(Long id, Long accountId) {
        log.info("Marcando nómina {} como pagada a cuenta {}", id, accountId);

        PayrollEntity payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));

        payroll.setStatus("PAID");
        payroll.setPaymentDate(LocalDateTime.now());
        payroll.setAccountId(accountId);
        payroll.setUpdatedAt(LocalDateTime.now());

        PayrollEntity paid = payrollRepository.save(payroll);
        log.info("Nómina pagada: {}", paid.getId());

        return payrollMapper.toDto(paid);
    }

    @Override
    public PayrollDTO cancelPayroll(Long id) {
        log.info("Cancelando nómina ID: {}", id);

        PayrollEntity payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));

        payroll.setStatus("CANCELLED");
        payroll.setUpdatedAt(LocalDateTime.now());

        PayrollEntity cancelled = payrollRepository.save(payroll);
        log.info("Nómina cancelada: {}", cancelled.getId());

        return payrollMapper.toDto(cancelled);
    }

    @Override
    public boolean existsPayrollForPeriod(Long characterId, Long jobId, Integer year, Integer month) {
        log.info("Verificando si existe nómina para personaje {} trabajo {} en {}/{}",
                characterId, jobId, month, year);
        return payrollRepository.existsByCharacterIdAndJobIdAndYearAndMonth(characterId, jobId, year, month);
    }

    @Override
    public BigDecimal getTotalPaidByCharacterAndPeriod(Long characterId, Integer year, Integer month) {
        log.info("Calculando total pagado a personaje {} en {}/{}", characterId, month, year);
        BigDecimal total = payrollRepository.sumNetSalaryByCharacterAndPeriod(characterId, year, month);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalPaidByCharacterAndYear(Long characterId, Integer year) {
        log.info("Calculando total pagado a personaje {} en año {}", characterId, year);
        BigDecimal total = payrollRepository.sumNetSalaryByCharacterAndYear(characterId, year);
        return total != null ? total : BigDecimal.ZERO;
    }
}
