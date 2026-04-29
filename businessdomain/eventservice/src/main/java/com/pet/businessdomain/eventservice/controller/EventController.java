package com.pet.businessdomain.eventservice.controller;

import com.pet.businessdomain.eventservice.exceptions.BusinessValidationException;
import com.pet.businessdomain.eventservice.exceptions.ResourceNotFoundException;
import com.pet.businessdomain.eventservice.repository.EventRepository;
import com.pet.businessdomain.eventservice.services.EventService;
import com.pet.businessdomain.shareddto.dto.CharacterEventRecordResponseDto;
import com.pet.businessdomain.shareddto.dto.EventResponseDto;
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
 * Controlador REST para la gestión de eventos del sistema.
 *
 * <p>Este controlador proporciona endpoints para:</p>
 * <ul>
 *   <li>Gestión CRUD de eventos (crear, leer, actualizar, eliminar)</li>
 *   <li>Participación de personajes en eventos</li>
 *   <li>Consulta de eventos automáticos y disponibles para personajes</li>
 * </ul>
 *
 * <p>URL base: {@code /api/events}</p>
 *
 * @author PET Business Domain
 * @version 1.0
 * @since 2024
 */
@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;  // ⚠️ Solo para operaciones administrativas

    // ==================================================================
    //                    GESTIÓN DE EVENTOS (CRUD)
    // ==================================================================

    /**
     * Crea un nuevo evento en el sistema.
     *
     * <p>Valida los datos del evento antes de persistirlo en base de datos.</p>
     *
     * @param eventDto Datos del evento a crear (debe ser válido según las anotaciones de validación)
     * @return ResponseEntity con el evento creado y estado HTTP 201 (CREATED)
     * @throws BusinessValidationException Si el código del evento ya existe o los datos son inválidos
     *
     * @example
     * POST /api/events
     * {
     *   "code": "EVENT_BONUS_001",
     *   "title": "Bono de Productividad",
     *   "type": "BONUS",
     *   "scope": "PERSONAL",
     *   "startDate": "2024-06-01T09:00:00",
     *   "endDate": "2024-06-07T18:00:00"
     * }
     */
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventResponseDto eventDto) {
        log.info("📝 Creating new event with code: {}", eventDto.getCode());
        EventResponseDto createdEvent = eventService.createEvent(eventDto);
        log.info("✅ Event created successfully with ID: {}", createdEvent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PostMapping("/character/{characterId}")
    public ResponseEntity<EventResponseDto> createEventCharacter(@Valid @RequestBody EventResponseDto eventDto,
                                                                 @PathVariable(name = "characterId") Long characterId) {
        log.info("📝 Creating personal event for character {} with code: {}", characterId, eventDto.getCode());
        EventResponseDto createdEvent = eventService.createEventForCharacter(characterId, eventDto);
        log.info("✅ Personal event created successfully with ID: {}", createdEvent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    /**
     * Crea múltiples eventos en una sola petición (operación batch).
     *
     * <p>Útil para cargas iniciales de datos o importaciones masivas.
     * Los eventos que ya existan (mismo código) serán omitidos.</p>
     *
     * @param dtos Lista de eventos a crear
     * @return ResponseEntity con la lista de eventos creados exitosamente y estado HTTP 201 (CREATED)
     *
     * @example
     * POST /api/events/batch
     * [
     *   { "code": "EVENT_001", ... },
     *   { "code": "EVENT_002", ... }
     * ]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<EventResponseDto>> createEventsBatch(@RequestBody List<EventResponseDto> dtos) {
        log.info("📦 Processing batch creation of {} events", dtos.size());
        List<EventResponseDto> created = eventService.createEventBatch(dtos);
        log.info("✅ Successfully created {} out of {} events", created.size(), dtos.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualiza completamente un evento existente.
     *
     * <p>Reemplaza todos los campos del evento con los nuevos valores proporcionados.</p>
     *
     * @param id       ID del evento a actualizar
     * @param eventDto Nuevos datos del evento (debe ser válido)
     * @return ResponseEntity con el evento actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el evento con el ID especificado no existe
     *
     * @example
     * PUT /api/events/1
     * {
     *   "code": "EVENT_BONUS_001",
     *   "title": "Bono de Productividad Actualizado",
     *   ...
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody EventResponseDto eventDto) {
        log.info("🔄 Updating event with ID: {}", id);
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventDto);
        log.info("✅ Event updated successfully with ID: {}", id);
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Obtiene un evento por su identificador único (ID).
     *
     * @param id ID del evento a consultar
     * @return ResponseEntity con el evento encontrado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el evento con el ID especificado no existe
     *
     * @example
     * GET /api/events/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable(name = "id") Long id) {
        log.debug("🔍 Fetching event with ID: {}", id);
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    /**
     * Obtiene un evento por su código único.
     *
     * <p>El código es un identificador legible como "EVENT_BONUS_001".</p>
     *
     * @param code Código único del evento
     * @return ResponseEntity con el evento encontrado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el evento con el código especificado no existe
     *
     * @example
     * GET /api/events/code/EVENT_BONUS_001
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<EventResponseDto> getEventByCode(@PathVariable(name = "code") String code) {
        log.debug("🔍 Fetching event with code: {}", code);
        EventResponseDto event = eventService.getEventByCode(code);
        return ResponseEntity.ok(event);
    }

    /**
     * Obtiene todos los eventos del sistema (sin paginación).
     *
     * <p><b>⚠️ Precaución:</b> Este endpoint puede devolver una gran cantidad de datos.
     * Para entornos de producción, se recomienda usar {@link #getAllEventsPaginated(Pageable)}.</p>
     *
     * @return ResponseEntity con la lista completa de eventos y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/events
     */
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        log.debug("📋 Fetching all events");
        List<EventResponseDto> events = eventService.getAllEvents();
        log.debug("📋 Found {} events", events.size());
        return ResponseEntity.ok(events);
    }

    /**
     * Obtiene todos los eventos con paginación.
     *
     * <p>Soporta ordenación por cualquier campo del evento.</p>
     *
     * @param pageable Configuración de paginación y ordenación.
     *                 Por defecto: página 0, 20 elementos, ordenado por startDate descendente.
     * @return ResponseEntity con la página de eventos y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/events/paginated?page=0&size=10&sort=startDate,desc
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<EventResponseDto>> getAllEventsPaginated(
            @PageableDefault(size = 20, sort = "startDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("📄 Fetching paginated events - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<EventResponseDto> events = eventService.getAllEventsPaginated(pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * Obtiene los eventos que están actualmente activos.
     *
     * <p>Un evento está activo cuando la fecha actual se encuentra entre
     * su fecha de inicio ({@code startDate}) y su fecha de fin ({@code endDate}).</p>
     *
     * @return ResponseEntity con la lista de eventos activos y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/events/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<EventResponseDto>> getActiveEvents() {
        log.debug("🎯 Fetching active events");
        List<EventResponseDto> events = eventService.getActiveEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * Obtiene los eventos filtrados por ciudad.
     *
     * <p>Solo aplica a eventos con scope {@code CITY}.</p>
     *
     * @param city Nombre de la ciudad para filtrar eventos
     * @return ResponseEntity con la lista de eventos de la ciudad y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/events/city/Madrid
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<EventResponseDto>> getEventsByCity(@PathVariable(name = "city") String city) {
        log.debug("🏙️ Fetching events for city: {}", city);
        List<EventResponseDto> events = eventService.getEventsByCity(city);
        return ResponseEntity.ok(events);
    }

    /**
     * Obtiene los eventos filtrados por tipo.
     *
     * <p>Tipos válidos según {@link com.pet.businessdomain.shareddto.enumentities.EnumAll.EventType}:</p>
     * <ul>
     *   <li>BONUS - Eventos de bonificación</li>
     *   <li>PROMOTION - Ascensos laborales</li>
     *   <li>DEMOTION - Descensos</li>
     *   <li>CONFLICT - Conflictos</li>
     *   <li>PROJECT_SUCCESS - Éxito de proyecto</li>
     *   <li>PROJECT_FAILURE - Fracaso de proyecto</li>
     *   <li>BURNOUT - Agotamiento</li>
     *   <li>OFFER_FROM_RIVAL - Oferta de la competencia</li>
     *   <li>MENTOR_LEAVES - El mentor se va</li>
     *   <li>TEAM_RESTRUCTURE - Reestructuración de equipo</li>
     *   <li>QUARTERLY_REVIEW - Revisión trimestral</li>
     * </ul>
     *
     * @param type Tipo de evento a filtrar
     * @return ResponseEntity con la lista de eventos del tipo y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/events/type/BONUS
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<EventResponseDto>> getEventsByType(@PathVariable(name = "type") String type) {
        log.debug("🏷️ Fetching events of type: {}", type);
        List<EventResponseDto> events = eventService.getEventsByType(type);
        return ResponseEntity.ok(events);
    }

    /**
     * Elimina un evento del sistema por su ID.
     *
     * <p><b>⚠️ Precaución:</b> Esta operación es irreversible.
     * Se recomienda usar {@link #cancelEvent(Long)} en su lugar para mantener el histórico.</p>
     *
     * @param id ID del evento a eliminar
     * @return ResponseEntity con estado HTTP 204 (NO CONTENT)
     * @throws ResourceNotFoundException Si el evento con el ID especificado no existe
     *
     * @example
     * DELETE /api/events/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable(name = "id") Long id) {
        log.info("🗑️ Deleting event with ID: {}", id);
        eventService.deleteEvent(id);
        log.info("✅ Event deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cancela un evento sin eliminarlo de la base de datos.
     *
     * <p>El evento permanece en el sistema con estado {@code CANCELLED}.
     * Esto permite mantener un histórico de eventos cancelados.</p>
     *
     * @param id ID del evento a cancelar
     * @return ResponseEntity con estado HTTP 204 (NO CONTENT)
     * @throws ResourceNotFoundException Si el evento con el ID especificado no existe
     *
     * @example
     * PATCH /api/events/1/cancel
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelEvent(@PathVariable(name = "id") Long id) {
        log.info("❌ Cancelling event with ID: {}", id);
        eventService.cancelEvent(id);
        log.info("✅ Event cancelled successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    // ==================================================================
    //                 PARTICIPACIÓN EN EVENTOS
    // ==================================================================

    /**
     * Registra un personaje para participar en un evento.
     *
     * <p>Verifica que:</p>
     * <ul>
     *   <li>El personaje existe</li>
     *   <li>El evento existe y está activo</li>
     *   <li>Hay plazas disponibles (si tiene límite de participantes)</li>
     *   <li>El personaje cumple los requisitos del evento</li>
     *   <li>El personaje no está ya registrado</li>
     * </ul>
     *
     * @param eventId     ID del evento al que registrarse
     * @param characterId ID del personaje que se registra
     * @return ResponseEntity con el registro creado y estado HTTP 201 (CREATED)
     * @throws ResourceNotFoundException   Si el evento o personaje no existe
     * @throws BusinessValidationException Si no se cumplen las condiciones de registro
     *
     * @example
     * POST /api/events/1/register/character/100
     */
    @PostMapping("/{eventId}/register/character/{characterId}")
    public ResponseEntity<CharacterEventRecordResponseDto> registerCharacterToEvent(
            @PathVariable(name = "eventId") Long eventId,
            @PathVariable(name = "characterId") Long characterId) {
        log.info("📋 Registering character {} to event {}", characterId, eventId);
        CharacterEventRecordResponseDto record = eventService.registerCharacterToEvent(characterId, eventId);
        log.info("✅ Character {} registered successfully to event {}", characterId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    /**
     * Actualiza el estado de participación de un personaje en un evento.
     *
     * <p>Estados válidos según {@link com.pet.businessdomain.shareddto.enumentities.EnumAll.EventParticipationStatus}:</p>
     * <ul>
     *   <li>INVITED - Invitado</li>
     *   <li>REGISTERED - Registrado</li>
     *   <li>ATTENDED - Asistió</li>
     *   <li>MISSED - No asistió</li>
     *   <li>SKIPPED - Omitido</li>
     * </ul>
     *
     * @param recordId ID del registro de participación
     * @param status   Nuevo estado de participación
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el registro no existe
     *
     * @example
     * PATCH /api/events/records/1/status?status=ATTENDED
     */
    @PatchMapping("/records/{recordId}/status")
    public ResponseEntity<CharacterEventRecordResponseDto> updateParticipationStatus(
            @PathVariable(name = "recordId") Long recordId,
            @RequestParam(name = "status") String status) {
        log.info("🔄 Updating participation status for record {} to {}", recordId, status);
        CharacterEventRecordResponseDto record = eventService.updateParticipationStatus(recordId, status);
        return ResponseEntity.ok(record);
    }

    /**
     * Marca la asistencia de un personaje a un evento y registra el resultado.
     *
     * <p>Resultados válidos según {@link com.pet.businessdomain.shareddto.enumentities.EnumAll.EventOutcome}:</p>
     * <ul>
     *   <li>SUCCESS - Éxito</li>
     *   <li>PARTIAL - Éxito parcial</li>
     *   <li>FAILURE - Fracaso</li>
     *   <li>NEUTRAL - Neutral</li>
     * </ul>
     *
     * @param recordId    ID del registro de participación
     * @param outcome     Resultado del evento (opcional)
     * @param outcomeNote Nota descriptiva del resultado (opcional)
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el registro no existe
     *
     * @example
     * POST /api/events/records/1/attend?outcome=SUCCESS&outcomeNote=Todo%20salió%20bien
     */
    @PostMapping("/records/{recordId}/attend")
    public ResponseEntity<CharacterEventRecordResponseDto> attendEvent(
            @PathVariable(name = "recordId") Long recordId,
            @RequestParam(required = false,name = "outcome") String outcome,
            @RequestParam(required = false,name = "outcomeNote") String outcomeNote) {
        log.info("✅ Marking attendance for record {} with outcome: {}", recordId, outcome);
        CharacterEventRecordResponseDto record = eventService.attendEvent(recordId, outcome, outcomeNote);
        return ResponseEntity.ok(record);
    }

    /**
     * Reclama las recompensas de un evento al que el personaje asistió.
     *
     * <p>Requisitos para reclamar:</p>
     * <ul>
     *   <li>El personaje ha asistido al evento (estado ATTENDED)</li>
     *   <li>Las recompensas no han sido reclamadas previamente</li>
     * </ul>
     *
     * @param recordId ID del registro de participación
     * @return ResponseEntity con el registro actualizado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException   Si el registro no existe
     * @throws BusinessValidationException Si las recompensas ya fueron reclamadas o no se asistió
     *
     * @example
     * POST /api/events/records/1/claim-rewards
     */
    @PostMapping("/records/{recordId}/claim-rewards")
    public ResponseEntity<CharacterEventRecordResponseDto> claimEventRewards(@PathVariable(name = "recordId") Long recordId) {
        log.info("🎁 Claiming rewards for event record {}", recordId);
        CharacterEventRecordResponseDto record = eventService.claimEventRewards(recordId);
        log.info("✅ Rewards claimed successfully for record {}", recordId);
        return ResponseEntity.ok(record);
    }

    /**
     * Obtiene todos los registros de participación en eventos de un personaje.
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de registros y estado HTTP 200 (OK)
     *
     * @example
     * GET /api/events/characters/100/records
     */
    @GetMapping("/characters/{characterId}/records")
    public ResponseEntity<List<CharacterEventRecordResponseDto>> getCharacterEventRecords(
            @PathVariable(name = "characterId") Long characterId) {
        log.debug("📋 Fetching event records for character {}", characterId);
        List<CharacterEventRecordResponseDto> records = eventService.getCharacterEventRecords(characterId);
        return ResponseEntity.ok(records);
    }

    /**
     * Obtiene el registro de participación de un personaje en un evento específico.
     *
     * @param characterId ID del personaje
     * @param eventId     ID del evento
     * @return ResponseEntity con el registro encontrado y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el registro no existe
     *
     * @example
     * GET /api/events/characters/100/events/1/record
     */
    @GetMapping("/characters/{characterId}/events/{eventId}/record")
    public ResponseEntity<CharacterEventRecordResponseDto> getCharacterEventRecord(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "eventId") Long eventId) {
        log.debug("🔍 Fetching event record for character {} and event {}", characterId, eventId);
        CharacterEventRecordResponseDto record = eventService.getCharacterEventRecord(characterId, eventId);
        return ResponseEntity.ok(record);
    }

    // ==================================================================
    //              EVENTOS AUTOMÁTICOS Y DISPONIBLES
    // ==================================================================

    /**
     * Obtiene los eventos configurados como auto-trigger que están disponibles para un personaje.
     *
     * <p>Los eventos auto-trigger son aquellos que se activan automáticamente
     * sin necesidad de que el jugador los acepte manualmente.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de eventos auto-trigger disponibles y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el personaje no existe
     *
     * @example
     * GET /api/events/characters/100/auto-trigger
     */
    @GetMapping("/characters/{characterId}/auto-trigger")
    public ResponseEntity<List<EventResponseDto>> getAutoTriggerEventsForCharacter(
            @PathVariable(name = "characterId") Long characterId) {
        log.debug("🤖 Fetching auto-trigger events for character {}", characterId);
        List<EventResponseDto> events = eventService.getAutoTriggerEventsForCharacter(characterId);
        return ResponseEntity.ok(events);
    }

    /**
     * Procesa y registra automáticamente al personaje en todos los eventos auto-trigger disponibles.
     *
     * <p>Este endpoint fuerza la ejecución del proceso automático de registro
     * sin esperar al scheduler programado.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el personaje no existe
     *
     * @example
     * POST /api/events/characters/100/process-auto-trigger
     */
    @PostMapping("/characters/{characterId}/process-auto-trigger")
    public ResponseEntity<Void> processAutoTriggerEvents(@PathVariable(name = "characterId") Long characterId) {
        log.info("🤖 Processing auto-trigger events for character {}", characterId);
        eventService.processAutoTriggerEvents(characterId);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene todos los eventos disponibles para un personaje.
     *
     * <p>Un evento está disponible si:</p>
     * <ul>
     *   <li>Está activo (fecha actual dentro del rango)</li>
     *   <li>El personaje cumple los requisitos</li>
     *   <li>El personaje no está ya registrado</li>
     *   <li>Hay plazas disponibles (si aplica)</li>
     *   <li>El scope es compatible (GLOBAL, misma ciudad, o PERSONAL)</li>
     * </ul>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de eventos disponibles y estado HTTP 200 (OK)
     * @throws ResourceNotFoundException Si el personaje no existe
     *
     * @example
     * GET /api/events/characters/100/available
     */
    @GetMapping("/characters/{characterId}/available")
    public ResponseEntity<List<EventResponseDto>> getAvailableEventsForCharacter(
            @PathVariable(name = "characterId") Long characterId) {
        log.debug("📅 Fetching available events for character {}", characterId);
        List<EventResponseDto> events = eventService.getAvailableEventsForCharacter(characterId);
        return ResponseEntity.ok(events);
    }

    // ==================================================================
    //                    OPERACIONES ADMINISTRATIVAS
    // ==================================================================

    /**
     * ⚠️ ELIMINA TODOS LOS EVENTOS DEL SISTEMA.
     *
     * <p><b>¡PELIGRO!</b> Esta operación es irreversible y elimina permanentemente
     * todos los eventos y sus registros de participación asociados.</p>
     *
     * <p><b>Uso exclusivo para:</b></p>
     * <ul>
     *   <li>Entornos de desarrollo y pruebas</li>
     *   <li>Reseteo de datos iniciales</li>
     * </ul>
     *
     * <p><b>NO USAR EN PRODUCCIÓN</b> sin las debidas precauciones.</p>
     *
     * @return ResponseEntity con mensaje de confirmación y estado HTTP 202 (ACCEPTED)
     *
     * @example
     * DELETE /api/events/all
     */
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        log.warn("⚠️⚠️⚠️ DELETING ALL EVENTS FROM DATABASE ⚠️⚠️⚠️");
        long count = eventRepository.count();
        eventRepository.deleteAll();
        log.warn("✅ Deleted {} events from database", count);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Deleted " + count + " events successfully");
    }
}