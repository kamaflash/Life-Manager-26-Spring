package com.pet.businessdomain.jobservice.services.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingEntity;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingPeriodEntity;
import com.pet.businessdomain.jobservice.entities.PayrollEntity;
import com.pet.businessdomain.jobservice.repository.IRPF.TaxFilingPeriodRepository;
import com.pet.businessdomain.jobservice.repository.IRPF.TaxFilingRepository;
import com.pet.businessdomain.jobservice.repository.IRPF.TaxWithholdingRepository;
import com.pet.businessdomain.jobservice.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxFilingServiceImpl implements TaxFilingService {

    private final TaxFilingRepository filingRepository;
    private final TaxWithholdingRepository withholdingRepository;
    private final PayrollRepository payrollRepository;
    private final TaxFilingPeriodRepository periodRepository;

    // Tramos IRPF anuales
    private static final List<TaxBracket> TAX_BRACKETS = List.of(
            new TaxBracket(0, 12450, 0.19),
            new TaxBracket(12451, 20200, 0.24),
            new TaxBracket(20201, 35200, 0.30),
            new TaxBracket(35201, 60000, 0.37),
            new TaxBracket(60001, 300000, 0.45),
            new TaxBracket(300001, Double.MAX_VALUE, 0.47)
    );

    // Deducciones estándar
    private static final BigDecimal MINIMUM_PERSONAL = new BigDecimal("5500");
    private static final BigDecimal WORK_EXPENSE = new BigDecimal("2000");

    @Override
    public TaxFilingEntity prepareFiling(Long characterId, Integer taxYear) {
        log.info("Preparing filing for characterId: {}, year: {}", characterId, taxYear);

        // 1. Obtener nóminas pagadas del año
        List<PayrollEntity> payrolls = payrollRepository.findPaidByCharacterAndYear(characterId, taxYear);
        BigDecimal totalGrossIncome = payrolls.stream()
                .map(PayrollEntity::getBaseSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Obtener retenciones del año
        BigDecimal totalWithheld = withholdingRepository.sumWithheldByCharacterAndYear(characterId, taxYear);
        if (totalWithheld == null) totalWithheld = BigDecimal.ZERO;

        // 3. Calcular deducciones
        BigDecimal totalDeductions = MINIMUM_PERSONAL.add(WORK_EXPENSE);

        // 4. Calcular base imponible
        BigDecimal taxableBase = totalGrossIncome.subtract(totalDeductions);
        if (taxableBase.compareTo(BigDecimal.ZERO) < 0) taxableBase = BigDecimal.ZERO;

        // 5. Calcular impuesto según tramos
        BigDecimal calculatedTax = calculateProgressiveTax(taxableBase);

        // 6. Calcular resultado
        BigDecimal result = calculatedTax.subtract(totalWithheld);

        // 7. Crear objeto (sin guardar)
        TaxFilingEntity filing = new TaxFilingEntity();
        filing.setCharacterId(characterId);
        filing.setTaxYear(taxYear);
        filing.setTotalGrossIncome(totalGrossIncome);
        filing.setTotalDeductions(totalDeductions);
        filing.setTaxableBase(taxableBase);
        filing.setCalculatedTax(calculatedTax);
        filing.setTotalWithheld(totalWithheld);
        filing.setResult(result);

        if (result.compareTo(BigDecimal.ZERO) > 0) {
            filing.setResultType("TO_PAY");
            filing.setAmountToPay(result);
            filing.setAmountToReceive(BigDecimal.ZERO);
        } else if (result.compareTo(BigDecimal.ZERO) < 0) {
            filing.setResultType("TO_RECEIVE");
            filing.setAmountToReceive(result.abs());
            filing.setAmountToPay(BigDecimal.ZERO);
        } else {
            filing.setResultType("NEUTRAL");
            filing.setAmountToPay(BigDecimal.ZERO);
            filing.setAmountToReceive(BigDecimal.ZERO);
        }

        return filing;
    }

    @Override
    @Transactional
    public TaxFilingEntity createOrUpdateDraft(Long characterId, Integer taxYear) {
        log.info("Creating/updating draft filing for characterId: {}, year: {}", characterId, taxYear);

        TaxFilingEntity existingFiling = filingRepository.findByCharacterIdAndTaxYear(characterId, taxYear).orElse(null);

        if (existingFiling != null && !"DRAFT".equals(existingFiling.getStatus())) {
            throw new RuntimeException("Cannot modify filing that is not in DRAFT status");
        }

        TaxFilingEntity preparedFiling = prepareFiling(characterId, taxYear);

        if (existingFiling != null) {
            // Actualizar existente
            existingFiling.setTotalGrossIncome(preparedFiling.getTotalGrossIncome());
            existingFiling.setTotalDeductions(preparedFiling.getTotalDeductions());
            existingFiling.setTaxableBase(preparedFiling.getTaxableBase());
            existingFiling.setCalculatedTax(preparedFiling.getCalculatedTax());
            existingFiling.setTotalWithheld(preparedFiling.getTotalWithheld());
            existingFiling.setResult(preparedFiling.getResult());
            existingFiling.setResultType(preparedFiling.getResultType());
            existingFiling.setAmountToPay(preparedFiling.getAmountToPay());
            existingFiling.setAmountToReceive(preparedFiling.getAmountToReceive());
            existingFiling.setUpdatedAt(LocalDateTime.now());
            return filingRepository.save(existingFiling);
        } else {
            // Crear nuevo
            preparedFiling.setStatus("DRAFT");
            preparedFiling.setPaymentStatus("PENDING");
            preparedFiling.setCreatedAt(LocalDateTime.now());
            preparedFiling.setUpdatedAt(LocalDateTime.now());
            return filingRepository.save(preparedFiling);
        }
    }

    @Override
    @Transactional
    public TaxFilingEntity submitFiling(Long filingId, Long characterId) {
        log.info("Submitting filing id: {}, characterId: {}", filingId, characterId);

        TaxFilingEntity filing = filingRepository.findById(filingId)
                .orElseThrow(() -> new RuntimeException("Filing not found with id: " + filingId));

        if (!filing.getCharacterId().equals(characterId)) {
            throw new RuntimeException("Filing does not belong to character");
        }

        if (!"DRAFT".equals(filing.getStatus())) {
            throw new RuntimeException("Filing is not in DRAFT status");
        }

        // Verificar período de declaración activo
        TaxFilingPeriodEntity activePeriod = periodRepository.findActivePeriod(LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("No active tax filing period"));

        filing.setStatus("SUBMITTED");
        filing.setFilingDate(LocalDate.now());
        filing.setSubmittedAt(LocalDateTime.now());
        filing.setSubmittedBy(String.valueOf(characterId));

        // Calcular fecha límite de pago (30 días después)
        filing.setPaymentDeadline(LocalDate.now().plusDays(30));

        return filingRepository.save(filing);
    }

    @Override
    public TaxFilingEntity getFilingByCharacterAndYear(Long characterId, Integer taxYear) {
        return filingRepository.findByCharacterIdAndTaxYear(characterId, taxYear).orElse(null);
    }

    @Override
    public List<TaxFilingEntity> getFilingsByCharacter(Long characterId) {
        return filingRepository.findByCharacterIdOrderByTaxYearDesc(characterId);
    }

    @Override
    @Transactional
    public TaxFilingEntity processPayment(Long filingId, Long accountId, boolean fullPayment) {
        log.info("Processing payment for filingId: {}, accountId: {}", filingId, accountId);

        TaxFilingEntity filing = filingRepository.findById(filingId)
                .orElseThrow(() -> new RuntimeException("Filing not found with id: " + filingId));

        if (!"TO_PAY".equals(filing.getResultType())) {
            throw new RuntimeException("Filing does not require payment");
        }

        if (!"SUBMITTED".equals(filing.getStatus())) {
            throw new RuntimeException("Filing is not in SUBMITTED status");
        }

        // Aquí iría la lógica de pago con el sistema de cuentas
        // Por ahora simulamos

        filing.setPaymentStatus("PAID");
        filing.setPaymentProcessedAt(LocalDateTime.now());
        filing.setStatus("COMPLETED");

        return filingRepository.save(filing);
    }

    @Override
    @Transactional
    public TaxFilingEntity processRefund(Long filingId, Long accountId) {
        log.info("Processing refund for filingId: {}, accountId: {}", filingId, accountId);

        TaxFilingEntity filing = filingRepository.findById(filingId)
                .orElseThrow(() -> new RuntimeException("Filing not found with id: " + filingId));

        if (!"TO_RECEIVE".equals(filing.getResultType())) {
            throw new RuntimeException("Filing does not require refund");
        }

        if (!"SUBMITTED".equals(filing.getStatus())) {
            throw new RuntimeException("Filing is not in SUBMITTED status");
        }

        // Aquí iría la lógica de devolución con el sistema de cuentas

        filing.setPaymentStatus("REFUNDED");
        filing.setPaymentProcessedAt(LocalDateTime.now());
        filing.setStatus("COMPLETED");

        return filingRepository.save(filing);
    }

    @Override
    @Transactional
    public void processMassiveFilings(Integer taxYear, boolean simulateOnly) {
        log.info("Processing massive filings for year: {}, simulateOnly: {}", taxYear, simulateOnly);

        List<Long> characterIds = payrollRepository.findDistinctCharacterIdsByYear(taxYear);

        int processed = 0;
        int toPay = 0;
        int toReceive = 0;

        for (Long characterId : characterIds) {
            try {
                if (!simulateOnly) {
                    createOrUpdateDraft(characterId, taxYear);
                }
                processed++;

                TaxFilingEntity filing = prepareFiling(characterId, taxYear);
                if ("TO_PAY".equals(filing.getResultType())) toPay++;
                if ("TO_RECEIVE".equals(filing.getResultType())) toReceive++;

            } catch (Exception e) {
                log.error("Error processing characterId: {}", characterId, e);
            }
        }

        log.info("Massive filing completed. Processed: {}, ToPay: {}, ToReceive: {}", processed, toPay, toReceive);
    }

    @Override
    public Object[] getStatisticsByYear(Integer taxYear) {
        return filingRepository.getStatisticsByYear(taxYear);
    }

    @Override
    @Transactional
    public int markOverduePayments() {
        return filingRepository.markOverduePayments(LocalDate.now(), LocalDateTime.now());
    }

    private BigDecimal calculateProgressiveTax(BigDecimal taxableBase) {
        BigDecimal remaining = taxableBase;
        BigDecimal totalTax = BigDecimal.ZERO;

        for (TaxBracket bracket : TAX_BRACKETS) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal bracketLimit = BigDecimal.valueOf(bracket.maximum);
            BigDecimal bracketMin = BigDecimal.valueOf(bracket.minimum);
            BigDecimal bracketRange = bracketLimit.subtract(bracketMin);

            if (remaining.compareTo(bracketRange) > 0) {
                totalTax = totalTax.add(bracketRange.multiply(BigDecimal.valueOf(bracket.rate)));
                remaining = remaining.subtract(bracketRange);
            } else {
                totalTax = totalTax.add(remaining.multiply(BigDecimal.valueOf(bracket.rate)));
                remaining = BigDecimal.ZERO;
            }
        }

        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

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
