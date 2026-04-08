package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.CharacterExamMapper;
import com.pet.businessdomain.formationservice.mapper.FormationExamMapper;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.mapper.ICharacterTrainingMapper;
import com.pet.businessdomain.formationservice.services.ExamManagerService;
import com.pet.businessdomain.formationservice.services.FormationService;
import com.pet.businessdomain.formationservice.services.ICharacterTrainingService;
import com.pet.businessdomain.formationservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/trainer")
@Validated
public class CharacterTrainingController {
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    private ICharacterTrainingService characterTrainingService;

    @Autowired
    private FormationService formationService;

    @Autowired
    private FormationMapper formationMapper;

    @Autowired
    private ICharacterTrainingMapper iCharacterTrainingMapper;

    @Autowired
    private ICharacterTrainingMapper characterTrainingMapper;

    @Autowired
    private ExamManagerService examManagerService;

    @Autowired
    private CharacterExamMapper characterExamMapper;


    @Autowired
    private FormationExamMapper formationExamMapper;

    @Autowired
    private BusinessTransactions businessTransactions;

    // ======================== OBTENER ENTRENAMIENTOS ========================

    /**
     * Obtiene todos los entrenamientos asociados a un personaje.
     * - Llama al servicio para recuperar la lista completa.
     * - Si no hay resultados devuelve 204 (No Content).
     * - Si hay resultados, los mapea a DTO y devuelve 200 (OK).
     */
    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<CharacterTrainingDto>> getCharacterTrainings(
            @PathVariable(name = "characterId") Long characterId) {
        log.info("Obteniendo entrenamientos para personaje: {}", characterId);
        List<CharacterTraining> trainings = characterTrainingService.getTrainingsForCharacter(characterId);

        if (trainings.isEmpty()) {
            log.debug("No hay entrenamientos para el personaje: {}", characterId);
            return ResponseEntity.noContent().build();
        }

        List<CharacterTrainingDto> dtos = characterTrainingMapper.toDtoList(trainings);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene todos los entrenamientos de un personaje con paginación manual.
     * - Recupera todos los entrenamientos desde el servicio.
     * - Calcula manualmente los índices de paginación.
     * - Construye un PageImpl para simular paginación.
     * - Devuelve metadatos (página, total, etc.) junto con los datos.
     */
    @GetMapping("/character/{id}/full/trainings")
    public ResponseEntity<Map<String, Object>> getCharacterTrainings(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
            @PathVariable(name = "id") Long characterId) {
        Pageable pageable = PageRequest.of(page, size);

        // Obtener todos los entrenamientos
        List<CharacterTraining> availableTrainings = characterTrainingService.getTrainingsForCharacter(characterId);

        // Calcular rango de la página
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), availableTrainings.size());

        // Crear página manual
        Page<CharacterTraining> formationsPage = new PageImpl<>(
                availableTrainings.subList(start, end),
                pageable,
                availableTrainings.size()
        );

        // Construir respuesta con metadata
        Map<String, Object> response = new HashMap<>();
        response.put("formations", iCharacterTrainingMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());
        response.put("Lista", availableTrainings); // Lista completa (posible uso debug)

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un entrenamiento específico por su ID.
     * - Si no existe devuelve 404.
     * - Si existe lo convierte a DTO y devuelve 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTrainingById(@PathVariable(name = "id") Long id) {
        log.info("Obteniendo entrenamiento: {}", id);
        CharacterTraining training = characterTrainingService.getById(id);
        Formation formation = formationService.getById(training.getTrainingId());
        FormationDto formationDto = formationMapper.toDto(formation);
        Map<String, Object> response = new HashMap<>();
        if (training == null) {
            log.warn("Entrenamiento no encontrado: {}", id);
            return ResponseEntity.notFound().build();
        }
        response.put("trainning", characterTrainingMapper.toDto(training));
        response.put("formationDto", formationDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene los entrenamientos completados de un personaje.
     * - Filtra solo los finalizados.
     * - Devuelve 204 si no hay resultados.
     */
    @GetMapping("/character/{characterId}/completed")
    public ResponseEntity<List<CharacterTrainingDto>> getCompletedTrainings(
            @PathVariable(name = "characterId") Long characterId) {
        log.info("Obteniendo entrenamientos completados para personaje: {}", characterId);
        List<CharacterTraining> completed = characterTrainingService.getCompletedTrainings(characterId);

        if (completed.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CharacterTrainingDto> dtos = characterTrainingMapper.toDtoList(completed);
        return ResponseEntity.ok(dtos);
    }

// ======================== CURSOS DISPONIBLES ========================

    /**
     * Obtiene los cursos disponibles para un personaje (no suscritos o válidos según reglas).
     * - Aplica paginación manual.
     * - Devuelve lista de cursos y metadatos de paginación.
     */
    @GetMapping("/available/{characterId}")
    public ResponseEntity<Map<String, Object>> getAvailableCourses(
            @PathVariable(name = "characterId") Long characterId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
        log.info("Obteniendo cursos disponibles para personaje: {}", characterId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Formation> availableCourses = characterTrainingService.getAvailableCoursesForCharacter(characterId, pageable);

        List<CharacterTraining> dtoList = characterTrainingService.getByCharacterIdAndStatus(characterId, EnumAll.TrainingStatus.IN_PROGRESS);
// Obtener IDs en progreso
        Set<Long> trainingIdsInProgress = dtoList.stream()
                .map(CharacterTraining::getTrainingId)
                .collect(Collectors.toSet());

// Filtrar contenido
        List<Formation> filteredList = availableCourses.getContent().stream()
                .filter(course -> !trainingIdsInProgress.contains(course.getId()))
                .toList();

// Reconstruir Page
        Page<Formation> filteredPage = new PageImpl<>(
                filteredList,
                availableCourses.getPageable(),
                filteredList.size()
        );

        if (filteredPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("courses", formationMapper.toDtoList(filteredPage.getContent()));
        response.put("currentPage", filteredPage.getNumber());
        response.put("totalItems", filteredPage.getTotalElements());
        response.put("totalPages", filteredPage.getTotalPages());
        response.put("pageSize", filteredPage.getSize());

        return ResponseEntity.ok(response);
    }

// ======================== SUSCRIPCIÓN ========================

    /**
     * Suscribe un personaje a un curso usando el ID en la URL.
     * - Valida el DTO.
     * - Llama al servicio para crear la relación personaje-curso.
     * - Devuelve 201 (Created).
     */
    @PostMapping("/subscribe/resp/{characterId}")
    public ResponseEntity<CharacterTrainingDto> subscribeToCourse(
            @PathVariable(name = "characterId") Long characterId,
            @Valid @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {
        log.info("Suscribiendo personaje {} a curso: {}", characterId, dto.getTrainingId());

        CharacterTrainingDto subscribed = characterTrainingService.subscribeToCourse(dto, characterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscribed);
    }
    @GetMapping("/dto/{id}/{trainingId}")
    public CharacterTrainingDto getTrainingsById(@PathVariable(name = "id") Long id, @PathVariable(name = "trainingId") Long trainingId) {
        CharacterTraining trainings = characterTrainingService.getByCharacterIdAndTrainingId(id,trainingId);
        return iCharacterTrainingMapper.toDto(trainings);
    }
    /**
     * Simula la asistencia a un entrenamiento sumando 2 horas automáticamente.
     * - Modifica el DTO recibido incrementando horas.
     * - Persiste el cambio mediante el servicio.
     */
    @PutMapping("/assist/{id}")
    public CharacterTrainingDto attendTraining(@PathVariable(name = "id")  Long id, @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {
        dto.setInvestedHours(dto.getInvestedHours() + 2);
        CharacterTrainingDto updated = characterTrainingService.updateTraining(id, dto);
        characterTrainingService.setStasCharacter(dto);
        return updated;
    }
    @PutMapping("/dto/{id}")
    public CharacterTrainingDto updateDtoTraining(
            @PathVariable(name = "id") Long id,
            @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {

        // Llamamos al servicio para actualizar el entrenamiento
        CharacterTrainingDto updated = characterTrainingService.updateTraining(id, dto);

        return updated;
    }

// ======================== ACTUALIZACIÓN ========================

    /**
     * Actualiza completamente un entrenamiento.
     * - Recibe un DTO con los nuevos valores.
     * - Delega la lógica en el servicio.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CharacterTrainingDto> updateTraining(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {
        log.info("Actualizando entrenamiento: {}", id);

        CharacterTrainingDto updated = characterTrainingService.updateTraining(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Registra asistencia sumando un número configurable de horas.
     * - Obtiene el entrenamiento actual.
     * - Convierte a DTO.
     * - Suma las horas (por defecto 2).
     * - Guarda el progreso actualizado.
     */
    @PutMapping("/{id}/attend")
    public ResponseEntity<CharacterTrainingDto> attendClass(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "hours", defaultValue = "2") int hours) throws BusinessRuleException {
        log.info("Registrando {} horas de asistencia para entrenamiento: {}", hours, id);

        CharacterTraining training = characterTrainingService.getById(id);
        if (training == null) {
            return ResponseEntity.notFound().build();
        }

        CharacterTrainingDto dto = characterTrainingMapper.toDto(training);
        dto.setInvestedHours((dto.getInvestedHours() != null ? dto.getInvestedHours() : 0) + hours);

        CharacterTrainingDto updated = characterTrainingService.updateTraining(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Registra horas de estudio dedicadas.
     * - Suma las horas especificadas a studyHours.
     * - Por defecto 2 horas si no se especifica.
     */
    @PutMapping("/{id}/study")
    public ResponseEntity<CharacterTrainingDto> studyTraining(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "hours", defaultValue = "2") int hours) throws BusinessRuleException {
        log.info("Registrando {} horas de estudio para entrenamiento: {}", hours, id);

        CharacterTrainingDto updated = characterTrainingService.study(id, hours);
        characterTrainingService.setStasCharacter(updated);
        return ResponseEntity.ok(updated);
    }

// ======================== EXÁMENES ========================

    /**
     * Obtiene los exámenes asociados a un entrenamiento.
     * - Devuelve 204 si no hay exámenes.
     */
    @GetMapping("/{trainingId}/exams")
    public ResponseEntity<List<FormationExamDto>> getExamsForTraining(
            @PathVariable(name = "trainingId") Long trainingId) {
        log.info("Obteniendo exámenes para entrenamiento: {}", trainingId);

        List<FormationExam> exams = characterTrainingService.getExamsForTraining(trainingId);
        if (exams.isEmpty()) {
            return ResponseEntity.ok(formationExamMapper.toDtoList(exams));
        }

        return ResponseEntity.ok(formationExamMapper.toDtoList(exams));
    }

    /**
     * Suscripción alternativa usando el characterId dentro del DTO.
     */
    @PostMapping("/subscribe")
    public ResponseEntity<CharacterTrainingDto> subscribeToCourse(@RequestBody CharacterTrainingDto dto) throws BusinessRuleException {
        CharacterTrainingDto subscribed = characterTrainingService.subscribeToCourse(dto, dto.getCharacterId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subscribed);
    }

    /**
     * Suscripción alternativa pasando el ID por path.
     */
    @PostMapping("/subscribe/{id}")
    public CharacterTrainingDto subscribeToCourseDto(
            @RequestBody CharacterTrainingDto dto, @PathVariable(name = "id") Long id) throws BusinessRuleException {

        CharacterTrainingDto subscribed = characterTrainingService.subscribeToCourse(dto, id);
        return subscribed;
    }

    /**
     * Ejecuta un examen para un personaje en un entrenamiento.
     * - Llama al servicio de exámenes.
     * - Maneja errores devolviendo 500 si falla.
     */
    @PostMapping("/take-exam")
    public ResponseEntity<CharacterExam> takeExam(@RequestBody Map<String, Long> payload) {
        Long characterId = payload.get("characterId");
        Long trainingId = payload.get("trainingId");

        if (characterId == null || trainingId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            CharacterExam exam = examManagerService.takeExam(characterId, trainingId);
            return ResponseEntity.ok(exam);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null); // O un error específico
        }
    }
    @PostMapping("/{trainingId}/exams/take/{characterId}")
    public ResponseEntity<?> takeExam(
            @PathVariable(name = "trainingId") Long trainingId,
            @PathVariable(name = "characterId") Long characterId) {
        log.info("Personaje {} realizando examen del entrenamiento: {}", characterId, trainingId);

        try {
            return ResponseEntity.ok(examManagerService.takeExam(characterId, trainingId));
        } catch (Exception e) {
            log.error("Error al realizar examen", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

// ======================== ELIMINACIÓN ========================

    /**
     * Cancela (elimina) un entrenamiento existente.
     * - Verifica si existe.
     * - Si no existe devuelve 404.
     * - Si existe lo elimina y devuelve 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTraining(@PathVariable(name = "id") Long id) throws BusinessRuleException {
        log.info("Cancelando entrenamiento: {}", id);

        CharacterTraining training = characterTrainingService.getById(id);
        if (training == null) {
            return ResponseEntity.notFound().build();
        }

        characterTrainingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
