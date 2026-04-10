package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.JobVacancyService;
import com.pet.businessdomain.jobservice.services.RequirementService;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.NotificationPriority;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/job-vacancies")
@RequiredArgsConstructor
public class JobVacancyController {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private final JobVacancyService jobVacancyService;
    private final RequirementService requirementService;

    // ===== CRUD BÁSICO =====

    /**
     * Crea una nueva vacante de trabajo
     *
     * @param dto Datos de la vacante a crear
     * @return JobVacancyDTO con los datos de la vacante creada
     *
     * @example POST /api/job-vacancies
     * @example Body: { "positionId": 456, "minSalary": 30000, "maxSalary": 50000, "location": "Madrid" }
     */
    @PostMapping
    public ResponseEntity<JobVacancyDTO> create(@RequestBody JobVacancyDTO dto) {
        log.info("POST /api/job-vacancies - Create job vacancy");
        return ResponseEntity.ok(jobVacancyService.create(dto));
    }

    /**
     * Crea múltiples vacantes de trabajo en lote
     *
     * @param dtos Lista de vacantes a crear
     * @return Lista de JobVacancyDTO con las vacantes creadas
     *
     * @example POST /api/job-vacancies/batch
     * @example Body: [{ "positionId": 456 }, { "positionId": 457 }]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<JobVacancyDTO>> createBatch(@RequestBody List<JobVacancyDTO> dtos) {
        log.info("POST /api/job-vacancies/batch - Creating {} vacancies", dtos.size());

        List<JobVacancyDTO> createdVacancies = new ArrayList<>();
        for (JobVacancyDTO dto : dtos) {
            log.info("  - Creating vacancy for positionId: {}", dto.getPositionId());
            JobVacancyDTO created = jobVacancyService.create(dto);
            createdVacancies.add(created);
        }

        log.info("Successfully created {} vacancies", createdVacancies.size());
        return ResponseEntity.ok(createdVacancies);
    }

    /**
     * Crea múltiples requisitos en lote para una vacante
     *
     * @param dtos Lista de requisitos a crear
     * @return Lista de RequirementDTO con los requisitos creados
     *
     * @example POST /api/job-vacancies/requirements/batch
     * @example Body: [{ "vacancyId": 123, "type": "SKILL", "skill_key": "java", "minValue": 70 }]
     */
    @PostMapping("/requirements/batch")
    public ResponseEntity<List<RequirementDTO>> createRequirementsBatch(@RequestBody List<RequirementDTO> dtos) {
        log.info("POST /api/job-vacancies/requirements/batch - Creating {} requirements", dtos.size());

        List<RequirementDTO> createdRequirements = new ArrayList<>();
        for (RequirementDTO dto : dtos) {
            log.info("  - Creating requirement for vacancyId: {}", dto.getVacancyId());
            RequirementDTO created = requirementService.create(dto);
            createdRequirements.add(created);
        }

        log.info("Successfully created {} requirements", createdRequirements.size());
        return ResponseEntity.ok(createdRequirements);
    }

    /**
     * Obtiene todos los requisitos de una vacante
     *
     * @param vacancyId ID de la vacante
     * @return Lista de RequirementDTO con los requisitos de la vacante
     *
     * @example GET /api/job-vacancies/123/requirements
     */
    @GetMapping("/{vacancyId}/requirements")
    public ResponseEntity<List<RequirementDTO>> getRequirementsByVacancy(@PathVariable(name = "vacancyId") Long vacancyId) {
        log.info("GET /api/job-vacancies/{}/requirements - Get requirements by vacancy", vacancyId);
        return ResponseEntity.ok(requirementService.getByVacancyId(vacancyId));
    }

    /**
     * Elimina todos los requisitos de una vacante
     *
     * @param vacancyId ID de la vacante
     * @return 204 No Content si la operación es exitosa
     *
     * @example DELETE /api/job-vacancies/123/requirements
     */
    @DeleteMapping("/{vacancyId}/requirements")
    public ResponseEntity<Void> deleteRequirementsByVacancy(@PathVariable(name = "vacancyId") Long vacancyId) {
        log.info("DELETE /api/job-vacancies/{}/requirements - Delete requirements by vacancy", vacancyId);
        requirementService.deleteByVacancyId(vacancyId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza una vacante existente
     *
     * @param id ID de la vacante a actualizar
     * @param dto Nuevos datos de la vacante
     * @return JobVacancyDTO con los datos actualizados
     *
     * @example PUT /api/job-vacancies/123
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobVacancyDTO> update(
            @PathVariable(name = "id") Long id,
            @RequestBody JobVacancyDTO dto) {
        log.info("PUT /api/job-vacancies/{} - Update job vacancy", id);
        return ResponseEntity.ok(jobVacancyService.update(id, dto));
    }

    /**
     * Obtiene una vacante por su ID
     *
     * @param id ID de la vacante
     * @return JobVacancyDTO con los datos de la vacante
     *
     * @example GET /api/job-vacancies/123
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobVacancyDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-vacancies/{} - Get job vacancy", id);
        return ResponseEntity.ok(jobVacancyService.getById(id));
    }

    /**
     * Elimina una vacante (borrado físico)
     *
     * @param id ID de la vacante a eliminar
     * @return 204 No Content si la operación es exitosa
     *
     * @example DELETE /api/job-vacancies/123
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        log.info("DELETE /api/job-vacancies/{} - Delete job vacancy", id);
        jobVacancyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cierra una vacante (cierre lógico)
     *
     * @param id ID de la vacante a cerrar
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/job-vacancies/123/close
     */
    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> closeVacancy(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/job-vacancies/{}/close - Close vacancy", id);
        jobVacancyService.closeVacancy(id);
        return ResponseEntity.noContent().build();
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todas las vacantes
     *
     * @return Lista de JobVacancyDTO con todas las vacantes
     *
     * @example GET /api/job-vacancies
     */
    @GetMapping
    public ResponseEntity<List<JobVacancyDTO>> getAll() {
        log.info("GET /api/job-vacancies - Get all vacancies");
        return ResponseEntity.ok(jobVacancyService.getAll());
    }

    /**
     * Obtiene solo las vacantes activas
     *
     * @return Lista de JobVacancyDTO con vacantes activas
     *
     * @example GET /api/job-vacancies/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<JobVacancyDTO>> getAllActive() {
        log.info("GET /api/job-vacancies/active - Get all active vacancies");
        return ResponseEntity.ok(jobVacancyService.getAllActive());
    }

    /**
     * Obtiene todas las vacantes de un puesto específico
     *
     * @param positionId ID del puesto
     * @return Lista de JobVacancyDTO con las vacantes del puesto
     *
     * @example GET /api/job-vacancies/position/456
     */
    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<JobVacancyDTO>> getByPosition(@PathVariable(name = "positionId") Long positionId) {
        log.info("GET /api/job-vacancies/position/{} - Get vacancies by position", positionId);
        return ResponseEntity.ok(jobVacancyService.getByPosition(positionId));
    }

    /**
     * Obtiene solo las vacantes activas de un puesto
     *
     * @param positionId ID del puesto
     * @return Lista de JobVacancyDTO con vacantes activas del puesto
     *
     * @example GET /api/job-vacancies/position/456/active
     */
    @GetMapping("/position/{positionId}/active")
    public ResponseEntity<List<JobVacancyDTO>> getByPositionActive(@PathVariable(name = "positionId") Long positionId) {
        log.info("GET /api/job-vacancies/position/{}/active - Get active vacancies by position", positionId);
        return ResponseEntity.ok(jobVacancyService.getByPositionActive(positionId));
    }

    /**
     * Obtiene todas las vacantes de una empresa
     *
     * @param companyId ID de la empresa
     * @return Lista de JobVacancyDTO con las vacantes de la empresa
     *
     * @example GET /api/job-vacancies/company/100
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobVacancyDTO>> getByCompany(@PathVariable(name = "companyId") Long companyId) {
        log.info("GET /api/job-vacancies/company/{} - Get vacancies by company", companyId);
        return ResponseEntity.ok(jobVacancyService.getByCompany(companyId));
    }

    /**
     * Obtiene vacantes por tipo de contrato
     *
     * @param contractType Tipo de contrato (FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE)
     * @return Lista de JobVacancyDTO con vacantes de ese tipo de contrato
     *
     * @example GET /api/job-vacancies/contract-type/FULL_TIME
     */
    @GetMapping("/contract-type/{contractType}")
    public ResponseEntity<List<JobVacancyDTO>> getByContractType(@PathVariable(name = "contractType") String contractType) {
        log.info("GET /api/job-vacancies/contract-type/{} - Get vacancies by contract type", contractType);
        return ResponseEntity.ok(jobVacancyService.getByContractType(contractType));
    }

    /**
     * Obtiene vacantes por modalidad de trabajo
     *
     * @param workModality Modalidad (ONSITE, REMOTE, HYBRID)
     * @return Lista de JobVacancyDTO con vacantes de esa modalidad
     *
     * @example GET /api/job-vacancies/work-modality/REMOTE
     */
    @GetMapping("/work-modality/{workModality}")
    public ResponseEntity<List<JobVacancyDTO>> getByWorkModality(@PathVariable(name = "workModality") String workModality) {
        log.info("GET /api/job-vacancies/work-modality/{} - Get vacancies by work modality", workModality);
        return ResponseEntity.ok(jobVacancyService.getByWorkModality(workModality));
    }

    /**
     * Obtiene vacantes por rango salarial
     *
     * @param min Salario mínimo
     * @param max Salario máximo
     * @return Lista de JobVacancyDTO con vacantes en ese rango salarial
     *
     * @example GET /api/job-vacancies/salary-range?min=30000&max=50000
     */
    @GetMapping("/salary-range")
    public ResponseEntity<List<JobVacancyDTO>> getBySalaryRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        log.info("GET /api/job-vacancies/salary-range - Get vacancies by salary range: {} - {}", min, max);
        return ResponseEntity.ok(jobVacancyService.getBySalaryRange(min, max));
    }

    // ===== BÚSQUEDAS AVANZADAS =====

    /**
     * Búsqueda avanzada de vacantes con múltiples filtros
     *
     * @param filters Objeto con los filtros de búsqueda (categoría, ubicación, salario, habilidades, etc.)
     * @return Lista de JobVacancyDTO que cumplen los filtros
     *
     * @example POST /api/job-vacancies/search
     * @example Body: { "category": "TECHNOLOGY", "location": "Madrid", "minSalary": 40000 }
     */
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestBody JobSearchFiltersDTO filters) {
        log.info("POST /api/job-vacancies/search - Search vacancies");
        Pageable pageable = PageRequest.of(filters.getPage(), DEFAULT_PAGE_SIZE);
        Page<JobVacancyDTO> jobVacancyDTOS = jobVacancyService.search(filters,pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobVacancyDTOS.getContent());
        response.put("currentPage", jobVacancyDTOS.getNumber());
        response.put("totalItems", jobVacancyDTOS.getTotalElements());
        response.put("totalPages", jobVacancyDTOS.getTotalPages());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene vacantes que están por expirar (próximos 7 días)
     *
     * @return Lista de JobVacancyDTO con vacantes próximas a expirar
     *
     * @example GET /api/job-vacancies/expiring-soon
     */
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<JobVacancyDTO>> findExpiringSoon() {
        log.info("GET /api/job-vacancies/expiring-soon - Get vacancies expiring soon");
        return ResponseEntity.ok(jobVacancyService.findExpiringSoon());
    }

    // ===== ESTADÍSTICAS =====

    /**
     * Cuenta el número de postulantes para una vacante
     *
     * @param id ID de la vacante
     * @return Número de postulantes
     *
     * @example GET /api/job-vacancies/123/applicants-count
     */
    @GetMapping("/{id}/applicants-count")
    public ResponseEntity<Integer> countApplicants(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-vacancies/{}/applicants-count - Count applicants", id);
        return ResponseEntity.ok(jobVacancyService.countApplicants(id));
    }

    /**
     * Obtiene el número de cupos disponibles para una vacante
     *
     * @param id ID de la vacante
     * @return Número de cupos disponibles
     *
     * @example GET /api/job-vacancies/123/available-slots
     */
    @GetMapping("/{id}/available-slots")
    public ResponseEntity<Integer> getAvailableSlots(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-vacancies/{}/available-slots - Get available slots", id);
        return ResponseEntity.ok(jobVacancyService.getAvailableSlots(id));
    }

    /**
     * Verifica si una vacante tiene cupos disponibles
     *
     * @param id ID de la vacante
     * @return true si hay cupos disponibles, false en caso contrario
     *
     * @example GET /api/job-vacancies/123/has-slots
     */
    @GetMapping("/{id}/has-slots")
    public ResponseEntity<Boolean> hasAvailableSlots(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-vacancies/{}/has-slots - Check available slots", id);
        return ResponseEntity.ok(jobVacancyService.hasAvailableSlots(id));
    }

    /**
     * Verifica si una vacante ha expirado
     *
     * @param id ID de la vacante
     * @return true si ha expirado, false en caso contrario
     *
     * @example GET /api/job-vacancies/123/is-expired
     */
    @GetMapping("/{id}/is-expired")
    public ResponseEntity<Boolean> isExpired(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-vacancies/{}/is-expired - Check if expired", id);
        return ResponseEntity.ok(jobVacancyService.isExpired(id));
    }

    /**
     * Verifica si una vacante está activa
     *
     * @param id ID de la vacante
     * @return true si está activa, false en caso contrario
     *
     * @example GET /api/job-vacancies/123/is-active
     */
    @GetMapping("/{id}/is-active")
    public ResponseEntity<Boolean> isActive(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-vacancies/{}/is-active - Check if active", id);
        return ResponseEntity.ok(jobVacancyService.isActive(id));
    }
}