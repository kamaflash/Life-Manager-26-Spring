package com.pet.businessdomain.jobservice.controller.IRPF;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingEntity;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingPeriodEntity;
import com.pet.businessdomain.jobservice.services.IRPF.TaxFilingPeriodService;
import com.pet.businessdomain.jobservice.services.IRPF.TaxFilingService;
import com.pet.businessdomain.jobservice.services.IRPF.TaxNotificationService;
import com.pet.businessdomain.jobservice.services.IRPF.TaxWithholdingService;
import com.pet.businessdomain.shareddto.dto.IRPF.TaxDashboardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tax/dashboard")
@RequiredArgsConstructor
public class TaxDashboardController {

    private final TaxWithholdingService withholdingService;
    private final TaxFilingService filingService;
    private final TaxFilingPeriodService periodService;
    private final TaxNotificationService notificationService;

    @GetMapping("/character/{characterId}/year/{year}")
    public ResponseEntity<TaxDashboardDTO> getTaxDashboard(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "year") Integer year) {

        TaxDashboardDTO dashboard = new TaxDashboardDTO();

        dashboard.setCharacterId(characterId);

        BigDecimal totalWithheld = withholdingService.getTotalWithheldByCharacterAndYear(characterId, year);
        if (totalWithheld == null) totalWithheld = BigDecimal.ZERO;
        dashboard.setTotalWithheldCurrentYear(totalWithheld);

        BigDecimal totalGross = withholdingService.getTotalGrossIncomeByCharacterAndYear(characterId, year);
        if (totalGross == null) totalGross = BigDecimal.ZERO;
        BigDecimal estimatedTax = totalGross.multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal estimatedResult = estimatedTax.subtract(totalWithheld).setScale(2, RoundingMode.HALF_UP);

        dashboard.setEstimatedTax(estimatedTax);
        dashboard.setEstimatedResult(estimatedResult);

        TaxFilingPeriodEntity currentPeriod = periodService.getActivePeriod();
        if (currentPeriod != null && currentPeriod.getFilingEndDate() != null) {
            long daysUntilDeadline = ChronoUnit.DAYS.between(LocalDateTime.now(), currentPeriod.getFilingEndDate());
            dashboard.setDaysUntilDeadline((int) Math.max(0, daysUntilDeadline));
        } else {
            dashboard.setDaysUntilDeadline(0);
        }

        return ResponseEntity.ok(dashboard);
    }
}