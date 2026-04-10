package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.JobPositionService;
import com.pet.businessdomain.shareddto.dto.CompanyDTO;
import com.pet.businessdomain.shareddto.dto.JobPositionDTO;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/job-positions")
@RequiredArgsConstructor
public class JobPositionController {

    private final JobPositionService jobPositionService;

    // ===== CRUD BÁSICO =====

    /**
     * Crea un nuevo puesto de trabajo
     *
     * @param dto Datos del puesto a crear
     * @return JobPositionDTO con los datos del puesto creado
     *
     * @example POST /api/job-positions
     * @example Body: { "title": "Desarrollador Senior", "companyId": 100, "category": "TECHNOLOGY" }
     */
    @PostMapping
    public ResponseEntity<JobPositionDTO> create(@RequestBody JobPositionDTO dto) {
        log.info("POST /api/job-positions - Create job position: {}", dto.getTitle());
        return ResponseEntity.ok(jobPositionService.create(dto));
    }

    /**
     * Crea múltiples puestos de trabajo en lote
     *
     * @param dtos Lista de puestos a crear
     * @return Lista de JobPositionDTO con los puestos creados
     *
     * @example POST /api/job-positions/batch
     * @example Body: [{ "title": "Backend Dev", "companyId": 100 }, { "title": "Frontend Dev", "companyId": 100 }]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<JobPositionDTO>> createBatch(@RequestBody List<JobPositionDTO> dtos) {
        log.info("POST /api/job-positions/batch - Creating {} job positions", dtos.size());

        List<JobPositionDTO> createdPositions = new ArrayList<>();
        for (JobPositionDTO dto : dtos) {
            log.info("  - Creating job position: {} for companyId: {}", dto.getTitle(), dto.getCompanyId());
            JobPositionDTO created = jobPositionService.create(dto);
            createdPositions.add(created);
        }

        log.info("Successfully created {} job positions", createdPositions.size());
        return ResponseEntity.ok(createdPositions);
    }

    /**
     * Actualiza un puesto de trabajo existente
     *
     * @param id ID del puesto a actualizar
     * @param dto Nuevos datos del puesto
     * @return JobPositionDTO con los datos actualizados
     *
     * @example PUT /api/job-positions/456
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobPositionDTO> update(
            @PathVariable(name = "id") Long id,
            @RequestBody JobPositionDTO dto) {
        log.info("PUT /api/job-positions/{} - Update job position", id);
        return ResponseEntity.ok(jobPositionService.update(id, dto));
    }

    /**
     * Obtiene un puesto de trabajo por su ID
     *
     * @param id ID del puesto
     * @return JobPositionDTO con los datos del puesto
     *
     * @example GET /api/job-positions/456
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobPositionDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-positions/{} - Get job position", id);
        return ResponseEntity.ok(jobPositionService.getById(id));
    }

    /**
     * Elimina un puesto de trabajo (borrado físico)
     *
     * @param id ID del puesto a eliminar
     * @return 204 No Content si la operación es exitosa
     *
     * @example DELETE /api/job-positions/456
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        log.info("DELETE /api/job-positions/{} - Delete job position", id);
        jobPositionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Desactiva un puesto de trabajo (borrado lógico)
     *
     * @param id ID del puesto a desactivar
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/job-positions/456/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/job-positions/{}/deactivate - Deactivate job position", id);
        jobPositionService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todos los puestos de trabajo
     *
     * @return Lista de JobPositionDTO con todos los puestos
     *
     * @example GET /api/job-positions
     */
    @GetMapping
    public ResponseEntity<List<JobPositionDTO>> getAll() {
        log.info("GET /api/job-positions - Get all job positions");
        return ResponseEntity.ok(jobPositionService.getAll());
    }

    /**
     * Obtiene solo los puestos de trabajo activos
     *
     * @return Lista de JobPositionDTO con puestos activos
     *
     * @example GET /api/job-positions/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<JobPositionDTO>> getAllActive() {
        log.info("GET /api/job-positions/active - Get all active job positions");
        return ResponseEntity.ok(jobPositionService.getAllActive());
    }

    /**
     * Obtiene todos los puestos de una empresa
     *
     * @param companyId ID de la empresa
     * @return Lista de JobPositionDTO con los puestos de la empresa
     *
     * @example GET /api/job-positions/company/100
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobPositionDTO>> getByCompany(@PathVariable(name = "companyId") Long companyId) {
        log.info("GET /api/job-positions/company/{} - Get positions by company", companyId);
        return ResponseEntity.ok(jobPositionService.getByCompany(companyId));
    }

    /**
     * Obtiene solo los puestos activos de una empresa
     *
     * @param companyId ID de la empresa
     * @return Lista de JobPositionDTO con puestos activos de la empresa
     *
     * @example GET /api/job-positions/company/100/active
     */
    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<JobPositionDTO>> getByCompanyActive(@PathVariable(name = "companyId") Long companyId) {
        log.info("GET /api/job-positions/company/{}/active - Get active positions by company", companyId);
        return ResponseEntity.ok(jobPositionService.getByCompanyActive(companyId));
    }

    /**
     * Obtiene puestos de trabajo por categoría
     *
     * @param category Categoría del puesto (TECHNOLOGY, HEALTH, BUSINESS, etc.)
     * @return Lista de JobPositionDTO con puestos de esa categoría
     *
     * @example GET /api/job-positions/category/TECHNOLOGY
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<JobPositionDTO>> getByCategory(@PathVariable(name = "category") JobCategory category) {
        log.info("GET /api/job-positions/category/{} - Get positions by category", category);
        return ResponseEntity.ok(jobPositionService.getByCategory(category));
    }

    /**
     * Obtiene puestos de trabajo por nivel jerárquico
     *
     * @param level Nivel del puesto (JUNIOR, SEMI_SENIOR, SENIOR, LEAD, MANAGER, DIRECTOR)
     * @return Lista de JobPositionDTO con puestos de ese nivel
     *
     * @example GET /api/job-positions/level/SENIOR
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<List<JobPositionDTO>> getByLevel(@PathVariable(name = "level") String level) {
        log.info("GET /api/job-positions/level/{} - Get positions by level", level);
        return ResponseEntity.ok(jobPositionService.getByLevel(level));
    }

    /**
     * Obtiene puestos de trabajo por rama de especialización
     *
     * @param careerPath Rama profesional (TECHNOLOGY, BUSINESS, HEALTH, CREATIVE, CONSTRUCTION, SOCIAL)
     * @return Lista de JobPositionDTO con puestos de esa rama
     *
     * @example GET /api/job-positions/career-path/TECHNOLOGY
     */
    @GetMapping("/career-path/{careerPath}")
    public ResponseEntity<List<JobPositionDTO>> getByCareerPath(@PathVariable(name = "careerPath") String careerPath) {
        log.info("GET /api/job-positions/career-path/{} - Get positions by career path", careerPath);
        return ResponseEntity.ok(jobPositionService.getByCareerPath(careerPath));
    }

    /**
     * Busca puestos de trabajo por título
     *
     * @param title Título del puesto a buscar
     * @return Lista de JobPositionDTO con puestos que coinciden con el título
     *
     * @example GET /api/job-positions/search?title=Desarrollador
     */
    @GetMapping("/search")
    public ResponseEntity<List<JobPositionDTO>> getByTitle(@RequestParam String title) {
        log.info("GET /api/job-positions/search - Get positions by title: {}", title);
        return ResponseEntity.ok(jobPositionService.getByTitle(title));
    }

    // ===== VACANTES RELACIONADAS =====

    /**
     * Obtiene los puestos que tienen al menos una vacante activa
     *
     * @return Lista de JobPositionDTO con puestos que tienen vacantes activas
     *
     * @example GET /api/job-positions/with-vacancies
     */
    @GetMapping("/with-vacancies")
    public ResponseEntity<List<JobPositionDTO>> getPositionsWithActiveVacancies() {
        log.info("GET /api/job-positions/with-vacancies - Get positions with active vacancies");
        return ResponseEntity.ok(jobPositionService.getPositionsWithActiveVacancies());
    }

    /**
     * Cuenta las vacantes activas de un puesto
     *
     * @param id ID del puesto
     * @return Número de vacantes activas
     *
     * @example GET /api/job-positions/456/vacancies-count
     */
    @GetMapping("/{id}/vacancies-count")
    public ResponseEntity<Integer> countActiveVacanciesByPosition(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-positions/{}/vacancies-count - Count active vacancies", id);
        return ResponseEntity.ok(jobPositionService.countActiveVacanciesByPosition(id));
    }

    // ===== VALIDACIONES =====

    /**
     * Verifica si existe un puesto con el mismo título en una empresa
     *
     * @param companyId ID de la empresa
     * @param title Título del puesto
     * @return true si ya existe, false en caso contrario
     *
     * @example GET /api/job-positions/exists?companyId=100&title=Desarrollador
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByCompanyAndTitle(
            @RequestParam Long companyId,
            @RequestParam String title) {
        log.info("GET /api/job-positions/exists - Check if position exists: companyId={}, title={}", companyId, title);
        return ResponseEntity.ok(jobPositionService.existsByCompanyAndTitle(companyId, title));
    }
}