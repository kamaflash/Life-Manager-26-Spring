package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.JobEventService;
import com.pet.businessdomain.shareddto.dto.JobEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/job-events")
@RequiredArgsConstructor
public class JobEventController {

    private final JobEventService jobEventService;

    // ===== CRUD BÁSICO =====

    /**
     * Obtiene un evento laboral por su ID
     *
     * @param id ID del evento
     * @return JobEventDTO con los datos del evento
     *
     * @example GET /api/job-events/123
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobEventDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-events/{} - Get event", id);
        return ResponseEntity.ok(jobEventService.getById(id));
    }

    /**
     * Crea múltiples eventos laborales en lote
     *
     * @param dtos Lista de eventos a crear
     * @return Lista de JobEventDTO con los eventos creados
     *
     * @example POST /api/job-events/batch
     * @example Body: [{ "title": "Bonus", "type": "BONUS" }, { "title": "Promotion", "type": "PROMOTION" }]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<JobEventDTO>> createBatch(@RequestBody List<JobEventDTO> dtos) {
        log.info("POST /api/job-events/batch - Creating {} job events", dtos.size());

        List<JobEventDTO> createdEvents = new ArrayList<>();
        for (JobEventDTO dto : dtos) {
            log.info("  - Creating job event: {}", dto.getTitle());
            JobEventDTO created = jobEventService.create(dto);
            createdEvents.add(created);
        }

        log.info("Successfully created {} job events", createdEvents.size());
        return ResponseEntity.ok(createdEvents);
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todos los eventos asociados a un trabajo de personaje
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de JobEventDTO con todos los eventos del trabajo
     *
     * @example GET /api/job-events/job/456
     */
    @GetMapping("/job/{characterJobId}")
    public ResponseEntity<List<JobEventDTO>> getByCharacterJob(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/job-events/job/{} - Get events by job", characterJobId);
        return ResponseEntity.ok(jobEventService.getByCharacterJob(characterJobId));
    }

    /**
     * Obtiene solo los eventos positivos de un trabajo (BONUS, PROMOTION, PROJECT_SUCCESS)
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de JobEventDTO con eventos positivos
     *
     * @example GET /api/job-events/job/456/positive
     */
    @GetMapping("/job/{characterJobId}/positive")
    public ResponseEntity<List<JobEventDTO>> getPositiveEvents(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/job-events/job/{}/positive - Get positive events", characterJobId);
        return ResponseEntity.ok(jobEventService.getPositiveEvents(characterJobId));
    }

    /**
     * Obtiene solo los eventos negativos de un trabajo (CONFLICT, BURNOUT, PROJECT_FAILURE)
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de JobEventDTO con eventos negativos
     *
     * @example GET /api/job-events/job/456/negative
     */
    @GetMapping("/job/{characterJobId}/negative")
    public ResponseEntity<List<JobEventDTO>> getNegativeEvents(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/job-events/job/{}/negative - Get negative events", characterJobId);
        return ResponseEntity.ok(jobEventService.getNegativeEvents(characterJobId));
    }

    /**
     * Obtiene los eventos más recientes de un trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @param limit Número máximo de eventos a retornar (por defecto 5)
     * @return Lista de JobEventDTO con los eventos más recientes
     *
     * @example GET /api/job-events/job/456/recent?limit=10
     */
    @GetMapping("/job/{characterJobId}/recent")
    public ResponseEntity<List<JobEventDTO>> getRecentEvents(
            @PathVariable(name = "characterJobId") Long characterJobId,
            @RequestParam(defaultValue = "5") int limit) {
        log.info("GET /api/job-events/job/{}/recent - Get recent events", characterJobId);
        return ResponseEntity.ok(jobEventService.getRecentEvents(characterJobId, limit));
    }

    // ===== GENERACIÓN DE EVENTOS =====

    /**
     * Genera un evento aleatorio para un trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @return JobEventDTO con el evento generado
     *
     * @example POST /api/job-events/job/456/random
     */
    @PostMapping("/job/{characterJobId}/random")
    public ResponseEntity<JobEventDTO> generateRandomEvent(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("POST /api/job-events/job/{}/random - Generate random event", characterJobId);
        return ResponseEntity.ok(jobEventService.generateRandomEvent(characterJobId));
    }

    /**
     * Genera un evento de bono económico
     *
     * @param characterJobId ID del trabajo del personaje
     * @param amount Monto del bono
     * @return JobEventDTO con el evento generado
     *
     * @example POST /api/job-events/job/456/bonus?amount=1000
     */
    @PostMapping("/job/{characterJobId}/bonus")
    public ResponseEntity<JobEventDTO> generateBonusEvent(
            @PathVariable(name = "characterJobId") Long characterJobId,
            @RequestParam Integer amount) {
        log.info("POST /api/job-events/job/{}/bonus - Generate bonus event of {}", characterJobId, amount);
        return ResponseEntity.ok(jobEventService.generateBonusEvent(characterJobId, amount));
    }

    /**
     * Genera un evento de ascenso
     *
     * @param characterJobId ID del trabajo del personaje
     * @return JobEventDTO con el evento generado
     *
     * @example POST /api/job-events/job/456/promotion
     */
    @PostMapping("/job/{characterJobId}/promotion")
    public ResponseEntity<JobEventDTO> generatePromotionEvent(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("POST /api/job-events/job/{}/promotion - Generate promotion event", characterJobId);
        return ResponseEntity.ok(jobEventService.generatePromotionEvent(characterJobId));
    }

    /**
     * Genera un evento de conflicto laboral
     *
     * @param characterJobId ID del trabajo del personaje
     * @param withCharacterId ID del otro personaje involucrado (opcional)
     * @return JobEventDTO con el evento generado
     *
     * @example POST /api/job-events/job/456/conflict?withCharacterId=789
     */
    @PostMapping("/job/{characterJobId}/conflict")
    public ResponseEntity<JobEventDTO> generateConflictEvent(
            @PathVariable(name = "characterJobId") Long characterJobId,
            @RequestParam(required = false) Long withCharacterId) {
        log.info("POST /api/job-events/job/{}/conflict - Generate conflict event", characterJobId);
        return ResponseEntity.ok(jobEventService.generateConflictEvent(characterJobId, withCharacterId));
    }

    /**
     * Genera un evento de éxito en proyecto (XP y recompensas positivas)
     *
     * @param characterJobId ID del trabajo del personaje
     * @param xpReward Cantidad de XP otorgada
     * @return JobEventDTO con el evento generado
     *
     * @example POST /api/job-events/job/456/project-success?xpReward=500
     */
    @PostMapping("/job/{characterJobId}/project-success")
    public ResponseEntity<JobEventDTO> generateProjectSuccessEvent(
            @PathVariable(name = "characterJobId") Long characterJobId,
            @RequestParam Integer xpReward) {
        log.info("POST /api/job-events/job/{}/project-success - Generate project success event", characterJobId);
        return ResponseEntity.ok(jobEventService.generateProjectSuccessEvent(characterJobId, xpReward));
    }

    /**
     * Genera un evento de fracaso en proyecto (consecuencias negativas)
     *
     * @param characterJobId ID del trabajo del personaje
     * @return JobEventDTO con el evento generado
     *
     * @example POST /api/job-events/job/456/project-failure
     */
    @PostMapping("/job/{characterJobId}/project-failure")
    public ResponseEntity<JobEventDTO> generateProjectFailureEvent(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("POST /api/job-events/job/{}/project-failure - Generate project failure event", characterJobId);
        return ResponseEntity.ok(jobEventService.generateProjectFailureEvent(characterJobId));
    }

    /**
     * Genera un evento de burnout (agotamiento laboral)
     *
     * @param characterJobId ID del trabajo del personaje
     * @return JobEventDTO con el evento generado
     *
     * @example POST /api/job-events/job/456/burnout
     */
    @PostMapping("/job/{characterJobId}/burnout")
    public ResponseEntity<JobEventDTO> generateBurnoutEvent(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("POST /api/job-events/job/{}/burnout - Generate burnout event", characterJobId);
        return ResponseEntity.ok(jobEventService.generateBurnoutEvent(characterJobId));
    }

    // ===== PROCESAMIENTO =====

    /**
     * Marca un evento como resuelto
     *
     * @param id ID del evento a resolver
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/job-events/123/resolve
     */
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveEvent(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/job-events/{}/resolve - Resolve event", id);
        jobEventService.resolveEvent(id);
        return ResponseEntity.noContent().build();
    }

    // ===== ESTADÍSTICAS =====

    /**
     * Cuenta los eventos de un tipo específico para un trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @param eventType Tipo de evento (BONUS, PROMOTION, CONFLICT, BURNOUT, PROJECT_SUCCESS, PROJECT_FAILURE)
     * @return Número de eventos de ese tipo
     *
     * @example GET /api/job-events/job/456/count/BONUS
     */
    @GetMapping("/job/{characterJobId}/count/{eventType}")
    public ResponseEntity<Long> countEventsByType(
            @PathVariable(name = "characterJobId") Long characterJobId,
            @PathVariable(name = "eventType") String eventType) {
        log.info("GET /api/job-events/job/{}/count/{} - Count events by type", characterJobId, eventType);
        return ResponseEntity.ok(jobEventService.countEventsByType(characterJobId, eventType));
    }
}