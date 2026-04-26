package com.pet.businessdomain.jobservice.services.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxWithholdingEntity;
import com.pet.businessdomain.jobservice.entities.PayrollEntity;
import com.pet.businessdomain.jobservice.repository.IRPF.TaxWithholdingRepository;
import com.pet.businessdomain.jobservice.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxWithholdingServiceImpl implements TaxWithholdingService {

    private final TaxWithholdingRepository withholdingRepository;
    private final PayrollRepository payrollRepository;

    // Tramos IRPF simplificados para cálculo de retención mensual
    private static final List<TaxBracket> WITHHOLDING_BRACKETS = List.of(
            new TaxBracket(0, 12450, 0.19),
            new TaxBracket(12451, 20200, 0.24),
            new TaxBracket(20201, 35200, 0.30),
            new TaxBracket(35201, 60000, 0.37),
            new TaxBracket(60001, 300000, 0.45),
            new TaxBracket(300001, Double.MAX_VALUE, 0.47)
    );

    @Override
    @Transactional
    public TaxWithholdingEntity createWithholdingFromPayroll(PayrollEntity payroll) {
        log.info("Creating withholding for payrollId: {}", payroll.getId());

        // Verificar si ya existe
        if (withholdingRepository.existsByPayrollId(payroll.getId())) {
            log.warn("Withholding already exists for payrollId: {}", payroll.getId());
            return withholdingRepository.findByPayrollId(payroll.getId()).orElse(null);
        }

        // Calcular retención basada en el salario base
        BigDecimal irpfRate = calculateIRPFRate(payroll.getBaseSalary());
        BigDecimal irpfWithheld = payroll.getBaseSalary()
                .multiply(irpfRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        // Obtener acumulados del año hasta el mes anterior
        BigDecimal accumulatedTaxBase = withholdingRepository
                .sumGrossIncomeByCharacterAndYear(payroll.getCharacterId(), payroll.getYear());
        if (accumulatedTaxBase == null) accumulatedTaxBase = BigDecimal.ZERO;

        BigDecimal accumulatedWithheld = withholdingRepository
                .sumWithheldByCharacterAndYear(payroll.getCharacterId(), payroll.getYear());
        if (accumulatedWithheld == null) accumulatedWithheld = BigDecimal.ZERO;

        // Crear entidad
        TaxWithholdingEntity entity = new TaxWithholdingEntity();
        entity.setPayrollId(payroll.getId());
        entity.setCharacterId(payroll.getCharacterId());
        entity.setAccountId(payroll.getAccountId());
        entity.setYear(payroll.getYear());
        entity.setMonth(payroll.getMonth());
        entity.setGrossIncome(payroll.getBaseSalary());
        entity.setIrpfRate(irpfRate);
        entity.setIrpfWithheld(irpfWithheld);
        entity.setAccumulatedTaxBase(accumulatedTaxBase.add(payroll.getBaseSalary()));
        entity.setAccumulatedWithheld(accumulatedWithheld.add(irpfWithheld));

        return withholdingRepository.save(entity);
    }

    @Override
    public List<TaxWithholdingEntity> getWithholdingsByCharacterAndYear(Long characterId, Integer year) {
        return withholdingRepository.findByCharacterIdAndYearOrderByMonthAsc(characterId, year);
    }

    @Override
    public TaxWithholdingEntity getWithholdingByPayrollId(Long payrollId) {
        return withholdingRepository.findByPayrollId(payrollId).orElse(null);
    }

    @Override
    public BigDecimal getTotalWithheldByCharacterAndYear(Long characterId, Integer year) {
        BigDecimal total = withholdingRepository.sumWithheldByCharacterAndYear(characterId, year);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalGrossIncomeByCharacterAndYear(Long characterId, Integer year) {
        BigDecimal total = withholdingRepository.sumGrossIncomeByCharacterAndYear(characterId, year);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public void processYearlyWithholdings(Integer year) {
        log.info("Processing withholdings for year: {}", year);

        List<PayrollEntity> paidPayrolls = payrollRepository.findByYearAndStatus(year, "PAID");

        for (PayrollEntity payroll : paidPayrolls) {
            if (!withholdingRepository.existsByPayrollId(payroll.getId())) {
                createWithholdingFromPayroll(payroll);
            }
        }

        log.info("Completed processing withholdings for year: {}", year);
    }

    @Override
    @Transactional
    public TaxWithholdingEntity updateWithholding(Long id, BigDecimal newRate, BigDecimal newWithheld) {
        TaxWithholdingEntity entity = withholdingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Withholding not found with id: " + id));

        entity.setIrpfRate(newRate);
        entity.setIrpfWithheld(newWithheld);

        return withholdingRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteWithholdingByPayrollId(Long payrollId) {
        withholdingRepository.deleteByPayrollId(payrollId);
        log.info("Deleted withholding for payrollId: {}", payrollId);
    }

    private BigDecimal calculateIRPFRate(BigDecimal monthlySalary) {
        // Convertir salario mensual a anual (asumiendo 12 pagas)
        BigDecimal annualSalary = monthlySalary.multiply(new BigDecimal("12"));

        for (TaxBracket bracket : WITHHOLDING_BRACKETS) {
            if (annualSalary.compareTo(BigDecimal.valueOf(bracket.maximum)) <= 0) {
                return BigDecimal.valueOf(bracket.rate);
            }
        }
        return BigDecimal.valueOf(WITHHOLDING_BRACKETS.get(WITHHOLDING_BRACKETS.size() - 1).rate);
    }

    // Clase interna para tramos
    private static class TaxBracket {
        final double minimum;
        final double maximum;
        final double rate;

        TaxBracket(double minimum, double maximum, double rate) {
            this.minimum = minimum;
            this.maximum = maximum;
            this.rate = rate;
        }
    }
}
