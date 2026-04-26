package com.pet.businessdomain.jobservice.controller.IRPF;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingEntity;
import com.pet.businessdomain.jobservice.services.IRPF.TaxFilingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tax/filings")
@RequiredArgsConstructor
public class TaxFilingController {

    private final TaxFilingService filingService;

    // Preparar declaración (calcular sin guardar) - retorna entity
    @GetMapping("/prepare/character/{characterId}/year/{taxYear}")
    public ResponseEntity<TaxFilingEntity> prepareFiling(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "taxYear") Integer taxYear) {
        TaxFilingEntity filing = filingService.prepareFiling(characterId, taxYear);
        return ResponseEntity.ok(filing);
    }

    // Crear o actualizar borrador - retorna entity
    @PostMapping("/draft/character/{characterId}/year/{taxYear}")
    public ResponseEntity<TaxFilingEntity> createOrUpdateDraft(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "taxYear") Integer taxYear) {
        TaxFilingEntity filing = filingService.createOrUpdateDraft(characterId, taxYear);
        return ResponseEntity.status(HttpStatus.CREATED).body(filing);
    }

    // Presentar declaración - retorna entity
    @PostMapping("/{filingId}/submit")
    public ResponseEntity<TaxFilingEntity> submitFiling(
            @PathVariable(name = "filingId") Long filingId,
            @RequestParam(name = "characterId") Long characterId) {
        TaxFilingEntity filing = filingService.submitFiling(filingId, characterId);
        return ResponseEntity.ok(filing);
    }

    // Obtener declaración por personaje y año - retorna entity
    @GetMapping("/character/{characterId}/year/{taxYear}")
    public ResponseEntity<TaxFilingEntity> getFilingByCharacterAndYear(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "taxYear") Integer taxYear) {
        TaxFilingEntity filing = filingService.getFilingByCharacterAndYear(characterId, taxYear);
        if (filing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(filing);
    }

    // Obtener todas las declaraciones de un personaje - retorna lista de entities
    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<TaxFilingEntity>> getFilingsByCharacter(
            @PathVariable(name = "characterId") Long characterId) {
        List<TaxFilingEntity> filings = filingService.getFilingsByCharacter(characterId);
        return ResponseEntity.ok(filings);
    }

    // Procesar pago de declaración - retorna entity
    @PostMapping("/{filingId}/payment")
    public ResponseEntity<TaxFilingEntity> processPayment(
            @PathVariable(name = "filingId") Long filingId,
            @RequestParam(name = "accountId") Long accountId,
            @RequestParam(name = "fullPayment", defaultValue = "true") boolean fullPayment) {
        TaxFilingEntity filing = filingService.processPayment(filingId, accountId, fullPayment);
        return ResponseEntity.ok(filing);
    }

    // Procesar devolución - retorna entity
    @PostMapping("/{filingId}/refund")
    public ResponseEntity<TaxFilingEntity> processRefund(
            @PathVariable(name = "filingId") Long filingId,
            @RequestParam(name = "accountId") Long accountId) {
        TaxFilingEntity filing = filingService.processRefund(filingId, accountId);
        return ResponseEntity.ok(filing);
    }

    // Procesar declaraciones masivas
    @PostMapping("/massive/year/{taxYear}")
    public ResponseEntity<Void> processMassiveFilings(
            @PathVariable(name = "taxYear") Integer taxYear,
            @RequestParam(name = "simulateOnly", defaultValue = "false") boolean simulateOnly) {
        filingService.processMassiveFilings(taxYear, simulateOnly);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // Obtener estadísticas por año
    @GetMapping("/statistics/year/{taxYear}")
    public ResponseEntity<Object[]> getStatisticsByYear(
            @PathVariable(name = "taxYear") Integer taxYear) {
        Object[] statistics = filingService.getStatisticsByYear(taxYear);
        return ResponseEntity.ok(statistics);
    }

    // Marcar pagos atrasados
    @PostMapping("/mark-overdue")
    public ResponseEntity<Integer> markOverduePayments() {
        int count = filingService.markOverduePayments();
        return ResponseEntity.ok(count);
    }
}