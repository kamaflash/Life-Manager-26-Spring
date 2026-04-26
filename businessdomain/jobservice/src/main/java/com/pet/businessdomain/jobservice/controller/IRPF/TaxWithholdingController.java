package com.pet.businessdomain.jobservice.controller.IRPF;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxWithholdingEntity;
import com.pet.businessdomain.jobservice.mapper.IRPF.TaxWithholdingMapper;
import com.pet.businessdomain.jobservice.services.IRPF.TaxWithholdingService;
import com.pet.businessdomain.shareddto.dto.IRPF.TaxWithholdingHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tax/withholdings")
@RequiredArgsConstructor
public class TaxWithholdingController {

    private final TaxWithholdingService withholdingService;
    private final TaxWithholdingMapper withholdingMapper;

    @GetMapping("/history/character/{characterId}/year/{year}")
    public ResponseEntity<TaxWithholdingHistoryDTO> getWithholdingHistory(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "year") Integer year) {

        List<TaxWithholdingEntity> withholdings = withholdingService.getWithholdingsByCharacterAndYear(characterId, year);
        TaxWithholdingHistoryDTO history = withholdingMapper.toHistoryDto(characterId, year, withholdings);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/character/{characterId}/year/{year}")
    public ResponseEntity<List<TaxWithholdingEntity>> getWithholdingsByCharacterAndYear(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "year") Integer year) {
        List<TaxWithholdingEntity> withholdings = withholdingService.getWithholdingsByCharacterAndYear(characterId, year);
        return ResponseEntity.ok(withholdings);
    }

    @GetMapping("/payroll/{payrollId}")
    public ResponseEntity<TaxWithholdingEntity> getWithholdingByPayrollId(
            @PathVariable(name = "payrollId") Long payrollId) {
        TaxWithholdingEntity withholding = withholdingService.getWithholdingByPayrollId(payrollId);
        if (withholding == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(withholding);
    }

    @GetMapping("/character/{characterId}/year/{year}/total-withheld")
    public ResponseEntity<BigDecimal> getTotalWithheldByCharacterAndYear(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "year") Integer year) {
        BigDecimal total = withholdingService.getTotalWithheldByCharacterAndYear(characterId, year);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/character/{characterId}/year/{year}/total-gross")
    public ResponseEntity<BigDecimal> getTotalGrossIncomeByCharacterAndYear(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "year") Integer year) {
        BigDecimal total = withholdingService.getTotalGrossIncomeByCharacterAndYear(characterId, year);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/process-year/{year}")
    public ResponseEntity<Void> processYearlyWithholdings(
            @PathVariable(name = "year") Integer year) {
        withholdingService.processYearlyWithholdings(year);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxWithholdingEntity> updateWithholding(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "rate") BigDecimal rate,
            @RequestParam(name = "withheld") BigDecimal withheld) {
        TaxWithholdingEntity updated = withholdingService.updateWithholding(id, rate, withheld);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/payroll/{payrollId}")
    public ResponseEntity<Void> deleteWithholdingByPayrollId(
            @PathVariable(name = "payrollId") Long payrollId) {
        withholdingService.deleteWithholdingByPayrollId(payrollId);
        return ResponseEntity.noContent().build();
    }
}