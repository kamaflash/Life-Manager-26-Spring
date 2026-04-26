package com.pet.businessdomain.jobservice.controller.IRPF;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingPeriodEntity;
import com.pet.businessdomain.jobservice.services.IRPF.TaxFilingPeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tax/periods")
@RequiredArgsConstructor
public class TaxFilingPeriodController {

    private final TaxFilingPeriodService periodService;

    // Crear nuevo período
    @PostMapping
    public ResponseEntity<TaxFilingPeriodEntity> createPeriod(
            @RequestParam(name = "taxYear") Integer taxYear,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        TaxFilingPeriodEntity period = periodService.createPeriod(taxYear, startDate, endDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(period);
    }

    // Obtener período activo actual
    @GetMapping("/active")
    public ResponseEntity<TaxFilingPeriodEntity> getActivePeriod() {
        TaxFilingPeriodEntity period = periodService.getActivePeriod();
        if (period == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(period);
    }

    // Obtener período por año
    @GetMapping("/year/{taxYear}")
    public ResponseEntity<TaxFilingPeriodEntity> getPeriodByYear(
            @PathVariable(name = "taxYear") Integer taxYear) {
        TaxFilingPeriodEntity period = periodService.getPeriodByYear(taxYear);
        if (period == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(period);
    }

    // Obtener todos los períodos
    @GetMapping
    public ResponseEntity<List<TaxFilingPeriodEntity>> getAllPeriods() {
        List<TaxFilingPeriodEntity> periods = periodService.getAllPeriods();
        return ResponseEntity.ok(periods);
    }

    // Actualizar estados de períodos automáticamente
    @PostMapping("/update-status")
    public ResponseEntity<Void> updatePeriodsStatus() {
        periodService.updatePeriodsStatus();
        return ResponseEntity.accepted().build();
    }

    // Verificar si se puede presentar declaración
    @GetMapping("/can-file")
    public ResponseEntity<Boolean> canFileTaxes() {
        boolean canFile = periodService.canFileTaxes();
        return ResponseEntity.ok(canFile);
    }

    // Cerrar período manualmente
    @PostMapping("/{taxYear}/close")
    public ResponseEntity<TaxFilingPeriodEntity> closePeriod(
            @PathVariable(name = "taxYear") Integer taxYear) {
        TaxFilingPeriodEntity period = periodService.closePeriod(taxYear);
        return ResponseEntity.ok(period);
    }

    // Abrir período manualmente
    @PostMapping("/{taxYear}/open")
    public ResponseEntity<TaxFilingPeriodEntity> openPeriod(
            @PathVariable(name = "taxYear") Integer taxYear) {
        TaxFilingPeriodEntity period = periodService.openPeriod(taxYear);
        return ResponseEntity.ok(period);
    }
}