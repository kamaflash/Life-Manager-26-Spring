package com.pet.businessdomain.eventservice.controller;

import com.pet.businessdomain.eventservice.exceptions.BusinessValidationException;
import com.pet.businessdomain.eventservice.exceptions.ResourceNotFoundException;
import com.pet.businessdomain.eventservice.services.MissionService;
import com.pet.businessdomain.shareddto.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de misiones del sistema.
 *
 * <p>Este controlador proporciona endpoints para:</p>
 * <ul>
 *   <li>Gestión CRUD de misiones (crear, leer, actualizar, eliminar)</li>
 *   <li>Gestión de cadenas de misiones (mission chains)</li>
 *   <li>Aceptación y progreso de misiones por personajes</li>
 *   <li>Consulta de misiones disponibles, activas y completadas</li>
 *   <li>Procesamiento automático de misiones</li>
 * </ul>
 *
 * <p>URL base: {@code /api/missions}</p>
 *
 * @author PET Business Domain
 * @version 1.0
 * @since 2024
 * @see MissionService
 * @see com.pet.businessdomain.shareddto.enumentities.EnumAll.MissionType
 * @see com.pet.businessdomain.shareddto.enumentities.EnumAll.MissionCategory
 * @see com.pet.businessdomain.shareddto.enumentities.EnumAll.MissionStatus
 */
@Slf4j
@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // ==================================================================
    //                    GESTIÓN DE MISIONES (CRUD)
    // ==================================================================

    /**
     * Crea una nueva misión en el sistema.
     *
     * <p>Valida los datos de la misión antes de persistirla en base de datos.</p>
     *
     * <p>Tipos de misión válidos:</p>
     * <ul>
     *   <li>ONE_TIME - Misión única</li>
     *   <li>DAILY - Misión diaria</li>
     *   <li>WEEKLY - Misión semanal</li>
     *   <li>MONTHLY - Misión mensual</li>
     *   <li>REPEATABLE - Misión repetible</li>
     *   <li>STORY - Misión de historia principal</li>
     * </ul>
     *
     * <p>Categorías válidas:</p>
     * <ul>
     *   <li>STUDY - Estudio</li>
     *   <li>WORK - Trabajo</li>
     *   <li>SOCIAL - Social</li>
     *   <li>ROMANCE - Romance</li>
     *   <li>EXPLORATION - Exploración</li>
     *   <li>TRAINING - Entrenamiento</li>
     *   <li>PERSONAL - Personal</li>
     *   <li>SPECIAL - Especial</li>
     * </ul>
     *
     * @param missionDto Datos de la misión a crear (debe ser válido)
     * @return ResponseEntity con la misión creada y estado HTTP 201 (CREATED)
     * @throws BusinessValidationException Si el código de misión ya existe o los datos son inválidos
     *
     * @example
     * POST /api/missions
     * {
     *   "code": "MISSION_WORK_001",
     *   "title": "Primer Día de Trabajo",
     *   "type": "ONE_TIME",
     *   "category": "WORK",
     *   "difficulty": "EASY",
     *   "minAge": 16,
     *   "objectives": [...],
     *   "rewards": [...]
     * }
     */
    @PostMapping
    public ResponseEntity<MissionResponseDto> createMission(@Valid @RequestBody MissionResponseDto missionDto) {
        log.info("📝 Creating new mission with code: {}", missionDto.getCode());
        MissionResponseDto createdMission = missionService.createMission(missionDto);
        log.info("✅ Mission created successfully with ID: {}", createdMission.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMission);
    }

    /**
     * Crea múltiples misiones en una sola petición (operación batch).
     *
     * <p>Útil para cargas iniciales de datos o importaciones masivas.
     * Las misiones que ya existan (mismo código) serán omitidas.</p>
     *
     * @param dtos Lista de misiones a crear
     * @return ResponseEntity con la lista de misiones creadas exitosamente y estado HTTP 201 (CREATED)
     *
     * @example
     * POST /api/missions/batch
     * [
     *   { "code": "MISSION_001", ... },
     *   { "code": "MISSION_002", ... }
     * ]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<MissionResponseDto>> createMissionsBatch(@RequestBody List<MissionResponseDto> dtos) {
        log.info("📦 Processing batch creation of {} missions", dtos.size());
        List<MissionResponseDto> created = missionService.createMissionBatch(dtos);
        log.info("✅ Successfully created {} out of {} missions", created.size(), dtos.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualiza completamente una misión existente.
     *
     * <p>Reemplaza todos los campos de la misión con los nuevos valores proporcionados.</p>
     *
     * @param id         ID de la misión a actualizar
     * @param missionDto Nuevos datos de la misión (debe ser válido)
     * @return ResponseEntity con la misión actualizada y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si la misión con el ID especificado no existe
     *
     * @example
     * PUT /api/missions/1
     * {
     *   "code": "MISSION_WORK_001",
     *   "title": "Primer Día de Trabajo (Actualizado)",
     *   ...
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<MissionResponseDto> updateMission(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody MissionResponseDto missionDto) {
        log.info("🔄 Updating mission with ID: {}", id);
        MissionResponseDto updatedMission = missionService.updateMission(id, missionDto);
        log.info("✅ Mission updated successfully with ID: {}", id);
        return ResponseEntity.ok(updatedMission);
    }

    /**
     * Obtiene una misión por su identificador único (ID).
     *
     * @param id ID de la misión a consultar
     * @return ResponseEntity con la misión encontrada y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si la misión con el ID especificado no existe
     *
     * @example
     * GET /api/missions/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<MissionResponseDto> getMissionById(@PathVariable(name = "id") Long id) {
        log.debug("🔍 Fetching mission with ID: {}", id);
        MissionResponseDto mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    /**
     * Obtiene una misión por su código único.
     *
     * <p>El código es un identificador legible como "MISSION_WORK_001".</p>
     *
     * @param code Código único de la misión
     * @return ResponseEntity con la misión encontrada y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si la misión con el código especificado no existe
     *
     * @example
     * GET /api/missions/code/MISSION_WORK_001
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<MissionResponseDto> getMissionByCode(@PathVariable(name = "code") String code) {
        log.debug("🔍 Fetching mission with code: {}", code);
        MissionResponseDto mission = missionService.getMissionByCode(code);
        return ResponseEntity.ok(mission);
    }

    /**
     * Obtiene todas las misiones del sistema (sin paginación).
     *
     * <p><b>⚠️ Precaución:</b> Este endpoint puede devolver una gran cantidad de datos.
     * Para entornos de producción, se recomienda usar {@link #getAllMissionsPaginated(Pageable)}.</p>
     *
     * @return ResponseEntity con la lista completa de misiones y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions
     */
    @GetMapping
    public ResponseEntity<List<MissionResponseDto>> getAllMissions() {
        log.debug("📋 Fetching all missions");
        List<MissionResponseDto> missions = missionService.getAllMissions();
        log.debug("📋 Found {} missions", missions.size());
        return ResponseEntity.ok(missions);
    }

    /**
     * Obtiene todas las misiones con paginación.
     *
     * <p>Soporta ordenación por cualquier campo de la misión.</p>
     *
     * @param pageable Configuración de paginación y ordenación.
     *                 Por defecto: página 0, 20 elementos, ordenado por createdAt descendente.
     * @return ResponseEntity con la página de misiones y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/paginated?page=0&size=10&sort=createdAt,desc
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<MissionResponseDto>> getAllMissionsPaginated(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("📄 Fetching paginated missions - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<MissionResponseDto> missions = missionService.getAllMissionsPaginated(pageable);
        return ResponseEntity.ok(missions);
    }

    /**
     * Obtiene las misiones filtradas por categoría.
     *
     * <p>Categorías válidas:</p>
     * <ul>
     *   <li>STUDY - Estudio</li>
     *   <li>WORK - Trabajo</li>
     *   <li>SOCIAL - Social</li>
     *   <li>ROMANCE - Romance</li>
     *   <li>EXPLORATION - Exploración</li>
     *   <li>TRAINING - Entrenamiento</li>
     *   <li>PERSONAL - Personal</li>
     *   <li>SPECIAL - Especial</li>
     * </ul>
     *
     * @param category Categoría de misión a filtrar
     * @return ResponseEntity con la lista de misiones de la categoría y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/category/WORK
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MissionResponseDto>> getMissionsByCategory(@PathVariable(name = "category") String category) {
        log.debug("🏷️ Fetching missions of category: {}", category);
        List<MissionResponseDto> missions = missionService.getMissionsByCategory(category);
        return ResponseEntity.ok(missions);
    }

    /**
     * Obtiene las misiones filtradas por tipo.
     *
     * <p>Tipos válidos:</p>
     * <ul>
     *   <li>ONE_TIME - Misión única</li>
     *   <li>DAILY - Misión diaria</li>
     *   <li>WEEKLY - Misión semanal</li>
     *   <li>MONTHLY - Misión mensual</li>
     *   <li>REPEATABLE - Misión repetible</li>
     *   <li>STORY - Misión de historia principal</li>
     * </ul>
     *
     * @param type Tipo de misión a filtrar
     * @return ResponseEntity con la lista de misiones del tipo y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/type/DAILY
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MissionResponseDto>> getMissionsByType(@PathVariable(name = "type") String type) {
        log.debug("🏷️ Fetching missions of type: {}", type);
        List<MissionResponseDto> missions = missionService.getMissionsByType(type);
        return ResponseEntity.ok(missions);
    }

    /**
     * Elimina una misión del sistema por su ID.
     *
     * <p><b>⚠️ Precaución:</b> Esta operación es irreversible.
     * También se eliminarán todos los registros de progreso de personajes asociados.</p>
     *
     * @param id ID de la misión a eliminar
     * @return ResponseEntity con estado HTTP 204 (NO CONTENT)
     * @throws ResourceNotFoundException Si la misión con el ID especificado no existe
     *
     * @example
     * DELETE /api/missions/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable(name = "id") Long id) {
        log.info("🗑️ Deleting mission with ID: {}", id);
        missionService.deleteMission(id);
        log.info("✅ Mission deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    // ==================================================================
    //                 GESTIÓN DE CADENAS DE MISIONES
    // ==================================================================

    /**
     * Crea una nueva cadena de misiones.
     *
     * <p>Una cadena de misiones agrupa misiones relacionadas que deben completarse
     * en un orden específico para progresar en una historia o arco argumental.</p>
     *
     * @param chainDto Datos de la cadena de misiones a crear
     * @return ResponseEntity con la cadena creada y estado HTTP 201 (CREATED)
     * @throws BusinessValidationException Si el código de cadena ya existe
     *
     * @example
     * POST /api/missions/chains
     * {
     *   "code": "CHAIN_ENTREPRENEUR",
     *   "title": "Camino del Emprendedor",
     *   "description": "Construye tu imperio desde cero",
     *   "category": "WORK"
     * }
     */
    @PostMapping("/chains")
    public ResponseEntity<MissionChainResponseDto> createMissionChain(
            @Valid @RequestBody MissionChainResponseDto chainDto) {
        log.info("🔗 Creating new mission chain with code: {}", chainDto.getCode());
        MissionChainResponseDto createdChain = missionService.createMissionChain(chainDto);
        log.info("✅ Mission chain created successfully with ID: {}", createdChain.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChain);
    }

    /**
     * Actualiza completamente una cadena de misiones existente.
     *
     * @param id       ID de la cadena a actualizar
     * @param chainDto Nuevos datos de la cadena
     * @return ResponseEntity con la cadena actualizada y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si la cadena con el ID especificado no existe
     *
     * @example
     * PUT /api/missions/chains/1
     * {
     *   "code": "CHAIN_ENTREPRENEUR",
     *   "title": "Camino del Emprendedor (Actualizado)",
     *   ...
     * }
     */
    @PutMapping("/chains/{id}")
    public ResponseEntity<MissionChainResponseDto> updateMissionChain(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody MissionChainResponseDto chainDto) {
        log.info("🔄 Updating mission chain with ID: {}", id);
        MissionChainResponseDto updatedChain = missionService.updateMissionChain(id, chainDto);
        log.info("✅ Mission chain updated successfully with ID: {}", id);
        return ResponseEntity.ok(updatedChain);
    }

    /**
     * Obtiene una cadena de misiones por su ID.
     *
     * <p>Incluye la lista completa de misiones que pertenecen a la cadena,
     * ordenadas por {@code orderInChain}.</p>
     *
     * @param id ID de la cadena de misiones
     * @return ResponseEntity con la cadena encontrada y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si la cadena con el ID especificado no existe
     *
     * @example
     * GET /api/missions/chains/1
     */
    @GetMapping("/chains/{id}")
    public ResponseEntity<MissionChainResponseDto> getMissionChainById(@PathVariable(name = "id") Long id) {
        log.debug("🔍 Fetching mission chain with ID: {}", id);
        MissionChainResponseDto chain = missionService.getMissionChainById(id);
        return ResponseEntity.ok(chain);
    }

    /**
     * Obtiene una cadena de misiones por su código único.
     *
     * @param code Código único de la cadena (ej: "CHAIN_ENTREPRENEUR")
     * @return ResponseEntity con la cadena encontrada y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si la cadena con el código especificado no existe
     *
     * @example
     * GET /api/missions/chains/code/CHAIN_ENTREPRENEUR
     */
    @GetMapping("/chains/code/{code}")
    public ResponseEntity<MissionChainResponseDto> getMissionChainByCode(@PathVariable(name = "code") String code) {
        log.debug("🔍 Fetching mission chain with code: {}", code);
        MissionChainResponseDto chain = missionService.getMissionChainByCode(code);
        return ResponseEntity.ok(chain);
    }

    /**
     * Obtiene todas las cadenas de misiones del sistema.
     *
     * @return ResponseEntity con la lista de cadenas y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/chains
     */
    @GetMapping("/chains")
    public ResponseEntity<List<MissionChainResponseDto>> getAllMissionChains() {
        log.debug("🔗 Fetching all mission chains");
        List<MissionChainResponseDto> chains = missionService.getAllMissionChains();
        return ResponseEntity.ok(chains);
    }

    /**
     * Elimina una cadena de misiones por su ID.
     *
     * <p><b>⚠️ Precaución:</b> Esta operación es irreversible.
     * Las misiones asociadas a la cadena NO se eliminan automáticamente.</p>
     *
     * @param id ID de la cadena a eliminar
     * @return ResponseEntity con estado HTTP 204 (NO CONTENT)
     * @throws ResourceNotFoundException Si la cadena con el ID especificado no existe
     *
     * @example
     * DELETE /api/missions/chains/1
     */
    @DeleteMapping("/chains/{id}")
    public ResponseEntity<Void> deleteMissionChain(@PathVariable(name = "id") Long id) {
        log.info("🗑️ Deleting mission chain with ID: {}", id);
        missionService.deleteMissionChain(id);
        log.info("✅ Mission chain deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    // ==================================================================
    //               GESTIÓN DE MISIONES PARA PERSONAJES
    // ==================================================================

    /**
     * Obtiene las misiones disponibles para un personaje específico.
     *
     * <p>Una misión está disponible si:</p>
     * <ul>
     *   <li>No está oculta ({@code hidden = false})</li>
     *   <li>El personaje cumple la edad mínima requerida</li>
     *   <li>El personaje tiene suficiente XP en trabajos (si aplica)</li>
     *   <li>El personaje tiene suficiente XP académico (si aplica)</li>
     *   <li>El personaje cumple los requisitos adicionales</li>
     *   <li>La misión no está ya activa para el personaje</li>
     * </ul>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de misiones disponibles y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el personaje no existe
     *
     * @example
     * GET /api/missions/characters/100/available
     */
    @GetMapping("/characters/{characterId}/available")
    public ResponseEntity<List<MissionResponseDto>> getAvailableMissionsForCharacter(
            @PathVariable(name = "characterId") Long characterId) {
        log.debug("📋 Fetching available missions for character {}", characterId);
        List<MissionResponseDto> missions = missionService.getAvailableMissionsForCharacter(characterId);
        log.debug("📋 Found {} available missions for character {}", missions.size(), characterId);
        return ResponseEntity.ok(missions);
    }

    /**
     * Permite a un personaje aceptar una misión.
     *
     * <p>Al aceptar una misión:</p>
     * <ul>
     *   <li>Se crea un registro de progreso ({@code CharacterMissionRecord})</li>
     *   <li>El estado inicial es {@code ACTIVE}</li>
     *   <li>Se inicializa el progreso de los objetivos</li>
     * </ul>
     *
     * <p>Validaciones:</p>
     * <ul>
     *   <li>El personaje cumple los requisitos de la misión</li>
     *   <li>El personaje no ha alcanzado el límite de misiones activas (máx. 10)</li>
     *   <li>La misión no está ya en progreso para el personaje</li>
     * </ul>
     *
     * @param request DTO con characterId y missionId
     * @return ResponseEntity con el registro de misión creado y estado HTTP 201 (CREATED)
     * @throws ResourceNotFoundException   Si el personaje o misión no existen
     * @throws BusinessValidationException Si no se cumplen las condiciones de aceptación
     *
     * @example
     * POST /api/missions/accept
     * {
     *   "characterId": 100,
     *   "missionId": 1
     * }
     */
    @PostMapping("/accept")
    public ResponseEntity<CharacterMissionRecordResponseDto> acceptMission(
            @Valid @RequestBody AcceptMissionRequestDto request) {
        log.info("✅ Character {} accepting mission {}", request.getCharacterId(), request.getMissionId());
        CharacterMissionRecordResponseDto record = missionService.acceptMission(request);
        log.info("✅ Mission {} accepted successfully by character {}", request.getMissionId(), request.getCharacterId());
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    /**
     * Actualiza el progreso de un objetivo específico de una misión activa.
     *
     * <p>Si al actualizar el progreso se completan todos los objetivos obligatorios,
     * la misión se marca automáticamente como {@code COMPLETED}.</p>
     *
     * @param request DTO con characterId, missionId, objectiveId y valor de progreso
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el registro de misión activa no existe
     *
     * @example
     * PATCH /api/missions/progress
     * {
     *   "characterId": 100,
     *   "missionId": 1,
     *   "objectiveId": 5,
     *   "value": 3
     * }
     */
    @PatchMapping("/progress")
    public ResponseEntity<CharacterMissionRecordResponseDto> updateObjectiveProgress(
            @Valid @RequestBody UpdateObjectiveProgressRequestDto request) {
        log.info("📊 Updating objective progress - Character: {}, Mission: {}, Objective: {}, Value: {}",
                request.getCharacterId(), request.getMissionId(), request.getObjectiveId(), request.getValue());
        CharacterMissionRecordResponseDto record = missionService.updateObjectiveProgress(request);
        return ResponseEntity.ok(record);
    }

    /**
     * Marca una misión como completada manualmente.
     *
     * <p>Verifica que todos los objetivos obligatorios estén completados antes de marcar como completada.</p>
     *
     * @param recordId ID del registro de misión del personaje
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException   Si el registro no existe
     * @throws BusinessValidationException Si la misión no está activa o no todos los objetivos están completados
     *
     * @example
     * POST /api/missions/records/1/complete
     */
    @PostMapping("/records/{recordId}/complete")
    public ResponseEntity<CharacterMissionRecordResponseDto> completeMission(@PathVariable(name = "recordId") Long recordId) {
        log.info("🎉 Completing mission record {}", recordId);
        CharacterMissionRecordResponseDto record = missionService.completeMission(recordId);
        log.info("✅ Mission record {} completed successfully", recordId);
        return ResponseEntity.ok(record);
    }

    /**
     * Marca una misión como fallida.
     *
     * <p>Una misión puede fallar por:</p>
     * <ul>
     *   <li>Expiración del tiempo límite</li>
     *   <li>Fallo manual del jugador</li>
     *   <li>Condiciones externas</li>
     * </ul>
     *
     * @param recordId ID del registro de misión del personaje
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el registro no existe
     *
     * @example
     * POST /api/missions/records/1/fail
     */
    @PostMapping("/records/{recordId}/fail")
    public ResponseEntity<CharacterMissionRecordResponseDto> failMission(@PathVariable(name = "recordId") Long recordId) {
        log.info("❌ Failing mission record {}", recordId);
        CharacterMissionRecordResponseDto record = missionService.failMission(recordId);
        log.info("✅ Mission record {} marked as failed", recordId);
        return ResponseEntity.ok(record);
    }

    /**
     * Abandona una misión activa.
     *
     * <p>A diferencia de fallar, abandonar es una decisión voluntaria del jugador.
     * La misión puede volver a estar disponible más tarde si es repetible.</p>
     *
     * @param recordId ID del registro de misión del personaje
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el registro no existe
     *
     * @example
     * POST /api/missions/records/1/abandon
     */
    @PostMapping("/records/{recordId}/abandon")
    public ResponseEntity<CharacterMissionRecordResponseDto> abandonMission(@PathVariable(name = "recordId") Long recordId) {
        log.info("🚫 Abandoning mission record {}", recordId);
        CharacterMissionRecordResponseDto record = missionService.abandonMission(recordId);
        log.info("✅ Mission record {} abandoned", recordId);
        return ResponseEntity.ok(record);
    }

    /**
     * Reclama las recompensas de una misión completada.
     *
     * <p>Requisitos para reclamar:</p>
     * <ul>
     *   <li>La misión está en estado {@code COMPLETED}</li>
     *   <li>Las recompensas no han sido reclamadas previamente</li>
     * </ul>
     *
     * <p>Tipos de recompensa que pueden otorgarse:</p>
     * <ul>
     *   <li>XP_JOBS - Experiencia en trabajos</li>
     *   <li>XP_ACADEMY - Experiencia académica</li>
     *   <li>STAT_BOOST - Aumento de estadísticas</li>
     *   <li>SKILL_BOOST - Aumento de habilidades</li>
     *   <li>MONEY - Dinero</li>
     *   <li>ITEM - Objetos</li>
     *   <li>TITLE - Títulos</li>
     *   <li>BADGE - Insignias/logros</li>
     *   <li>UNLOCK_MISSION - Desbloqueo de nuevas misiones</li>
     * </ul>
     *
     * @param recordId ID del registro de misión del personaje
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException   Si el registro no existe
     * @throws BusinessValidationException Si las recompensas ya fueron reclamadas o la misión no está completada
     *
     * @example
     * POST /api/missions/records/1/claim-rewards
     */
    @PostMapping("/records/{recordId}/claim-rewards")
    public ResponseEntity<CharacterMissionRecordResponseDto> claimMissionRewards(@PathVariable(name = "recordId") Long recordId) {
        log.info("🎁 Claiming rewards for mission record {}", recordId);
        CharacterMissionRecordResponseDto record = missionService.claimMissionRewards(recordId);
        log.info("✅ Rewards claimed successfully for record {}", recordId);
        return ResponseEntity.ok(record);
    }

    // ==================================================================
    //               CONSULTAS DE MISIONES DE PERSONAJE
    // ==================================================================

    /**
     * Obtiene las misiones actualmente activas de un personaje.
     *
     * <p>Una misión activa es aquella que el personaje ha aceptado
     * y aún no ha completado, fallado o abandonado.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de misiones activas y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/characters/100/active
     */
    @GetMapping("/characters/{characterId}/active")
    public ResponseEntity<List<CharacterMissionRecordResponseDto>> getCharacterActiveMissions(
            @PathVariable(name = "characterId") Long characterId) {
        log.debug("🎯 Fetching active missions for character {}", characterId);
        List<CharacterMissionRecordResponseDto> missions = missionService.getCharacterActiveMissions(characterId);
        log.debug("🎯 Found {} active missions for character {}", missions.size(), characterId);
        return ResponseEntity.ok(missions);
    }

    /**
     * Obtiene las misiones completadas por un personaje.
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de misiones completadas y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/characters/100/completed
     */
    @GetMapping("/characters/{characterId}/completed")
    public ResponseEntity<List<CharacterMissionRecordResponseDto>> getCharacterCompletedMissions(
            @PathVariable(name = "characterId") Long characterId) {
        log.debug("🏆 Fetching completed missions for character {}", characterId);
        List<CharacterMissionRecordResponseDto> missions = missionService.getCharacterCompletedMissions(characterId);
        log.debug("🏆 Found {} completed missions for character {}", missions.size(), characterId);
        return ResponseEntity.ok(missions);
    }

    /**
     * Obtiene el historial completo de misiones de un personaje con paginación.
     *
     * <p>Incluye misiones en todos los estados: activas, completadas, fallidas y abandonadas.</p>
     *
     * @param characterId ID del personaje
     * @param pageable    Configuración de paginación. Por defecto: 20 elementos, ordenado por updatedAt descendente
     * @return ResponseEntity con la página de historial y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/characters/100/history?page=0&size=10
     */
    @GetMapping("/characters/{characterId}/history")
    public ResponseEntity<Page<CharacterMissionRecordResponseDto>> getCharacterMissionHistory(
            @PathVariable(name = "characterId") Long characterId,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("📜 Fetching mission history for character {} - page: {}", characterId, pageable.getPageNumber());
        Page<CharacterMissionRecordResponseDto> history = missionService.getCharacterMissionHistory(characterId, pageable);
        return ResponseEntity.ok(history);
    }

    /**
     * Obtiene el registro de progreso de un personaje en una misión específica.
     *
     * <p>Útil para consultar el estado detallado de una misión,
     * incluyendo el progreso de cada objetivo.</p>
     *
     * @param characterId ID del personaje
     * @param missionId   ID de la misión
     * @return ResponseEntity con el registro encontrado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el registro no existe
     *
     * @example
     * GET /api/missions/characters/100/missions/1/record
     */
    @GetMapping("/characters/{characterId}/missions/{missionId}/record")
    public ResponseEntity<CharacterMissionRecordResponseDto> getCharacterMissionRecord(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "missionId") Long missionId) {
        log.debug("🔍 Fetching mission record for character {} and mission {}", characterId, missionId);
        CharacterMissionRecordResponseDto record = missionService.getCharacterMissionRecord(characterId, missionId);
        return ResponseEntity.ok(record);
    }

    // ==================================================================
    //                   PROCESAMIENTO AUTOMÁTICO
    // ==================================================================

    /**
     * Verifica y acepta automáticamente las misiones configuradas como auto-accept
     * para las cuales el personaje cumple los requisitos.
     *
     * <p>Este endpoint fuerza la ejecución del proceso automático
     * sin esperar al scheduler programado.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el personaje no existe
     *
     * @example
     * POST /api/missions/characters/100/check-auto-accept
     */
    @PostMapping("/characters/{characterId}/check-auto-accept")
    public ResponseEntity<Void> checkAndAutoAcceptMissions(@PathVariable(name = "characterId") Long characterId) {
        log.info("🤖 Checking auto-accept missions for character {}", characterId);
        missionService.checkAndAutoAcceptMissions(characterId);
        return ResponseEntity.ok().build();
    }

    /**
     * Verifica y marca como fallidas las misiones activas que han excedido su fecha de expiración.
     *
     * <p>Este endpoint fuerza la verificación de expiración sin esperar al scheduler.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el personaje no existe
     *
     * @example
     * POST /api/missions/characters/100/check-expired
     */
    @PostMapping("/characters/{characterId}/check-expired")
    public ResponseEntity<Void> checkExpiredMissions(@PathVariable(name = "characterId") Long characterId) {
        log.info("⏰ Checking expired missions for character {}", characterId);
        missionService.checkExpiredMissions(characterId);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene las misiones que se desbloquean al completar una misión específica.
     *
     * <p>Útil para mostrar al jugador qué nuevas misiones estarán disponibles
     * después de completar la misión actual.</p>
     *
     * @param missionCode Código de la misión completada
     * @return ResponseEntity con la lista de misiones desbloqueadas y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/missions/unlocked-by/MISSION_WORK_001
     */
    @GetMapping("/unlocked-by/{missionCode}")
    public ResponseEntity<List<MissionResponseDto>> getMissionsUnlockedByCompletion(
            @PathVariable(name = "missionCode") String missionCode) {
        log.debug("🔓 Fetching missions unlocked by completion of {}", missionCode);
        List<MissionResponseDto> missions = missionService.getMissionsUnlockedByCompletion(missionCode);
        log.debug("🔓 Found {} missions unlocked by {}", missions.size(), missionCode);
        return ResponseEntity.ok(missions);
    }
}