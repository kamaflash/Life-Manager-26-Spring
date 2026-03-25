
package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.repository.FormationRepository;
import com.pet.businessdomain.formationservice.services.FormationService;
import com.pet.businessdomain.shareddto.dto.FormationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/formations")
public class FormationController {

    private static final int DEFAULT_SIZE = 10;

    @Autowired
    private FormationService formationService;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    // =========================
    // 📚 LISTAR FORMACIONES
    // =========================
    @GetMapping
    public ResponseEntity<?> getAllFormations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Formation> formationsPage = formationRepository.findByActiveTrue(pageable);

        if (formationsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("formations", formationMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // =========================
    // 🔍 OBTENER POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<FormationDto> getFormationById(@PathVariable(name = "id") Long id)
            throws BusinessRuleException {

        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException(
                        "1001",                               // código de error
                        "La formación no existe",
                        HttpStatus.BAD_REQUEST
                ));

        return ResponseEntity.ok(formationMapper.toDto(formation));
    }

    // =========================
    // 🔍 OBTENER POR CODE
    // =========================
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getFormationByCode(@PathVariable(name = "code") String code,
                                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Formation> formationsPage = formationRepository.findByCode(pageable,code);

        if (formationsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("formations", formationMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());
        return ResponseEntity.ok(response);
    }


    // =========================
    // ➕ CREAR FORMACIÓN
    // =========================
    @PostMapping
    public ResponseEntity<FormationDto> createFormation(
            @RequestBody FormationDto formationDto
    ) throws BusinessRuleException {

        FormationDto created = formationService.createFormation(formationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> createFormationsBatch(@RequestBody List<FormationDto> formationDtos) {
        if (formationDtos == null || formationDtos.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No se ha recibido ninguna formación");
        }

        List<FormationDto> createdFormations = new ArrayList<>();

        for (FormationDto dto : formationDtos) {
            try {
                // Validaciones básicas
                if (dto.getCode() == null || dto.getCode().isBlank()) {
                    throw new BusinessRuleException(
                            "1001",
                            "El código de la formación es obligatorio",
                            HttpStatus.BAD_REQUEST
                    );
                }

                if (formationRepository.existsByCode(dto.getCode())) {
                    throw new BusinessRuleException(
                            "1002",
                            "Ya existe una formación con el código: " + dto.getCode(),
                            HttpStatus.CONFLICT
                    );
                }

                // Mapear y guardar
                Formation formation = formationMapper.toEntity(dto);
                formation.setActive(true); // aseguramos que estén activas
                Formation saved = formationRepository.save(formation);

                createdFormations.add(formationMapper.toDto(saved));

            } catch (BusinessRuleException e) {
                log.warn("Formación ignorada: {}", e.getMessage());
                // opcional: podrías recolectar errores en un array y devolverlos todos
            }
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdFormations);
    }

    // =========================
    // ✏️ ACTUALIZAR FORMACIÓN
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<FormationDto> updateFormation(
            @PathVariable(name = "id") Long id,
            @RequestBody FormationDto formationDto
    ) throws BusinessRuleException {

        FormationDto updated = formationService.updateFormation(id, formationDto);
        return ResponseEntity.accepted().body(updated);
    }

    // =========================
    // 🔓 FORMACIONES DISPONIBLES PARA PERSONAJE
    // =========================
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableFormations(
            @RequestParam(name = "educationLevel") EnumAll.EducationLevel educationLevel,
            @RequestParam(name = "academicLevel") Integer academicLevel,
            @RequestParam(name = "academicXp") Integer academicXp,
            @RequestParam(name = "careerInterest", required = false) EnumAll.CareerInterest careerInterest,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Formation> formationsPage = formationRepository.findAvailableFormations(
                educationLevel,
                academicLevel,
                academicXp,
                careerInterest,
                pageable
        );

        if (formationsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("formations", formationMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }


    // =========================
    // ❌ DESACTIVAR FORMACIÓN
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateFormation(@PathVariable(name = "id") Long id)
            throws BusinessRuleException {

        formationService.deactivateFormation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        formationRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hecho");
    }
}
