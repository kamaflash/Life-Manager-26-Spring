package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.services.FormationService;
import com.pet.businessdomain.shareddto.dto.FormationDto;
import com.pet.businessdomain.shareddto.dto.ScholarshipDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/formations")
public class FormationController {

    @Autowired
    private FormationService formationService;

    @Autowired
    private FormationMapper formationMapper;

    // =========================
    // 📚 LISTAR TODAS LAS FORMACIONES ACTIVAS
    // =========================
    @GetMapping
    public ResponseEntity<List<FormationDto>> getAllActiveFormations() {
        List<Formation> formations = formationService.getAllActive();
        if (formations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<FormationDto> dtos = formationMapper.toDtoList(formations);
        return ResponseEntity.ok(dtos);
    }

    // =========================
    // 🔍 OBTENER FORMACIÓN POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<FormationDto> getFormationById(@PathVariable Long id) {
        try {
            Formation formation = formationService.getById(id);
            return ResponseEntity.ok(formationMapper.toDto(formation));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // =========================
    // 🔍 OBTENER FORMACIONES POR CATEGORÍA
    // =========================
    @GetMapping("/category/{category}")
    public ResponseEntity<List<FormationDto>> getFormationsByCategory(@PathVariable String category) {
        List<Formation> formations = formationService.getByCategory(category);
        if (formations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(formationMapper.toDtoList(formations));
    }

    // =========================
    // ➕ CREAR NUEVA FORMACIÓN
    // =========================
    @PostMapping
    public ResponseEntity<FormationDto> createFormation(@RequestBody FormationDto formationDto) {
        try {
            FormationDto savedDto = formationService.createFormation(formationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(null);
        }
    }

    @SneakyThrows
    @PostMapping("/batch")
    public ResponseEntity<List<FormationDto>> createFormationAll(@RequestBody List<FormationDto> dtos) {
        List<FormationDto> created = dtos.stream()
                .map(dto -> {
                    try {
                        return formationService.createFormation(dto);
                    } catch (BusinessRuleException e) {
                        log.warn("No se pudo crear formación {}: {}", dto.getCode(), e.getMessage());
                        return null; // o puedes filtrar luego los nulls
                    }
                })
                .filter(Objects::nonNull)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =========================
    // ✏️ ACTUALIZAR FORMACIÓN
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<FormationDto> updateFormation(
            @PathVariable Long id,
            @RequestBody FormationDto formationDto
    ) {
        try {
            FormationDto updated = formationService.updateFormation(id, formationDto);
            return ResponseEntity.ok(updated);
        } catch (BusinessRuleException ex) {
            return ResponseEntity.status(ex.getHttpStatus()).body(null);
        }
    }

    // =========================
    // ❌ ELIMINAR / DESACTIVAR FORMACIÓN
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        try {
            formationService.deactivateFormation(id);
            return ResponseEntity.noContent().build();
        } catch (BusinessRuleException ex) {
            return ResponseEntity.status(ex.getHttpStatus()).build();
        }
    }
}
