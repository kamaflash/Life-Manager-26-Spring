package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.dto.*;
import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import com.pet.businessdomain.jobservice.mapper.CompanyMapper;
import com.pet.businessdomain.jobservice.mapper.JobPositionMapper;
import com.pet.businessdomain.jobservice.services.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private static final int DEFAULT_SIZE = 10;

    @Autowired
    private IJobService jobService;
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private JobPositionMapper jobPositionMapper;
    // =======================
    // 🏢 EMPRESAS
    // =======================

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return ResponseEntity.ok(jobService.getAllCompanies());
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(jobService.getCompanyById(id));
    }

    @GetMapping("/companies/category/{category}")
    public ResponseEntity<List<CompanyDto>> getCompaniesByCategory(@PathVariable(name = "category") String category) {
        return ResponseEntity.ok(jobService.getCompaniesByCategory(category));
    }

    @PostMapping("/companies")
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto dto) {
        return new ResponseEntity<>(jobService.createCompany(dto), HttpStatus.CREATED);
    }
    @PostMapping("/companies/batch")
    public List<CompanyDto> createCompaniesBatch(@RequestBody List<CompanyDto> dtos) {
        List<CompanyDto> saved = new ArrayList<>();
        for (CompanyDto dto : dtos) {
            saved.add(jobService.createCompany(dto));
        }
        return saved;
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyDto> updateCompany(
            @PathVariable(name = "id") Long id,
            @RequestBody CompanyDto dto
    ) {
        return ResponseEntity.ok(jobService.updateCompany(id, dto));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable(name = "id") Long id) {
        jobService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // =======================
    // 💼 PUESTOS DE TRABAJO
    // =======================

    @GetMapping("/companies/{companyId}/positions")
    public ResponseEntity<List<JobPositionDto>> getPositionsByCompany(
            @PathVariable(name = "companyId") Long companyId
    ) {
        return ResponseEntity.ok(jobService.getPositionsByCompany(companyId));
    }

    @GetMapping("/positions/{id}")
    public ResponseEntity<JobPositionDto> getPositionById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(jobService.getPositionById(id));
    }

    @GetMapping("/positions/full/{id}")
    public ResponseEntity<JobPositionDto> getPositionFullById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(jobService.getPositionById(id));
    }
    @GetMapping("/positions/fulldto/{id}")
    public JobPositionDto getPositionFullByIdDto(@PathVariable(name = "id") Long id) {
        JobVacancyDto vacancy = jobService.getVacancyById(id);
        return jobService.getPositionById(vacancy.getPositionId());
    }

    @GetMapping("/positions/category/{category}")
    public ResponseEntity<?> getPositionsByCategory(
            @PathVariable(name = "category")  String category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        JobCategory jobCategory;
        try {
            jobCategory = JobCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<JobPositionEntity> formationsPage = jobService.getPositionsByCategoryPage(pageable,jobCategory);
        if (formationsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }


        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobPositionMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/positions/category/{category}/{city}/{userXpAcademy}/{userXpJobs}")
    public ResponseEntity<?> getPositionsByCategory(
            @PathVariable(name = "category") String category,
            @PathVariable(name = "city") String city,
            @PathVariable(name = "userXpAcademy") int userXpAcademy,
            @PathVariable(name = "userXpJobs") int userXpJobs,
            @RequestParam(name = "minMatch", defaultValue = "50") int minMatch,
            @RequestParam(name = "skills") List<String> skills,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "" + DEFAULT_SIZE) int size
    ) {

        JobCategory jobCategory;

        try {
            jobCategory = JobCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid category");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<JobPositionEntity> result =
                jobService.getFilteredPositions(jobCategory, city, skills, minMatch,userXpAcademy,userXpJobs, pageable);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobPositionMapper.toDtoList(result.getContent()));
        response.put("currentPage", result.getNumber());
        response.put("totalItems", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/positions")
    public ResponseEntity<JobPositionDto> createPosition(@RequestBody JobPositionDto dto) {
        return new ResponseEntity<>(jobService.createPosition(dto), HttpStatus.CREATED);
    }
    @PostMapping("/positions/batch")
    public List<JobPositionDto> createPositionBatch(@RequestBody List<JobPositionDto> dtos) {
        List<JobPositionDto> saved = new ArrayList<>();
        for (JobPositionDto dto : dtos) {
            saved.add(jobService.createPosition(dto));
        }
        return saved;
    }
    @PutMapping("/positions/{id}")
    public ResponseEntity<JobPositionDto> updatePosition(
            @PathVariable Long id,
            @RequestBody JobPositionDto dto
    ) {
        return ResponseEntity.ok(jobService.updatePosition(id, dto));
    }

    @DeleteMapping("/positions/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable(name = "id") Long id) {
        jobService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }

    // =======================
    // 📌 VACANTES
    // =======================

    @GetMapping("/positions/{positionId}/vacancies")
    public ResponseEntity<List<JobVacancyDto>> getVacanciesByPosition(
            @PathVariable Long positionId
    ) {
        return ResponseEntity.ok(jobService.getVacanciesByPosition(positionId));
    }

    @GetMapping("/vacancies/{id}")
    public ResponseEntity<JobVacancyDto> getVacancyById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(jobService.getVacancyById(id));
    }

    @GetMapping("/vacancies/full")
    public ResponseEntity<List<VacancyFullDto>> getAllFullVacancies() {
        return ResponseEntity.ok(jobService.getAllFullVacancies());
    }

    @PostMapping("/vacancies")
    public ResponseEntity<JobVacancyDto> createVacancy(@RequestBody JobVacancyDto dto) {
        return new ResponseEntity<>(jobService.createVacancy(dto), HttpStatus.CREATED);
    }
    @PostMapping("/vacancies/batch")
    public List<JobVacancyDto> createVacaciesBatch(@RequestBody List<JobVacancyDto> dtos) {
        List<JobVacancyDto> saved = new ArrayList<>();
        for (JobVacancyDto dto : dtos) {
            saved.add(jobService.createVacancy(dto));
        }
        return saved;
    }
    @PutMapping("/vacancies/{id}")
    public ResponseEntity<JobVacancyDto> updateVacancy(
            @PathVariable(name = "id") Long id,
            @RequestBody JobVacancyDto dto
    ) {
        return ResponseEntity.ok(jobService.updateVacancy(id, dto));
    }

    @DeleteMapping("/vacancies/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable(name = "id") Long id) {
        jobService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }

    // =======================
    // 📝 APLICACIONES
    // =======================

    @GetMapping("/applications/character/{characterId}")
    public ResponseEntity<List<CharacterApplicationDto>> getApplicationsByCharacter(
            @PathVariable(name = "characterId") Long characterId
    ) {
        return ResponseEntity.ok(jobService.getApplicationsByCharacter(characterId));
    }
    @GetMapping("/applications/character/all/{characterId}")
    public CharacterApplicationDto getApplicationsByCharacterAll(
            @PathVariable(name = "characterId") Long characterId
    ) {
        List<CharacterApplicationDto> jobs = jobService.getApplicationsByCharacter(characterId);
        return jobs.getFirst();
    }

    @PostMapping("/applications")
    public ResponseEntity<CharacterApplicationDto> applyToVacancy(
            @RequestBody CharacterApplicationDto dto
    ) {
        return new ResponseEntity<>(
                jobService.applyToVacancy(dto),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> cancelApplication(@PathVariable(name = "id") Long id) {
        jobService.cancelApplication(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> cancelAll() {
        jobService.cancelAll();
        return ResponseEntity.noContent().build();
    }

    // =======================
    // 🧠 LÓGICA AVANZADA
    // =======================

    @GetMapping("/vacancies/available/{characterId}")
    public ResponseEntity<List<JobVacancyDto>> getAvailableVacanciesForCharacter(
            @PathVariable(name = "characterId") Long characterId
    ) {
        return ResponseEntity.ok(
                jobService.getAvailableVacanciesForCharacter(characterId)
        );
    }
    @GetMapping("/vacancies/subscribe")
    public ResponseEntity<CharacterApplicationDto> postAvailableVacanciesForCharacter(
            @RequestBody CharacterApplicationDto dto
    ) {
        return ResponseEntity.ok(
                jobService.applyToVacancy(dto)
        );
    }
}
