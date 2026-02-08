package com.pet.businessdomain.formationservice.controller;

import com.pet.businessdomain.formationservice.dto.CharacterTrainingDto;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.repository.ICharacterTrainingRepository;
import com.pet.businessdomain.formationservice.services.ICharacterTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    private ICharacterTrainingService characterTrainingService;
    @Autowired
    private ICharacterTrainingRepository iCharacterTrainingRepository;

    @GetMapping
    public ResponseEntity<?> getCharacterAll() {
        return ResponseEntity.ok(iCharacterTrainingRepository.findAll());
    }

    /**
     * 🔹 Obtener todos los entrenamientos de un personaje específico.
     *
     * @param id ID del personaje
     * @return Lista de entrenamientos del personaje
     */
    @GetMapping("/character/{id}/trainings")
    public ResponseEntity<?> getCharacterTrainings(@PathVariable(name = "id")  Long id) {
        List<CharacterTraining> trainings = characterTrainingService.getTrainingsForCharacter(id);
        return ResponseEntity.ok(trainings);
    }
    /**
     * 🔹 Obtener los cursos disponibles para suscribirse según el perfil del personaje.
     *
     * @param characterId ID del personaje
     * @return Lista de cursos a los que el personaje puede suscribirse
     */
    @GetMapping("/character/{id}/available")
    public ResponseEntity<?> getAvailableCourses(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_SIZE) int size,
            @PathVariable(name = "id") Long characterId) {
        Pageable pageable = PageRequest.of(page, size);

        // Llamamos al servicio para obtener los cursos filtrados según las reglas
        List<Formation> availableTrainings = characterTrainingService.getAvailableCoursesForCharacter(characterId);
        // Calculamos los índices para paginar
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), availableTrainings.size());

        // Creamos la página con PageImpl
        Page<Formation> formationsPage = new PageImpl<>(
                availableTrainings.subList(start, end),
                pageable,
                availableTrainings.size()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("formations", formationMapper.toDtoList(formationsPage.getContent()));
        response.put("currentPage", formationsPage.getNumber());
        response.put("totalItems", formationsPage.getTotalElements());
        response.put("totalPages", formationsPage.getTotalPages());
        response.put("Lista", availableTrainings);
        return ResponseEntity.ok(response);
    }


    /**
     * 🔹 Obtener los entrenamientos completados por un personaje específico.
     *
     * @param id ID del personaje
     * @return Lista de entrenamientos completados
     */
    @GetMapping("/character/{id}/completed")
    public ResponseEntity<?> getCompletedTrainings(@PathVariable(name = "id") Long id) {
        List<CharacterTraining> trainings = characterTrainingService.getCompletedTrainings(id);
        return ResponseEntity.ok(trainings);
    }

    /**
     * 🔹 Obtener los entrenamientos subscritos por un personaje específico.
     *
     * @param id ID del personaje
     * @return Lista de entrenamientos completados
     */
    @GetMapping("/character/{id}")
    public ResponseEntity<?> getTrainingsByCharacterId(@PathVariable(name = "id") Long id) {
        List<CharacterTraining> trainings = characterTrainingService.getTrainingsForCharacter(id);
        List<CharacterTrainingDto> trainingDtos = formationMapper.toDtoListFull(trainings);
        return ResponseEntity.ok(trainingDtos);
    }
    /**
     * 🔹 Suscribir a un personaje a un curso de entrenamiento.
     *
     * @param dto Objeto DTO con los datos de la suscripción (personaje y curso)
     * @return DTO con la información de la suscripción creada
     * @throws BusinessRuleException Si hay reglas de negocio que impiden la suscripción
     */
    @PostMapping("/subscribe")
    public ResponseEntity<CharacterTrainingDto> subscribeToCourse(
            @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {

        CharacterTrainingDto subscribed = characterTrainingService.subscribeToCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscribed);
    }
    @PostMapping()
    public CharacterTrainingDto subscribeToCourseDto(
            @RequestBody CharacterTrainingDto dto) throws BusinessRuleException {

        CharacterTrainingDto subscribed = characterTrainingService.subscribeToCourse(dto);
        return subscribed;
    }

    // =========================
    // ❌ DESACTIVAR FORMACIÓN
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateTrainer(@PathVariable(name = "id") Long id)
            throws BusinessRuleException {

        iCharacterTrainingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        iCharacterTrainingRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hecho");
    }
}
