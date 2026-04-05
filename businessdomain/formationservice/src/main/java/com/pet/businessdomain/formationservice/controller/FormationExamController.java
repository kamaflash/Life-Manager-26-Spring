package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.formationservice.services.FormationExamService;
import com.pet.businessdomain.shareddto.dto.FormationExamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@Validated
public class FormationExamController {
    
    private final FormationExamService formationExamService;

    // ======================== OBTENER EXÁMENES ========================

    /**
     * Obtener todos los exámenes de una formación
     */
    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<FormationExamDto>> getByFormation(
            @PathVariable(name = "formationId") Long formationId) {
        log.info("Obteniendo exámenes para formación: {}", formationId);
        List<FormationExamDto> exams = formationExamService.getByFormationId(formationId);

        if (exams.isEmpty()) {
            log.debug("No hay exámenes para la formación: {}", formationId);
            if (exams.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
        }

        return ResponseEntity.ok(exams);
    }

    /**
     * Obtener examen por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormationExam> getById(@PathVariable(name = "id") Long id) {
        log.info("Obteniendo examen: {}", id);
        FormationExam exam = formationExamService.getById(id);

        if (exam == null) {
            log.warn("Examen no encontrado: {}", id);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(exam);
    }

    // ======================== CREAR EXAMEN ========================

    /**
     * Crear un nuevo examen para una formación
     */
    @PostMapping
    public ResponseEntity<FormationExam> create(
            @Valid @RequestBody FormationExam exam) {
        log.info("Creando examen para formación: {}", exam.getFormationId());
        FormationExam saved = formationExamService.save(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Crear múltiples exámenes (batch)
     */
    @PostMapping("/batch")
    public ResponseEntity<List<FormationExam>> createBatch(
            @Valid @RequestBody List<FormationExam> exams) {
        log.info("Creando {} exámenes", exams.size());
        List<FormationExam> saved = exams.stream()
                .map(formationExamService::save)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ======================== ACTUALIZAR ========================

    /**
     * Actualizar un examen
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormationExam> update(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody FormationExam exam) {
        log.info("Actualizando examen: {}", id);
        FormationExam existing = formationExamService.getById(id);
        
        if (existing == null) {
            log.warn("Examen no encontrado: {}", id);
            return ResponseEntity.notFound().build();
        }
        
        exam.setId(id);
        FormationExam updated = formationExamService.save(exam);
        return ResponseEntity.ok(updated);
    }

    // ======================== ELIMINAR ========================

    /**
     * Desactivar un examen (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") Long id) {
        log.info("Desactivando examen: {}", id);
        FormationExam exam = formationExamService.getById(id);

        if (exam == null) {
            log.warn("Examen no encontrado: {}", id);
            return ResponseEntity.notFound().build();
        }

        exam.setActive(false);
        formationExamService.save(exam);
        
        return ResponseEntity.noContent().build();
    }
}
