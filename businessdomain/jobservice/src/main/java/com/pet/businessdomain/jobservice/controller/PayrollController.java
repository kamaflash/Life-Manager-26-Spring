package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.PayrollService;
import com.pet.businessdomain.shareddto.dto.PayrollDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    // ========== CRUD BÁSICO ==========

    @PostMapping("/create")
    public PayrollDTO createPayroll(@RequestBody PayrollDTO payrollDTO) {
        log.info("POST /api/payrolls - Creando nómina");
        return payrollService.createPayroll(payrollDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollDTO> getPayrollById(@PathVariable Long id) {
        log.info("GET /api/payrolls/{} - Obteniendo nómina", id);
        PayrollDTO payroll = payrollService.getPayrollById(id);
        return ResponseEntity.ok(payroll);
    }

    @GetMapping
    public ResponseEntity<List<PayrollDTO>> getAllPayrolls() {
        log.info("GET /api/payrolls - Obteniendo todas las nóminas");
        List<PayrollDTO> payrolls = payrollService.getAllPayrolls();
        return ResponseEntity.ok(payrolls);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollDTO> updatePayroll(@PathVariable Long id, @RequestBody PayrollDTO payrollDTO) {
        log.info("PUT /api/payrolls/{} - Actualizando nómina", id);
        PayrollDTO updated = payrollService.updatePayroll(id, payrollDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        log.info("DELETE /api/payrolls/{} - Eliminando nómina", id);
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }

    // ========== BÚSQUEDAS POR PERSONAJE ==========

    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByCharacterId(@PathVariable(name = "characterId")  Long characterId) {
        log.info("GET /api/payrolls/character/{} - Obteniendo nóminas del personaje", characterId);
        List<PayrollDTO> payrolls = payrollService.getPayrollsByCharacterId(characterId);
        return ResponseEntity.ok(payrolls);
    }

    // ========== BÚSQUEDAS POR TRABAJO ==========

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByJobId(@PathVariable(name = "jobId") Long jobId) {
        log.info("GET /api/payrolls/job/{} - Obteniendo nóminas del trabajo", jobId);
        List<PayrollDTO> payrolls = payrollService.getPayrollsByJobId(jobId);
        return ResponseEntity.ok(payrolls);
    }

    // ========== BÚSQUEDAS POR PERÍODO ==========

    @GetMapping("/period")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByPeriod(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        log.info("GET /api/payrolls/period - Obteniendo nóminas para {}/{}", month, year);
        List<PayrollDTO> payrolls = payrollService.getPayrollsByPeriod(year, month);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/character/{characterId}/period")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByCharacterAndPeriod(
            @PathVariable Long characterId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        log.info("GET /api/payrolls/character/{}/period - Obteniendo nóminas para {}/{}", characterId, month, year);
        List<PayrollDTO> payrolls = payrollService.getPayrollsByCharacterAndPeriod(characterId, year, month);
        return ResponseEntity.ok(payrolls);
    }

    // ========== BÚSQUEDAS POR ESTADO ==========

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByStatus(@PathVariable String status) {
        log.info("GET /api/payrolls/status/{} - Obteniendo nóminas por estado", status);
        List<PayrollDTO> payrolls = payrollService.getPayrollsByStatus(status);
        return ResponseEntity.ok(payrolls);
    }

    // ========== ACCIONES DE NEGOCIO ==========

    @PostMapping("/{id}/process")
    public ResponseEntity<PayrollDTO> processPayroll(@PathVariable Long id) {
        log.info("POST /api/payrolls/{}/process - Procesando nómina", id);
        PayrollDTO processed = payrollService.processPayroll(id);
        return ResponseEntity.ok(processed);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<PayrollDTO> markAsPaid(
            @PathVariable Long id,
            @RequestParam Long accountId) {
        log.info("POST /api/payrolls/{}/pay - Marcando como pagada a cuenta {}", id, accountId);
        PayrollDTO paid = payrollService.markAsPaid(id, accountId);
        return ResponseEntity.ok(paid);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PayrollDTO> cancelPayroll(@PathVariable Long id) {
        log.info("POST /api/payrolls/{}/cancel - Cancelando nómina", id);
        PayrollDTO cancelled = payrollService.cancelPayroll(id);
        return ResponseEntity.ok(cancelled);
    }

    // ========== VERIFICACIONES ==========

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsPayrollForPeriod(
            @RequestParam Long characterId,
            @RequestParam Long jobId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        log.info("GET /api/payrolls/exists - Verificando si existe nómina para personaje {} trabajo {} en {}/{}",
                characterId, jobId, month, year);
        boolean exists = payrollService.existsPayrollForPeriod(characterId, jobId, year, month);
        return ResponseEntity.ok(exists);
    }

    // ========== ESTADÍSTICAS ==========

    @GetMapping("/character/{characterId}/total")
    public ResponseEntity<BigDecimal> getTotalPaidByCharacterAndPeriod(
            @PathVariable Long characterId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        log.info("GET /api/payrolls/character/{}/total - Total pagado en {}/{}", characterId, month, year);
        BigDecimal total = payrollService.getTotalPaidByCharacterAndPeriod(characterId, year, month);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/character/{characterId}/total/year")
    public ResponseEntity<BigDecimal> getTotalPaidByCharacterAndYear(
            @PathVariable Long characterId,
            @RequestParam Integer year) {
        log.info("GET /api/payrolls/character/{}/total/year - Total pagado en año {}", characterId, year);
        BigDecimal total = payrollService.getTotalPaidByCharacterAndYear(characterId, year);
        return ResponseEntity.ok(total);
    }
}
