package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.MissionService;
import com.pet.businessdomain.shareddto.dto.JobVacancyDTO;
import com.pet.businessdomain.shareddto.dto.MissionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // ===== CRUD BÁSICO =====

    /**
     * Crea una nueva misión
     *
     * @param dto Datos de la misión a crear
     * @return MissionDTO con los datos de la misión creada
     *
     * @example POST /api/missions
     * @example Body: { "title": "Completar proyecto", "difficulty": "MEDIUM", "xpReward": 100 }
     */
    @PostMapping
    public ResponseEntity<MissionDTO> create(@RequestBody MissionDTO dto) {
        log.info("POST /api/missions - Create mission: {}", dto.getTitle());
        return ResponseEntity.ok(missionService.create(dto));
    }

    /**
     * Crea múltiples misiones en lote
     *
     * @param dtos Lista de misiones a crear
     * @return Lista de MissionDTO con las misiones creadas
     *
     * @example POST /api/missions/batch
     * @example Body: [{ "title": "Misión 1" }, { "title": "Misión 2" }]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<MissionDTO>> createBatch(@RequestBody List<MissionDTO> dtos) {
        log.info("POST /api/missions/batch - Creating {} missions", dtos.size());

        List<MissionDTO> createdMissions = new ArrayList<>();
        for (MissionDTO dto : dtos) {
            log.info("  - Creating mission: {}", dto.getTitle());
            MissionDTO created = missionService.create(dto);
            createdMissions.add(created);
        }

        log.info("Successfully created {} missions", createdMissions.size());
        return ResponseEntity.ok(createdMissions);
    }

    /**
     * Obtiene una misión por su ID
     *
     * @param id ID de la misión
     * @return MissionDTO con los datos de la misión
     *
     * @example GET /api/missions/123
     */
    @GetMapping("/{id}")
    public ResponseEntity<MissionDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/missions/{} - Get mission", id);
        return ResponseEntity.ok(missionService.getById(id));
    }

    /**
     * Elimina una misión
     *
     * @param id ID de la misión a eliminar
     * @return 204 No Content si la operación es exitosa
     *
     * @example DELETE /api/missions/123
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        log.info("DELETE /api/missions/{} - Delete mission", id);
        missionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todas las misiones de un trabajo de personaje
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de MissionDTO con todas las misiones del trabajo
     *
     * @example GET /api/missions/job/456
     */
    @GetMapping("/job/{characterJobId}")
    public ResponseEntity<List<MissionDTO>> getByCharacterJob(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/missions/job/{} - Get missions by job", characterJobId);
        return ResponseEntity.ok(missionService.getByCharacterJob(characterJobId));
    }

    /**
     * Obtiene solo las misiones activas (no completadas y no expiradas) de un trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de MissionDTO con misiones activas
     *
     * @example GET /api/missions/job/456/active
     */
    @GetMapping("/job/{characterJobId}/active")
    public ResponseEntity<List<MissionDTO>> getActiveMissions(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/missions/job/{}/active - Get active missions", characterJobId);
        return ResponseEntity.ok(missionService.getActiveMissions(characterJobId));
    }

    /**
     * Obtiene las misiones completadas de un trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de MissionDTO con misiones completadas
     *
     * @example GET /api/missions/job/456/completed
     */
    @GetMapping("/job/{characterJobId}/completed")
    public ResponseEntity<List<MissionDTO>> getCompletedMissions(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/missions/job/{}/completed - Get completed missions", characterJobId);
        return ResponseEntity.ok(missionService.getCompletedMissions(characterJobId));
    }

    /**
     * Obtiene misiones por nivel de dificultad
     *
     * @param difficulty Dificultad (EASY, MEDIUM, HARD, EPIC)
     * @return Lista de MissionDTO con misiones de esa dificultad
     *
     * @example GET /api/missions/difficulty/HARD
     */
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<MissionDTO>> getMissionsByDifficulty(@PathVariable(name = "difficulty") String difficulty) {
        log.info("GET /api/missions/difficulty/{} - Get missions by difficulty", difficulty);
        return ResponseEntity.ok(missionService.getMissionsByDifficulty(difficulty));
    }

    /**
     * Obtiene todas las misiones expiradas
     *
     * @return Lista de MissionDTO con misiones expiradas
     *
     * @example GET /api/missions/expired
     */
    @GetMapping("/expired")
    public ResponseEntity<List<MissionDTO>> getExpiredMissions() {
        log.info("GET /api/missions/expired - Get expired missions");
        return ResponseEntity.ok(missionService.getExpiredMissions());
    }

    /**
     * Obtiene misiones que están por expirar (próximas 24 horas)
     *
     * @return Lista de MissionDTO con misiones próximas a expirar
     *
     * @example GET /api/missions/expiring-soon
     */
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<MissionDTO>> getExpiringSoon() {
        log.info("GET /api/missions/expiring-soon - Get missions expiring soon");
        return ResponseEntity.ok(missionService.getExpiringSoon());
    }

    // ===== ACCIONES =====

    /**
     * Asigna una misión a un trabajo de personaje
     *
     * @param characterJobId ID del trabajo del personaje
     * @param mission Datos de la misión a asignar
     * @return MissionDTO con la misión asignada
     *
     * @example POST /api/missions/assign?characterJobId=456
     * @example Body: { "title": "Nueva misión", "difficulty": "EASY" }
     */
    @PostMapping("/assign")
    public ResponseEntity<MissionDTO> assignMission(
            @RequestParam Long characterJobId,
            @RequestBody MissionDTO mission) {
        log.info("POST /api/missions/assign - Assign mission to job {}", characterJobId);
        return ResponseEntity.ok(missionService.assignMission(characterJobId, mission));
    }

    /**
     * Completa una misión y otorga las recompensas correspondientes
     *
     * @param id ID de la misión a completar
     * @return MissionDTO con la misión actualizada (completada = true)
     *
     * @example POST /api/missions/123/complete
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<MissionDTO> completeMission(@PathVariable(name = "id") Long id) {
        log.info("POST /api/missions/{}/complete - Complete mission", id);
        return ResponseEntity.ok(missionService.completeMission(id));
    }

    /**
     * Marca una misión como fallida (sin recompensas)
     *
     * @param id ID de la misión a marcar como fallida
     * @return 204 No Content si la operación es exitosa
     *
     * @example POST /api/missions/123/fail
     */
    @PostMapping("/{id}/fail")
    public ResponseEntity<Void> failMission(@PathVariable(name = "id") Long id) {
        log.info("POST /api/missions/{}/fail - Fail mission", id);
        missionService.failMission(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica y actualiza automáticamente las misiones expiradas
     *
     * @return 200 OK si la operación es exitosa
     *
     * @example POST /api/missions/check-expired
     */
    @PostMapping("/check-expired")
    public ResponseEntity<Void> checkExpiredMissions() {
        log.info("POST /api/missions/check-expired - Check expired missions");
        missionService.checkExpiredMissions();
        return ResponseEntity.ok().build();
    }

    // ===== ESTADÍSTICAS =====

    /**
     * Calcula la tasa de completitud de misiones para un trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Porcentaje de misiones completadas (0.0 - 100.0)
     *
     * @example GET /api/missions/job/456/completion-rate
     */
    @GetMapping("/job/{characterJobId}/completion-rate")
    public ResponseEntity<Double> getCompletionRate(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/missions/job/{}/completion-rate - Get completion rate", characterJobId);
        return ResponseEntity.ok(missionService.getCompletionRate(characterJobId));
    }

    /**
     * Obtiene una misión aleatoria de una dificultad específica
     *
     * @param characterJobId ID del trabajo del personaje
     * @param difficulty Dificultad de la misión (EASY, MEDIUM, HARD, EPIC)
     * @return MissionDTO con una misión aleatoria, o 204 No Content si no hay disponibles
     *
     * @example GET /api/missions/job/456/random/MEDIUM
     */
    @GetMapping("/job/{characterJobId}/random/{difficulty}")
    public ResponseEntity<MissionDTO> getRandomMissionByDifficulty(
            @PathVariable(name = "characterJobId") Long characterJobId,
            @PathVariable(name = "difficulty") String difficulty) {
        log.info("GET /api/missions/job/{}/random/{} - Get random mission", characterJobId, difficulty);
        MissionDTO mission = missionService.getRandomMissionByDifficulty(characterJobId, difficulty);
        return mission != null ? ResponseEntity.ok(mission) : ResponseEntity.noContent().build();
    }

    // ===== VERIFICACIONES =====

    /**
     * Verifica si una misión ha expirado
     *
     * @param id ID de la misión
     * @return true si ha expirado, false en caso contrario
     *
     * @example GET /api/missions/123/is-expired
     */
    @GetMapping("/{id}/is-expired")
    public ResponseEntity<Boolean> isMissionExpired(@PathVariable(name = "id") Long id) {
        log.info("GET /api/missions/{}/is-expired - Check if mission expired", id);
        return ResponseEntity.ok(missionService.isMissionExpired(id));
    }

    /**
     * Verifica si una misión puede ser completada (no expirada, no completada, tiempo suficiente)
     *
     * @param id ID de la misión
     * @return true si se puede completar, false en caso contrario
     *
     * @example GET /api/missions/123/can-complete
     */
    @GetMapping("/{id}/can-complete")
    public ResponseEntity<Boolean> canCompleteMission(@PathVariable(name = "id") Long id) {
        log.info("GET /api/missions/{}/can-complete - Check if can complete mission", id);
        return ResponseEntity.ok(missionService.canCompleteMission(id));
    }
}