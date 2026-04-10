package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.CharacterJobService;
import com.pet.businessdomain.shareddto.dto.CharacterJobDTO;
import com.pet.businessdomain.shareddto.dto.WorkProgressDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/character-jobs")
@RequiredArgsConstructor
public class CharacterJobController {

    private final CharacterJobService characterJobService;

    // ===== CRUD BÁSICO =====

    /**
     * Inicia un nuevo trabajo a partir de una aplicación aprobada
     *
     * @param applicationId ID de la aplicación de trabajo aprobada
     * @return CharacterJobDTO con los datos del trabajo iniciado
     *
     * @example POST /api/character-jobs/start/123
     */
    @PostMapping("/start/{applicationId}")
    public ResponseEntity<CharacterJobDTO> startJob(@PathVariable(name = "applicationId") Long applicationId) {
        log.info("POST /api/character-jobs/start/{} - Start job from application", applicationId);
        return ResponseEntity.ok(characterJobService.startJob(applicationId));
    }

    /**
     * Obtiene un trabajo de personaje por su ID
     *
     * @param id ID del trabajo del personaje
     * @return CharacterJobDTO con los datos del trabajo
     *
     * @example GET /api/character-jobs/456
     */
    @GetMapping("/{id}")
    public ResponseEntity<CharacterJobDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/character-jobs/{} - Get character job", id);
        return ResponseEntity.ok(characterJobService.getById(id));
    }

    /**
     * Obtiene el trabajo actual (activo) de un personaje
     *
     * @param characterId ID del personaje
     * @return CharacterJobDTO con el trabajo actual o 204 No Content si no tiene trabajo activo
     *
     * @example GET /api/character-jobs/current/789
     */
    @GetMapping("/current/{characterId}")
    public ResponseEntity<CharacterJobDTO> getCurrentJob(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/character-jobs/current/{} - Get current job", characterId);
        CharacterJobDTO job = characterJobService.getCurrentJob(characterId);
        return job != null ? ResponseEntity.ok(job) : ResponseEntity.noContent().build();
    }

    /**
     * Renuncia voluntariamente a un trabajo
     *
     * @param id ID del trabajo del personaje
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/character-jobs/456/resign
     */
    @PatchMapping("/{id}/resign")
    public ResponseEntity<Void> resign(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/character-jobs/{}/resign - Resign from job", id);
        characterJobService.resign(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Termina un trabajo (despido o finalización forzosa)
     *
     * @param id ID del trabajo del personaje
     * @param reason Razón de la terminación (opcional)
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/character-jobs/456/terminate?reason=low+performance
     */
    @PatchMapping("/{id}/terminate")
    public ResponseEntity<Void> terminate(
            @PathVariable(name = "id") Long id,
            @RequestParam(required = false) String reason) {
        log.info("PATCH /api/character-jobs/{}/terminate - Terminate job: {}", id, reason);
        characterJobService.terminate(id, reason);
        return ResponseEntity.noContent().build();
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todos los trabajos (activos e históricos) de un personaje
     *
     * @param characterId ID del personaje
     * @return Lista de CharacterJobDTO con todos los trabajos del personaje
     *
     * @example GET /api/character-jobs/character/789
     */
    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<CharacterJobDTO>> getByCharacter(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/character-jobs/character/{} - Get jobs by character", characterId);
        return ResponseEntity.ok(characterJobService.getByCharacter(characterId));
    }

    /**
     * Obtiene todos los trabajos de un personaje (endpoint alternativo que devuelve lista directamente)
     *
     * @param characterId ID del personaje
     * @return Lista de CharacterJobDTO con todos los trabajos del personaje
     *
     * @example GET /api/character-jobs/character/all/789
     */
    @GetMapping("/character/all/{characterId}")
    public List<CharacterJobDTO> getByCharacterAll(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/character-jobs/character/all/{} - Get jobs by character", characterId);
        return characterJobService.getByCharacter(characterId);
    }

    /**
     * Obtiene solo los trabajos activos de un personaje
     *
     * @param characterId ID del personaje
     * @return Lista de CharacterJobDTO con los trabajos activos
     *
     * @example GET /api/character-jobs/character/789/active
     */
    @GetMapping("/character/{characterId}/active")
    public ResponseEntity<List<CharacterJobDTO>> getActiveByCharacter(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/character-jobs/character/{}/active - Get active jobs by character", characterId);
        return ResponseEntity.ok(characterJobService.getActiveByCharacter(characterId));
    }

    /**
     * Obtiene el historial laboral (trabajos finalizados) de un personaje
     *
     * @param characterId ID del personaje
     * @return Lista de CharacterJobDTO con los trabajos históricos
     *
     * @example GET /api/character-jobs/character/789/history
     */
    @GetMapping("/character/{characterId}/history")
    public ResponseEntity<List<CharacterJobDTO>> getHistoryByCharacter(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/character-jobs/character/{}/history - Get job history", characterId);
        return ResponseEntity.ok(characterJobService.getHistoryByCharacter(characterId));
    }

    /**
     * Obtiene todos los trabajos asociados a una empresa
     *
     * @param companyId ID de la empresa
     * @return Lista de CharacterJobDTO con los trabajos en esa empresa
     *
     * @example GET /api/character-jobs/company/100
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CharacterJobDTO>> getByCompany(@PathVariable(name = "companyId") Long companyId) {
        log.info("GET /api/character-jobs/company/{} - Get jobs by company", companyId);
        return ResponseEntity.ok(characterJobService.getByCompany(companyId));
    }

    // ===== ACCIONES LABORALES =====

    /**
     * Registra un día de trabajo, actualizando rendimiento, estrés y ganancias
     *
     * @param id ID del trabajo del personaje
     * @param hoursWorked Horas trabajadas en el día
     * @return WorkProgressDTO con el progreso actualizado
     *
     * @example POST /api/character-jobs/456/work-day?hoursWorked=8
     */
    @PostMapping("/{id}/work-day")
    public ResponseEntity<WorkProgressDTO> workDay(
            @PathVariable(name = "id") Long id,
            @RequestParam Integer hoursWorked) {
        log.info("POST /api/character-jobs/{}/work-day - Work {} hours", id, hoursWorked);
        return ResponseEntity.ok(characterJobService.workDay(id, hoursWorked));
    }

    /**
     * Registra tiempo de estudio en el trabajo, mejorando habilidades
     *
     * @param id ID del trabajo del personaje
     * @return WorkProgressDTO con el progreso actualizado
     *
     * @example POST /api/character-jobs/456/study
     */
    @PostMapping("/{id}/study")
    public ResponseEntity<WorkProgressDTO> studyOnJob(@PathVariable(name = "id") Long id) {
        log.info("POST /api/character-jobs/{}/study - Study on job", id);
        return ResponseEntity.ok(characterJobService.studyOnJob(id));
    }

    /**
     * Solicita un ascenso en el trabajo actual
     *
     * @param id ID del trabajo del personaje
     * @return WorkProgressDTO con el resultado de la solicitud
     *
     * @example POST /api/character-jobs/456/request-promotion
     */
    @PostMapping("/{id}/request-promotion")
    public ResponseEntity<WorkProgressDTO> requestPromotion(@PathVariable(name = "id") Long id) {
        log.info("POST /api/character-jobs/{}/request-promotion - Request promotion", id);
        return ResponseEntity.ok(characterJobService.requestPromotion(id));
    }

    /**
     * Solicita un aumento de sueldo en el trabajo actual
     *
     * @param id ID del trabajo del personaje
     * @return WorkProgressDTO con el resultado de la solicitud
     *
     * @example POST /api/character-jobs/456/request-raise
     */
    @PostMapping("/{id}/request-raise")
    public ResponseEntity<WorkProgressDTO> requestRaise(@PathVariable(name = "id") Long id) {
        log.info("POST /api/character-jobs/{}/request-raise - Request raise", id);
        return ResponseEntity.ok(characterJobService.requestRaise(id));
    }

    // ===== ESTADÍSTICAS Y PROGRESO =====

    /**
     * Actualiza el rendimiento del personaje en su trabajo
     *
     * @param id ID del trabajo del personaje
     * @param performance Nuevo valor de rendimiento (0-100)
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/character-jobs/456/performance?performance=85
     */
    @PatchMapping("/{id}/performance")
    public ResponseEntity<Void> updatePerformance(
            @PathVariable(name = "id") Long id,
            @RequestParam Integer performance) {
        log.info("PATCH /api/character-jobs/{}/performance - Update performance to {}", id, performance);
        characterJobService.updatePerformance(id, performance);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza la satisfacción laboral del personaje
     *
     * @param id ID del trabajo del personaje
     * @param satisfaction Nuevo valor de satisfacción (0-100)
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/character-jobs/456/satisfaction?satisfaction=90
     */
    @PatchMapping("/{id}/satisfaction")
    public ResponseEntity<Void> updateSatisfaction(
            @PathVariable(name = "id") Long id,
            @RequestParam Integer satisfaction) {
        log.info("PATCH /api/character-jobs/{}/satisfaction - Update satisfaction to {}", id, satisfaction);
        characterJobService.updateSatisfaction(id, satisfaction);
        return ResponseEntity.noContent().build();
    }

    /**
     * Modifica el nivel de estrés del personaje
     *
     * @param id ID del trabajo del personaje
     * @param stressChange Cambio en el estrés (puede ser positivo o negativo)
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/character-jobs/456/stress?stressChange=10
     */
    @PatchMapping("/{id}/stress")
    public ResponseEntity<Void> updateStress(
            @PathVariable(name = "id") Long id,
            @RequestParam Integer stressChange) {
        log.info("PATCH /api/character-jobs/{}/stress - Update stress by {}", id, stressChange);
        characterJobService.updateStress(id, stressChange);
        return ResponseEntity.noContent().build();
    }

    /**
     * Añade un ascenso al registro del personaje
     *
     * @param id ID del trabajo del personaje
     * @return 204 No Content si la operación es exitosa
     *
     * @example POST /api/character-jobs/456/add-promotion
     */
    @PostMapping("/{id}/add-promotion")
    public ResponseEntity<Void> addPromotion(@PathVariable(name = "id") Long id) {
        log.info("POST /api/character-jobs/{}/add-promotion - Add promotion", id);
        characterJobService.addPromotion(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Añade un bono al registro del personaje
     *
     * @param id ID del trabajo del personaje
     * @param amount Monto del bono
     * @return 204 No Content si la operación es exitosa
     *
     * @example POST /api/character-jobs/456/add-bonus?amount=500
     */
    @PostMapping("/{id}/add-bonus")
    public ResponseEntity<Void> addBonus(
            @PathVariable(name = "id") Long id,
            @RequestParam Integer amount) {
        log.info("POST /api/character-jobs/{}/add-bonus - Add bonus of {}", id, amount);
        characterJobService.addBonus(id, amount);
        return ResponseEntity.noContent().build();
    }

    // ===== VERIFICACIONES =====

    /**
     * Verifica si un personaje tiene un trabajo activo
     *
     * @param characterId ID del personaje
     * @return true si tiene trabajo activo, false en caso contrario
     *
     * @example GET /api/character-jobs/character/789/has-active
     */
    @GetMapping("/character/{characterId}/has-active")
    public ResponseEntity<Boolean> hasActiveJob(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/character-jobs/character/{}/has-active - Check if has active job", characterId);
        return ResponseEntity.ok(characterJobService.hasActiveJob(characterId));
    }

    /**
     * Verifica si el personaje puede trabajar hoy (no ha excedido horas o ya trabajó)
     *
     * @param id ID del trabajo del personaje
     * @return true si puede trabajar, false en caso contrario
     *
     * @example GET /api/character-jobs/456/can-work-today
     */
    @GetMapping("/{id}/can-work-today")
    public ResponseEntity<Boolean> canWorkToday(@PathVariable(name = "id") Long id) {
        log.info("GET /api/character-jobs/{}/can-work-today - Check if can work today", id);
        return ResponseEntity.ok(characterJobService.canWorkToday(id));
    }

    /**
     * Verifica si el personaje puede solicitar un ascenso
     *
     * @param id ID del trabajo del personaje
     * @return true si puede solicitar ascenso, false en caso contrario
     *
     * @example GET /api/character-jobs/456/can-request-promotion
     */
    @GetMapping("/{id}/can-request-promotion")
    public ResponseEntity<Boolean> canRequestPromotion(@PathVariable(name = "id") Long id) {
        log.info("GET /api/character-jobs/{}/can-request-promotion - Check if can request promotion", id);
        return ResponseEntity.ok(characterJobService.canRequestPromotion(id));
    }
}