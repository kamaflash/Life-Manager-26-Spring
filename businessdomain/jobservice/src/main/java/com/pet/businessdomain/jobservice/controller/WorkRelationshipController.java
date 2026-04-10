package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.WorkRelationshipService;
import com.pet.businessdomain.shareddto.dto.WorkRelationshipDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/work-relationships")
@RequiredArgsConstructor
public class WorkRelationshipController {

    private final WorkRelationshipService workRelationshipService;

    // ===== CRUD BÁSICO =====

    /**
     * Crea una nueva relación laboral entre personajes
     *
     * @param dto Datos de la relación a crear (characterJobId, relatedCharacterId, relationshipType, affinity)
     * @return WorkRelationshipDTO con los datos de la relación creada
     *
     * @example POST /api/work-relationships
     * @example Body: { "characterJobId": 456, "relatedCharacterId": 789, "relationshipType": "COLLEAGUE", "affinity": 50 }
     */
    @PostMapping
    public ResponseEntity<WorkRelationshipDTO> create(@RequestBody WorkRelationshipDTO dto) {
        log.info("POST /api/work-relationships - Create work relationship");
        return ResponseEntity.ok(workRelationshipService.create(dto));
    }

    /**
     * Actualiza una relación laboral existente
     *
     * @param id ID de la relación a actualizar
     * @param dto Nuevos datos de la relación
     * @return WorkRelationshipDTO con los datos actualizados
     *
     * @example PUT /api/work-relationships/123
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkRelationshipDTO> update(
            @PathVariable(name = "id") Long id,
            @RequestBody WorkRelationshipDTO dto) {
        log.info("PUT /api/work-relationships/{} - Update work relationship", id);
        return ResponseEntity.ok(workRelationshipService.update(id, dto));
    }

    /**
     * Obtiene una relación laboral por su ID
     *
     * @param id ID de la relación
     * @return WorkRelationshipDTO con los datos de la relación
     *
     * @example GET /api/work-relationships/123
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkRelationshipDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/work-relationships/{} - Get work relationship", id);
        return ResponseEntity.ok(workRelationshipService.getById(id));
    }

    /**
     * Elimina una relación laboral
     *
     * @param id ID de la relación a eliminar
     * @return 204 No Content si la operación es exitosa
     *
     * @example DELETE /api/work-relationships/123
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        log.info("DELETE /api/work-relationships/{} - Delete work relationship", id);
        workRelationshipService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todas las relaciones laborales de un trabajo de personaje
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de WorkRelationshipDTO con todas las relaciones del trabajo
     *
     * @example GET /api/work-relationships/job/456
     */
    @GetMapping("/job/{characterJobId}")
    public ResponseEntity<List<WorkRelationshipDTO>> getByCharacterJob(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{} - Get relationships by job", characterJobId);
        return ResponseEntity.ok(workRelationshipService.getByCharacterJob(characterJobId));
    }

    /**
     * Obtiene la relación con el jefe del personaje
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de WorkRelationshipDTO con la relación de jefe (normalmente 1 o 0 elementos)
     *
     * @example GET /api/work-relationships/job/456/boss
     */
    @GetMapping("/job/{characterJobId}/boss")
    public ResponseEntity<List<WorkRelationshipDTO>> getBoss(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/boss - Get boss", characterJobId);
        return ResponseEntity.ok(workRelationshipService.getBoss(characterJobId));
    }

    /**
     * Obtiene la relación con el mentor del personaje
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de WorkRelationshipDTO con la relación de mentor (normalmente 1 o 0 elementos)
     *
     * @example GET /api/work-relationships/job/456/mentor
     */
    @GetMapping("/job/{characterJobId}/mentor")
    public ResponseEntity<List<WorkRelationshipDTO>> getMentor(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/mentor - Get mentor", characterJobId);
        return ResponseEntity.ok(workRelationshipService.getMentor(characterJobId));
    }

    /**
     * Obtiene las relaciones con compañeros de trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de WorkRelationshipDTO con relaciones de tipo COLLEAGUE
     *
     * @example GET /api/work-relationships/job/456/colleagues
     */
    @GetMapping("/job/{characterJobId}/colleagues")
    public ResponseEntity<List<WorkRelationshipDTO>> getColleagues(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/colleagues - Get colleagues", characterJobId);
        return ResponseEntity.ok(workRelationshipService.getColleagues(characterJobId));
    }

    /**
     * Obtiene las relaciones con rivales laborales
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de WorkRelationshipDTO con relaciones de tipo RIVAL
     *
     * @example GET /api/work-relationships/job/456/rivals
     */
    @GetMapping("/job/{characterJobId}/rivals")
    public ResponseEntity<List<WorkRelationshipDTO>> getRivals(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/rivals - Get rivals", characterJobId);
        return ResponseEntity.ok(workRelationshipService.getRivals(characterJobId));
    }

    /**
     * Obtiene relaciones con alta afinidad (affinity > 70)
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de WorkRelationshipDTO con afinidad alta
     *
     * @example GET /api/work-relationships/job/456/high-affinity
     */
    @GetMapping("/job/{characterJobId}/high-affinity")
    public ResponseEntity<List<WorkRelationshipDTO>> getHighAffinity(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/high-affinity - Get high affinity relationships", characterJobId);
        return ResponseEntity.ok(workRelationshipService.getHighAffinity(characterJobId));
    }

    /**
     * Obtiene relaciones con baja afinidad (affinity < 30)
     *
     * @param characterJobId ID del trabajo del personaje
     * @return Lista de WorkRelationshipDTO con afinidad baja
     *
     * @example GET /api/work-relationships/job/456/low-affinity
     */
    @GetMapping("/job/{characterJobId}/low-affinity")
    public ResponseEntity<List<WorkRelationshipDTO>> getLowAffinity(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/low-affinity - Get low affinity relationships", characterJobId);
        return ResponseEntity.ok(workRelationshipService.getLowAffinity(characterJobId));
    }

    // ===== INTERACCIONES =====

    /**
     * Mejora la afinidad en una relación laboral
     *
     * @param id ID de la relación
     * @param amount Cantidad a aumentar la afinidad (1-100)
     * @return WorkRelationshipDTO con la afinidad actualizada
     *
     * @example PATCH /api/work-relationships/123/improve-affinity?amount=10
     */
    @PatchMapping("/{id}/improve-affinity")
    public ResponseEntity<WorkRelationshipDTO> improveAffinity(
            @PathVariable(name = "id") Long id,
            @RequestParam Integer amount) {
        log.info("PATCH /api/work-relationships/{}/improve-affinity - Improve affinity by {}", id, amount);
        return ResponseEntity.ok(workRelationshipService.improveAffinity(id, amount));
    }

    /**
     * Empeora la afinidad en una relación laboral
     *
     * @param id ID de la relación
     * @param amount Cantidad a disminuir la afinidad (1-100)
     * @return WorkRelationshipDTO con la afinidad actualizada
     *
     * @example PATCH /api/work-relationships/123/worsen-affinity?amount=5
     */
    @PatchMapping("/{id}/worsen-affinity")
    public ResponseEntity<WorkRelationshipDTO> worsenAffinity(
            @PathVariable(name = "id") Long id,
            @RequestParam Integer amount) {
        log.info("PATCH /api/work-relationships/{}/worsen-affinity - Worsen affinity by {}", id, amount);
        return ResponseEntity.ok(workRelationshipService.worsenAffinity(id, amount));
    }

    // ===== VERIFICACIONES =====

    /**
     * Verifica si el personaje tiene un jefe asignado
     *
     * @param characterJobId ID del trabajo del personaje
     * @return true si tiene jefe, false en caso contrario
     *
     * @example GET /api/work-relationships/job/456/has-boss
     */
    @GetMapping("/job/{characterJobId}/has-boss")
    public ResponseEntity<Boolean> hasBoss(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/has-boss - Check if has boss", characterJobId);
        return ResponseEntity.ok(workRelationshipService.hasBoss(characterJobId));
    }

    /**
     * Verifica si el personaje tiene un mentor asignado
     *
     * @param characterJobId ID del trabajo del personaje
     * @return true si tiene mentor, false en caso contrario
     *
     * @example GET /api/work-relationships/job/456/has-mentor
     */
    @GetMapping("/job/{characterJobId}/has-mentor")
    public ResponseEntity<Boolean> hasMentor(@PathVariable(name = "characterJobId") Long characterJobId) {
        log.info("GET /api/work-relationships/job/{}/has-mentor - Check if has mentor", characterJobId);
        return ResponseEntity.ok(workRelationshipService.hasMentor(characterJobId));
    }

    /**
     * Verifica si ya existe una relación entre dos personajes en el mismo trabajo
     *
     * @param characterJobId ID del trabajo del personaje
     * @param relatedCharacterId ID del otro personaje
     * @return true si ya existe la relación, false en caso contrario
     *
     * @example GET /api/work-relationships/exists?characterJobId=456&relatedCharacterId=789
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsRelationship(
            @RequestParam Long characterJobId,
            @RequestParam Long relatedCharacterId) {
        log.info("GET /api/work-relationships/exists - Check if relationship exists");
        return ResponseEntity.ok(workRelationshipService.existsRelationship(characterJobId, relatedCharacterId));
    }

    // ===== INICIALIZACIÓN =====

    /**
     * Inicializa las relaciones laborales automáticas para un nuevo trabajo
     * (crea relaciones con jefe, mentor y compañeros según la empresa)
     *
     * @param characterJobId ID del trabajo del personaje
     * @param companyId ID de la empresa
     * @return 200 OK si la operación es exitosa
     *
     * @example POST /api/work-relationships/initialize/456?companyId=100
     */
    @PostMapping("/initialize/{characterJobId}")
    public ResponseEntity<Void> initializeRelationships(
            @PathVariable(name = "characterJobId") Long characterJobId,
            @RequestParam Long companyId) {
        log.info("POST /api/work-relationships/initialize/{}/{} - Initialize relationships", characterJobId, companyId);
        workRelationshipService.initializeRelationships(characterJobId, companyId);
        return ResponseEntity.ok().build();
    }
}