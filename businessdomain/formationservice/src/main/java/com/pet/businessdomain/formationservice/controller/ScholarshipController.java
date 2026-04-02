package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.shareddto.dto.ScholarshipApplicationDto;
import com.pet.businessdomain.shareddto.dto.ScholarshipDto;
import com.pet.businessdomain.formationservice.services.ScholarshipApplicationService;
import com.pet.businessdomain.formationservice.services.ScholarshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;
    @Autowired
    private ScholarshipApplicationService scholarshipApplicationService;

    @PostMapping
    public ResponseEntity<ScholarshipDto> create(@RequestBody ScholarshipDto dto) {
        return ResponseEntity.ok(scholarshipService.create(dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ScholarshipDto>> createBatch(@RequestBody List<ScholarshipDto> dtos) {
        List<ScholarshipDto> created = dtos.stream()
                .map(scholarshipService::create)
                .toList();
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScholarshipDto> update(@PathVariable(name = "id") Long id,
                                                 @RequestBody ScholarshipDto dto) {
        return ResponseEntity.ok(scholarshipService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScholarshipDto> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(scholarshipService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ScholarshipDto>> getAllActive() {
        return ResponseEntity.ok(scholarshipService.getAllActive());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<List<ScholarshipDto>> getForCharacterId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(scholarshipService.getAllActive());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        scholarshipService.delete(id);
        return ResponseEntity.noContent().build();
    }
    // ✅ 1. Un personaje aplica a una beca
    @PostMapping("/apply")
    public ResponseEntity<ScholarshipApplicationDto> apply(
            @RequestParam(name = "scholarshipId") Long scholarshipId,
            @RequestParam(name = "characterId") Long characterId) {

        ScholarshipApplicationDto application =
                scholarshipApplicationService.applyToScholarship(scholarshipId, characterId);

        return ResponseEntity.ok(application);
    }

    // ✅ 2. Listar todas las aplicaciones de un personaje
    @GetMapping("/by-character/{characterId}")
    public ResponseEntity<List<ScholarshipApplicationDto>> getByCharacter(
            @PathVariable(name = "characterId") Long characterId) {

        List<ScholarshipApplicationDto> applications =
                scholarshipApplicationService.getApplicationsByCharacter(characterId);

        return ResponseEntity.ok(applications);
    }

    // ✅ 3. (Opcional) Listar todas las aplicaciones de una beca
    @GetMapping("/by-scholarship/{scholarshipId}")
    public ResponseEntity<List<ScholarshipApplicationDto>> getByScholarship(
            @PathVariable(name = "scholarshipId") Long scholarshipId) {

        List<ScholarshipApplicationDto> applications =
                scholarshipApplicationService.getApplicationsByScholarship(scholarshipId);

        return ResponseEntity.ok(applications);
    }

    // ✅ 4. Actualizar estado de una aplicación (pendiente, aprobada, rechazada)
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ScholarshipApplicationDto> updateStatus(
            @PathVariable(name = "applicationId") Long applicationId,
            @RequestParam(name = "status") String status) {

        ScholarshipApplicationDto updated =
                scholarshipApplicationService.updateStatus(applicationId, status);

        return ResponseEntity.ok(updated);
    }
}
