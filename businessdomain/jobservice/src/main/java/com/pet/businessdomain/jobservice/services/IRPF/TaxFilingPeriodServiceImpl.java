package com.pet.businessdomain.jobservice.services.IRPF;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingPeriodEntity;
import com.pet.businessdomain.jobservice.repository.IRPF.TaxFilingPeriodRepository;
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
public class TaxFilingPeriodServiceImpl implements TaxFilingPeriodService {

    private final TaxFilingPeriodRepository periodRepository;

    @Override
    @Transactional
    public TaxFilingPeriodEntity createPeriod(Integer taxYear, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Creating tax filing period for year: {}", taxYear);

        TaxFilingPeriodEntity entity = new TaxFilingPeriodEntity();
        entity.setTaxYear(taxYear);
        entity.setName("Campaña Renta " + taxYear);
        entity.setFilingStartDate(startDate);
        entity.setFilingEndDate(endDate);
        entity.setMinimumTaxable(new BigDecimal("22000"));
        entity.setLateFilingAllowed(true);
        entity.setLateFilingPenalty(new BigDecimal("5"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate)) {
            entity.setStatus("UPCOMING");
        } else if (now.isAfter(endDate)) {
            entity.setStatus("CLOSED");
        } else {
            entity.setStatus("ACTIVE");
        }

        entity.setApplicableRules("{}");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return periodRepository.save(entity);
    }

    @Override
    public TaxFilingPeriodEntity getActivePeriod() {
        return periodRepository.findActivePeriod(LocalDateTime.now()).orElse(null);
    }

    @Override
    public TaxFilingPeriodEntity getPeriodByYear(Integer taxYear) {
        return periodRepository.findByTaxYear(taxYear).orElse(null);
    }

    @Override
    public List<TaxFilingPeriodEntity> getAllPeriods() {
        // Usar findByStatusOrderByTaxYearDesc o findAll normal
        return periodRepository.findByStatusOrderByTaxYearDesc(null);
    }

    @Override
    @Transactional
    public void updatePeriodsStatus() {
        log.info("Updating periods status");

        int activated = periodRepository.activateUpcomingPeriods(LocalDateTime.now());
        int closed = periodRepository.closeExpiredPeriods(LocalDateTime.now());

        log.info("Periods updated - Activated: {}, Closed: {}", activated, closed);
    }

    @Override
    public boolean canFileTaxes() {
        return periodRepository.hasActivePeriod(LocalDateTime.now());
    }

    @Override
    @Transactional
    public TaxFilingPeriodEntity closePeriod(Integer taxYear) {
        TaxFilingPeriodEntity period = periodRepository.findByTaxYear(taxYear)
                .orElseThrow(() -> new RuntimeException("Period not found for year: " + taxYear));

        period.setStatus("CLOSED");
        period.setUpdatedAt(LocalDateTime.now());

        return periodRepository.save(period);
    }

    @Override
    @Transactional
    public TaxFilingPeriodEntity openPeriod(Integer taxYear) {
        TaxFilingPeriodEntity period = periodRepository.findByTaxYear(taxYear)
                .orElseThrow(() -> new RuntimeException("Period not found for year: " + taxYear));

        LocalDateTime now = LocalDateTime.now();
        period.setStatus("ACTIVE");
        period.setFilingStartDate(now);
        period.setFilingEndDate(now.plusDays(60));
        period.setUpdatedAt(now);

        return periodRepository.save(period);
    }
}