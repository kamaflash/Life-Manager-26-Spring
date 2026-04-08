package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.shareddto.dto.ScholarshipApplicationDto;
import com.pet.businessdomain.shareddto.dto.ScholarshipDto;
import com.pet.businessdomain.formationservice.services.ScholarshipApplicationService;
import com.pet.businessdomain.formationservice.services.ScholarshipService;
import com.pet.businessdomain.shareddto.enumentities.EnumFormation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;
    @Autowired
    private ScholarshipApplicationService scholarshipApplicationService;

    /**
     * Crea una nueva beca en el sistema
     *
     * @param dto Objeto con los datos de la beca a crear
     * @return La beca creada con su ID asignado
     *
     * Ejemplo: POST /api/scholarships
     * Body: { "title": "Beca Excelencia", "amount": 5000, ... }
     */
    @PostMapping
    public ResponseEntity<ScholarshipDto> create(@RequestBody ScholarshipDto dto) {
        return ResponseEntity.ok(scholarshipService.create(dto));
    }

    /**
     * Crea múltiples becas en lote (para inicialización masiva)
     *
     * @param dtos Lista de objetos ScholarshipDto a crear
     * @return Lista de becas creadas con sus IDs asignados
     *
     * Ejemplo: POST /api/scholarships/batch
     * Body: [{...}, {...}, {...}]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ScholarshipDto>> createBatch(@RequestBody List<ScholarshipDto> dtos) {
        List<ScholarshipDto> created = dtos.stream()
                .map(scholarshipService::create)
                .toList();
        return ResponseEntity.ok(created);
    }

    /**
     * Actualiza los datos de una beca existente
     *
     * @param id ID de la beca a actualizar
     * @param dto Objeto con los nuevos datos de la beca
     * @return La beca actualizada
     *
     * Ejemplo: PUT /api/scholarships/1
     * Body: { "title": "Nuevo título", "amount": 6000, ... }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScholarshipDto> update(@PathVariable(name = "id") Long id,
                                                 @RequestBody ScholarshipDto dto) {
        return ResponseEntity.ok(scholarshipService.update(id, dto));
    }

    /**
     * Obtiene los detalles de una beca específica por su ID
     *
     * @param id ID de la beca a consultar
     * @return Datos completos de la beca solicitada
     *
     * Ejemplo: GET /api/scholarships/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScholarshipDto> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(scholarshipService.getById(id));
    }
    @GetMapping("/dto/{id}")
    public ScholarshipDto getByIdDto(@PathVariable(name = "id") Long id) {
        return scholarshipService.getById(id);
    }

    /**
     * Lista todas las becas activas disponibles en el sistema
     *
     * @return Lista de becas activas
     *
     * Ejemplo: GET /api/scholarships
     */
    @GetMapping
    public ResponseEntity<List<ScholarshipDto>> getAllActive() {
        return ResponseEntity.ok(scholarshipService.getAllActive());
    }

    /**
     * Obtiene becas recomendadas para un personaje específico
     * (Actualmente retorna todas las becas activas)
     *
     * @param id ID del personaje
     * @return Lista de becas recomendadas
     *
     * Ejemplo: GET /api/scholarships/id/1
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<List<ScholarshipDto>> getForCharacterId(@PathVariable(name = "id") Long id) {
        // TODO: Implementar lógica de recomendación basada en el personaje
        return ResponseEntity.ok(scholarshipService.getAllActive());
    }

    /**
     * Elimina una beca del sistema (borrado lógico)
     *
     * @param id ID de la beca a eliminar
     * @return Respuesta sin contenido si la operación es exitosa
     *
     * Ejemplo: DELETE /api/scholarships/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        scholarshipService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Un personaje solicita una beca específica
     * Verifica que el personaje cumpla los requisitos y no haya solicitado antes
     *
     * @param scholarshipId ID de la beca que se solicita
     * @param characterId ID del personaje que solicita
     * @return Objeto con los detalles de la solicitud creada
     *
     * Ejemplo: POST /api/scholarships/apply?scholarshipId=1&characterId=100
     */
    @PostMapping("/apply")
    public ResponseEntity<ScholarshipApplicationDto> apply(
            @RequestParam(name = "scholarshipId") Long scholarshipId,
            @RequestParam(name = "characterId") Long characterId) {

        ScholarshipApplicationDto application =
                scholarshipApplicationService.applyToScholarship(scholarshipId, characterId);

        return ResponseEntity.ok(application);
    }

    /**
     * Lista todas las solicitudes de becas realizadas por un personaje
     * Útil para mostrar el historial de solicitudes del jugador
     *
     * @param characterId ID del personaje
     * @return Lista de solicitudes de becas del personaje
     *
     * Ejemplo: GET /api/scholarships/by-character/100
     */
    @GetMapping("/by-character/{characterId}")
    public ResponseEntity<List<ScholarshipApplicationDto>> getByCharacter(
            @PathVariable(name = "characterId") Long characterId) {

        List<ScholarshipApplicationDto> applications =
                scholarshipApplicationService.getApplicationsByCharacter(characterId);

        return ResponseEntity.ok(applications);
    }
    @GetMapping("/by-character/list/{characterId}")
    public List<ScholarshipApplicationDto> getByCharacterList(
            @PathVariable(name = "characterId") Long characterId) {

        List<ScholarshipApplicationDto> applications =
                scholarshipApplicationService.getApplicationsByCharacter(characterId);

        return applications;
    }

    /**
     * Lista todas las solicitudes recibidas para una beca específica
     * Útil para administradores que quieran ver quiénes solicitaron una beca
     *
     * @param scholarshipId ID de la beca
     * @return Lista de solicitudes para esa beca
     *
     * Ejemplo: GET /api/scholarships/by-scholarship/1
     */
    @GetMapping("/by-scholarship/{scholarshipId}")
    public ResponseEntity<List<ScholarshipApplicationDto>> getByScholarship(
            @PathVariable(name = "scholarshipId") Long scholarshipId) {

        List<ScholarshipApplicationDto> applications =
                scholarshipApplicationService.getApplicationsByScholarship(scholarshipId);

        return ResponseEntity.ok(applications);
    }

    /**
     * Actualiza el estado de una solicitud de beca (pendiente → aprobada/rechazada)
     * Solo accesible por administradores
     *
     * @param applicationId ID de la solicitud a actualizar
     * @param status Nuevo estado: "APPROVED", "REJECTED", "PENDING"
     * @return Solicitud actualizada
     *
     * Ejemplo: PUT /api/scholarships/1/status?status=APPROVED
     */
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ScholarshipApplicationDto> updateStatus(
            @PathVariable(name = "applicationId") Long applicationId,
            @RequestParam(name = "status") EnumFormation.ApplicationStatus status) {

        ScholarshipApplicationDto updated =
                scholarshipApplicationService.updateStatus(applicationId, status);

        return ResponseEntity.ok(updated);
    }
    @PutMapping("/dto/{applicationId}/statuss")
    public ScholarshipApplicationDto updateStatuss(
            @PathVariable(name = "applicationId") Long applicationId,
            @RequestParam(name = "statuss") EnumFormation.ApplicationStatus statuss) {

        ScholarshipApplicationDto updated =
                scholarshipApplicationService.updateStatus(applicationId, statuss);

        return updated;
    }

    /**
     * Verifica si un personaje ya ha solicitado una beca específica
     * Útil en el frontend para habilitar/deshabilitar el botón de solicitud
     *
     * @param scholarshipId ID de la beca
     * @param characterId ID del personaje
     * @return Map con la clave "applied" y valor true/false
     *
     * Ejemplo: GET /api/scholarships/check-application?scholarshipId=1&characterId=100
     * Respuesta: { "applied": true }
     */
    @GetMapping("/check-application")
    public ResponseEntity<Map<String, Boolean>> checkApplication(
            @RequestParam(name = "scholarshipId") Long scholarshipId,
            @RequestParam(name = "characterId") Long characterId) {

        boolean applied = scholarshipApplicationService.hasApplied(scholarshipId, characterId);
        return ResponseEntity.ok(Map.of("applied", applied));
    }
}