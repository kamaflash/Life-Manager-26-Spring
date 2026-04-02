package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.mapper.ICharacterTrainingMapper;
import com.pet.businessdomain.formationservice.repository.ICharacterTrainingRepository;
import com.pet.businessdomain.formationservice.services.ExamManagerService;
import com.pet.businessdomain.formationservice.services.ICharacterTrainingService;
import com.pet.businessdomain.formationservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.CharacterTrainingDto;
import com.pet.businessdomain.shareddto.dto.SystemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trainer")
public class CharacterTrainingController {
    private static final int DEFAULT_SIZE = 10;


    @Autowired
    private FormationMapper formationMapper;
    @Autowired
    private ICharacterTrainingMapper iCharacterTrainingMapper;

@Autowired
    private ICharacterTrainingService characterTrainingService;
    @Autowired

private ExamManagerService  examManagerService;
    @Autowired
    private ICharacterTrainingRepository iCharacterTrainingRepository;

    @Autowired
    private BusinessTransactions businessTransactions;
    // =========================
    // 🔹 Obtener entrenamientos de un personaje
    // =========================
    @GetMapping("/character/{id}/trainings")
    public ResponseEntity<?> getCharacterTrainings(@PathVariable(name = "id")  Long id) {
        List<CharacterTraining> trainings = characterTrainingService.getTrainingsForCharacter(id);
        return ResponseEntity.ok(trainings);
    }
    @GetMapping("/character/{id}")
    public ResponseEntity<?> getTrainingsByCharacterId(@PathVariable(name = "id") Long id) {
        List<CharacterTraining> trainings = characterTrainingService.getTrainingsForCharacter(id);
        List<CharacterTrainingDto> trainingDtos = iCharacterTrainingMapper.toDtoList(trainings);
        return ResponseEntity.ok(trainingDtos);
    }
    // =========================
    // 🔹 Obtener cursos disponibles para suscribirse
    // =========================
    @GetMapping("/character/{id}/available")
    public ResponseEntity<?> getAvailableCourses(
            @PathVariable(name = "id")  Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        // Obtener la lista completa de cursos disponibles
        List<Formation> availableTrainings = characterTrainingService.getAvailableCoursesForCharacter(id);

        // Paginación manual
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), availableTrainings.size());
        List<Formation> pagedList = availableTrainings.subList(start, end);

        // Crear PageImpl para información de paginación
        Page<Formation> formationsPage = new PageImpl<>(pagedList, pageable, availableTrainings.size());

        // Mapear a DTO
        Map<String, Object> response = new HashMap<>();
        response.put("formations", formationMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // =========================
    // 🔹 Suscribir a un personaje a un curso
    // =========================
    @PostMapping("/subscribe")
    public ResponseEntity<CharacterTrainingDto> subscribeToCourse(@RequestBody CharacterTrainingDto dto) throws BusinessRuleException {
        CharacterTrainingDto subscribed = characterTrainingService.subscribeToCourse(dto, dto.getCharacterId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subscribed);
    }
    @PostMapping("/subscribe/{id}")
    public CharacterTrainingDto subscribeToCourseDto(
            @RequestBody CharacterTrainingDto dto, @PathVariable(name = "id") Long id) throws BusinessRuleException {

        CharacterTrainingDto subscribed = characterTrainingService.subscribeToCourse(dto, id);
        return subscribed;
    }

    // =========================
    // 🔹 Asistir a un curso (sumar horas)
    // =========================
    @PutMapping("/assist/{id}")
    public CharacterTrainingDto attendTraining(@PathVariable(name = "id")  Long id, @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {
        dto.setInvestedHours(dto.getInvestedHours() + 2);
        CharacterTrainingDto updated = characterTrainingService.updateTraining(id, dto);
        return updated;
    }

    // =========================
    // 🔹 Obtener exámenes disponibles para un entrenamiento
    // =========================
    @GetMapping("/training/{trainingId}/exams")
    public ResponseEntity<List<FormationExam>> getExamsForTraining(@PathVariable(name = "trainingId")  Long trainingId) {
        List<FormationExam> exams = characterTrainingService.getExamsForTraining(trainingId);
        if (exams.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(exams);
    }

    // =========================
    // 🔹 Realizar examen de un entrenamiento
    // =========================
    @PostMapping("/training/{trainingId}/take-exam/{characterId}")
    public ResponseEntity<CharacterExam> takeExam(
            @PathVariable(name = "trainingId")  Long trainingId,
            @PathVariable(name = "characterId")  Long characterId
    ) {
        CharacterExam result = examManagerService.takeExam(characterId, trainingId);
        return ResponseEntity.ok(result);
    }

    // =========================
    // 🔹 Consultar exámenes de un personaje
    // =========================
//    @GetMapping("/character/{id}/exams")
//    public ResponseEntity<List<CharacterExam>> getCharacterExams(@PathVariable Long id) {
//        List<CharacterExam> exams = characterTrainingService.getExamsForCharacter(id);
//        return ResponseEntity.ok(exams);
//    }

    // =========================
    // 🔹 Actualizar entrenamiento
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<CharacterTrainingDto> updateTraining(@PathVariable(name = "id")  Long id, @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {
        CharacterTrainingDto updated = characterTrainingService.updateTraining(id, dto);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/dto/{id}/{trainingId}")
    public CharacterTrainingDto getTrainingsById(@PathVariable(name = "id") Long id, @PathVariable(name = "trainingId") Long trainingId) {
        CharacterTraining trainings = characterTrainingService.getByCharacterIdAndTrainingId(id,trainingId);
        return iCharacterTrainingMapper.toDto(trainings);
    }
    // =========================
    // ❌ Desactivar entrenamiento
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateTrainer(@PathVariable(name = "id")  Long id) throws BusinessRuleException {
        characterTrainingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        characterTrainingService.deleteAll();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hecho");
    }
}
