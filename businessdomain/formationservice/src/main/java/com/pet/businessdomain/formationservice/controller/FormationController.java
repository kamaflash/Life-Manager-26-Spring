package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.services.FormationService;
import com.pet.businessdomain.formationservice.services.ICharacterTrainingService;
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

    @Autowired
    private ICharacterTrainingService iCharacterTrainingService;
    // =========================
    // 📚 LISTAR TODAS LAS FORMACIONES ACTIVAS
    // =========================

    /**
     * Obtiene todas las formaciones activas del sistema.
     * - Llama al servicio para recuperar únicamente las formaciones activas.
     * - Si no hay resultados devuelve 204 (No Content).
     * - Si hay resultados los transforma a DTO y devuelve 200 (OK).
     */
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

    /**
     * Obtiene una formación concreta por su ID.
     * - Si la formación existe, la convierte a DTO y devuelve 200.
     * - Si no existe (o el servicio lanza excepción), devuelve 404.
     */
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

    /**
     * Obtiene formaciones filtradas por categoría.
     * - Recibe la categoría como String.
     * - Devuelve 204 si no hay resultados.
     * - Devuelve 200 con lista de DTOs si existen.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<FormationDto>> getFormationsByCategory(@PathVariable(name = "category") String category) {
        List<Formation> formations = formationService.getByCategory(category);

        if (formations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(formationMapper.toDtoList(formations));
    }

    // =========================
    // ➕ CREAR NUEVA FORMACIÓN
    // =========================

    /**
     * Crea una nueva formación.
     * - Recibe un DTO con los datos de la formación.
     * - Llama al servicio para aplicar reglas de negocio y persistir.
     * - Devuelve 201 (Created) si se crea correctamente.
     * - Si hay error de negocio, devuelve el status definido en la excepción.
     */
    @PostMapping
    public ResponseEntity<FormationDto> createFormation(@RequestBody FormationDto formationDto) {
        try {
            FormationDto savedDto = formationService.createFormation(formationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(null);
        }
    }

    /**
     * Crea múltiples formaciones en lote (batch).
     * - Itera sobre la lista de DTOs recibidos.
     * - Intenta crear cada formación individualmente.
     * - Si alguna falla, se ignora (se loguea y se devuelve null).
     * - Filtra los nulls para devolver solo las creadas correctamente.
     * - Devuelve 201 con la lista de formaciones creadas.
     */
    @SneakyThrows
    @PostMapping("/batch")
    public ResponseEntity<List<FormationDto>> createFormationAll(@RequestBody List<FormationDto> dtos) {
        List<FormationDto> created = dtos.stream()
                .map(dto -> {
                    try {
                        return formationService.createFormation(dto);
                    } catch (BusinessRuleException e) {
                        log.warn("No se pudo crear formación {}: {}", dto.getCode(), e.getMessage());
                        return null; // Se ignoran errores individuales
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =========================
    // ✏️ ACTUALIZAR FORMACIÓN
    // =========================

    /**
     * Actualiza una formación existente.
     * - Recibe el ID y los nuevos datos en el DTO.
     * - Delega la lógica al servicio.
     * - Devuelve 200 con la formación actualizada.
     * - Si hay error de negocio, devuelve el status correspondiente.
     */
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

    /**
     * Desactiva (elimina lógicamente) una formación.
     * - No borra físicamente, sino que cambia su estado (soft delete).
     * - Devuelve 204 si se realiza correctamente.
     * - Si falla por reglas de negocio, devuelve el status correspondiente.
     */
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