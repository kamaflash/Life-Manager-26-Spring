package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.CompanyService;
import com.pet.businessdomain.shareddto.dto.CompanyDTO;
import com.pet.businessdomain.shareddto.dto.CompanySummaryDTO;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // ===== CRUD BÁSICO =====

    /**
     * Crea una nueva empresa
     *
     * @param dto Datos de la empresa a crear
     * @return CompanyDTO con los datos de la empresa creada
     *
     * @example POST /api/companies
     * @example Body: { "name": "TechCorp", "category": "TECHNOLOGY", "location": "Madrid" }
     */
    @PostMapping
    public ResponseEntity<CompanyDTO> create(@RequestBody CompanyDTO dto) {
        log.info("POST /api/companies - Create company: {}", dto.getName());
        return ResponseEntity.ok(companyService.create(dto));
    }

    /**
     * Crea múltiples empresas en lote
     *
     * @param dtos Lista de datos de empresas a crear
     * @return Lista de CompanyDTO con las empresas creadas
     *
     * @example POST /api/companies/batch
     * @example Body: [{ "name": "TechCorp" }, { "name": "DevSolutions" }]
     */
    @PostMapping("/batch")
    public ResponseEntity<List<CompanyDTO>> createBatch(@RequestBody List<CompanyDTO> dtos) {
        log.info("POST /api/companies/batch - Creating {} companies", dtos.size());

        List<CompanyDTO> createdCompanies = new ArrayList<>();
        for (CompanyDTO dto : dtos) {
            log.info("  - Creating company: {}", dto.getName());
            CompanyDTO created = companyService.create(dto);
            createdCompanies.add(created);
        }

        log.info("Successfully created {} companies", createdCompanies.size());
        return ResponseEntity.ok(createdCompanies);
    }

    /**
     * Actualiza los datos de una empresa existente
     *
     * @param id ID de la empresa a actualizar
     * @param dto Nuevos datos de la empresa
     * @return CompanyDTO con los datos actualizados
     *
     * @example PUT /api/companies/100
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> update(
            @PathVariable(name = "id") Long id,
            @RequestBody CompanyDTO dto) {
        log.info("PUT /api/companies/{} - Update company", id);
        return ResponseEntity.ok(companyService.update(id, dto));
    }

    /**
     * Obtiene una empresa por su ID
     *
     * @param id ID de la empresa
     * @return CompanyDTO con los datos de la empresa
     *
     * @example GET /api/companies/100
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/companies/{} - Get company", id);
        return ResponseEntity.ok(companyService.getById(id));
    }

    /**
     * Obtiene un resumen de la empresa (datos básicos sin relaciones)
     *
     * @param id ID de la empresa
     * @return CompanySummaryDTO con el resumen de la empresa
     *
     * @example GET /api/companies/100/summary
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<CompanySummaryDTO> getSummaryById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/companies/{}/summary - Get company summary", id);
        return ResponseEntity.ok(companyService.getSummaryById(id));
    }

    /**
     * Elimina una empresa (borrado físico)
     *
     * @param id ID de la empresa a eliminar
     * @return 204 No Content si la operación es exitosa
     *
     * @example DELETE /api/companies/100
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        log.info("DELETE /api/companies/{} - Delete company", id);
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Desactiva una empresa (borrado lógico)
     *
     * @param id ID de la empresa a desactivar
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/companies/100/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/companies/{}/deactivate - Deactivate company", id);
        companyService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todas las empresas
     *
     * @return Lista de CompanyDTO con todas las empresas
     *
     * @example GET /api/companies
     */
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAll() {
        log.info("GET /api/companies - Get all companies");
        return ResponseEntity.ok(companyService.getAll());
    }

    /**
     * Obtiene solo las empresas activas
     *
     * @return Lista de CompanyDTO con empresas activas
     *
     * @example GET /api/companies/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<CompanyDTO>> getAllActive() {
        log.info("GET /api/companies/active - Get all active companies");
        return ResponseEntity.ok(companyService.getAllActive());
    }

    /**
     * Obtiene resúmenes de todas las empresas
     *
     * @return Lista de CompanySummaryDTO con resúmenes de empresas
     *
     * @example GET /api/companies/summaries
     */
    @GetMapping("/summaries")
    public ResponseEntity<List<CompanySummaryDTO>> getAllSummaries() {
        log.info("GET /api/companies/summaries - Get all company summaries");
        return ResponseEntity.ok(companyService.getAllSummaries());
    }

    /**
     * Obtiene empresas por categoría
     *
     * @param category Categoría de la empresa (TECHNOLOGY, HEALTH, etc.)
     * @return Lista de CompanyDTO con empresas de la categoría especificada
     *
     * @example GET /api/companies/category/TECHNOLOGY
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<CompanyDTO>> getByCategory(@PathVariable(name = "category") JobCategory category) {
        log.info("GET /api/companies/category/{} - Get companies by category", category);
        return ResponseEntity.ok(companyService.getByCategory(category));
    }

    /**
     * Obtiene empresas por ubicación
     *
     * @param location Ciudad o ubicación de la empresa
     * @return Lista de CompanyDTO con empresas en la ubicación especificada
     *
     * @example GET /api/companies/location?location=Madrid
     */
    @GetMapping("/location")
    public ResponseEntity<List<CompanyDTO>> getByLocation(@RequestParam String location) {
        log.info("GET /api/companies/location - Get companies by location: {}", location);
        return ResponseEntity.ok(companyService.getByLocation(location));
    }

    /**
     * Obtiene empresas que permiten teletrabajo
     *
     * @return Lista de CompanyDTO con empresas remote-friendly
     *
     * @example GET /api/companies/remote-friendly
     */
    @GetMapping("/remote-friendly")
    public ResponseEntity<List<CompanyDTO>> getRemoteFriendly() {
        log.info("GET /api/companies/remote-friendly - Get remote friendly companies");
        return ResponseEntity.ok(companyService.getRemoteFriendly());
    }

    /**
     * Obtiene empresas que ofrecen programas de prácticas
     *
     * @return Lista de CompanyDTO con empresas que tienen internships
     *
     * @example GET /api/companies/internships
     */
    @GetMapping("/internships")
    public ResponseEntity<List<CompanyDTO>> getWithInternships() {
        log.info("GET /api/companies/internships - Get companies with internships");
        return ResponseEntity.ok(companyService.getWithInternships());
    }

    // ===== BÚSQUEDAS =====

    /**
     * Búsqueda avanzada de empresas con múltiples filtros
     *
     * @param name Nombre de la empresa (opcional)
     * @param category Categoría de la empresa (opcional)
     * @param location Ubicación de la empresa (opcional)
     * @param active Estado activo/inactivo (opcional)
     * @return Lista de CompanyDTO que cumplen los filtros
     *
     * @example GET /api/companies/search?name=Tech&category=TECHNOLOGY&location=Madrid&active=true
     */
    @GetMapping("/search")
    public ResponseEntity<List<CompanyDTO>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) JobCategory category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean active) {
        log.info("GET /api/companies/search - Search companies: name={}, category={}, location={}, active={}",
                name, category, location, active);
        return ResponseEntity.ok(companyService.search(name, category, location, active));
    }

    /**
     * Obtiene las empresas con más vacantes activas
     *
     * @return Lista de CompanyDTO con las empresas top en vacantes
     *
     * @example GET /api/companies/top-vacancies
     */
    @GetMapping("/top-vacancies")
    public ResponseEntity<List<CompanyDTO>> findTopCompaniesByVacancies() {
        log.info("GET /api/companies/top-vacancies - Get top companies by vacancies");
        return ResponseEntity.ok(companyService.findTopCompaniesByVacancies());
    }

    // ===== ESTADÍSTICAS =====

    /**
     * Cuenta las vacantes activas de una empresa
     *
     * @param id ID de la empresa
     * @return Número de vacantes activas
     *
     * @example GET /api/companies/100/vacancies-count
     */
    @GetMapping("/{id}/vacancies-count")
    public ResponseEntity<Integer> countActiveVacanciesByCompany(@PathVariable(name = "id") Long id) {
        log.info("GET /api/companies/{}/vacancies-count - Count active vacancies", id);
        return ResponseEntity.ok(companyService.countActiveVacanciesByCompany(id));
    }

    /**
     * Cuenta los empleados actuales de una empresa
     *
     * @param id ID de la empresa
     * @return Número de empleados
     *
     * @example GET /api/companies/100/employees-count
     */
    @GetMapping("/{id}/employees-count")
    public ResponseEntity<Long> countEmployees(@PathVariable(name = "id") Long id) {
        log.info("GET /api/companies/{}/employees-count - Count employees", id);
        return ResponseEntity.ok(companyService.countEmployees(id));
    }
}