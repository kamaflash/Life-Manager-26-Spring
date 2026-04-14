package com.pet.businessdomain.personservice.controller;


import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.personservice.repository.CharacterRepository;
import com.pet.businessdomain.personservice.services.CharacterService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CharacterRepository characterRepository;

    // Crear un nuevo personaje
    @PostMapping
    public ResponseEntity<CharacterDto> createCharacter(@RequestBody CharacterDto characterDto) {
        if (characterDto == null) {
            return ResponseEntity.badRequest().build();
        }
        CharacterDto created = characterService.createCharacter(characterDto);
        return ResponseEntity.ok(created);
    }
    @PostMapping("/skills/update")
    public ResponseEntity<CharacterSkillsUpdateResponseDto> updateCharacterSkills(
            @RequestBody CharacterSkillsUpdateRequestDto request
    ) {
        CharacterSkillsUpdateResponseDto response = characterService.updateCharacterSkills(request);
        return ResponseEntity.ok(response);
    }
    // Obtener todos los personajes
    @GetMapping
    public ResponseEntity<List<CharacterDto>> getAllCharacters() {
        List<CharacterDto> characters = characterService.getAllCharacters();
        if (characters.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(characters);
    }

    // Obtener personaje por id
    @GetMapping("/{id}")
    public ResponseEntity<CharacterDto> getCharacterById(@PathVariable(name = "id") Long id) {
        try {
            CharacterDto character = characterService.getCharacterById(id);
            return ResponseEntity.ok(character);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    // Obtener personaje por id
    @GetMapping("/id/full/{id}")
    public ResponseEntity<CharacterDto> getCharacterByIdFull(@PathVariable(name = "id") Long id) {
        CharacterDto character = characterService.getCharacterById(id);
        return ResponseEntity.ok(character);
    }

    // Obtener personaje por uid
    @GetMapping("/uid/{uid}")
    public ResponseEntity<CharacterDto> getCharacterByUid(@PathVariable(name = "uid") Long uid) {
        CharacterDto character = characterService.getCharacterByUid(uid);
        return ResponseEntity.ok(character);
    }
    @GetMapping("/uid/full/{uid}")
    public CharacterDto getCharacterByUidFull(@PathVariable(name = "uid") Long uid) {
        CharacterDto character = characterService.getCharacterByUid(uid);
        return character;
    }

    // Actualizar personaje
    @PutMapping("/{id}")
    public ResponseEntity<CharacterDto> updateCharacterName(
            @PathVariable(name = "id") Long id,
            @RequestBody String name) {
        try {
            CharacterDto updated = characterService.updateCharacterName(id, name);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/stats/{id}")
    public CharacterDto updateCharacterStats(
            @PathVariable(name = "id") Long id,
            @RequestBody CharacterDto characterDto) {
        try {
            CharacterDto updated = characterService.updateCharacter(id, characterDto);
            return updated;
        } catch (RuntimeException ex) {
            return null;
        }
    }

    // Borrar personaje (opcional)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable(name = "id") Long id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/all")
    public void deleteAll() {
        characterRepository.deleteAll();
    }

    /**
     * Obtiene las habilidades de un personaje con paginación, ordenamiento y filtros.
     *
     * @param filters Filtros de búsqueda, paginación y ordenamiento
     * @return ResponseEntity con la lista paginada de habilidades
     *
     * @example POST /api/characters/skills/search
     * @example Body: {
     *   "characterId": 1,
     *   "page": 0,
     *   "size": 10,
     *   "sortBy": "level",
     *   "sortDir": "desc",
     *   "search": "programming",
     *   "minLevel": 1,
     *   "maxLevel": 5,
     *   "locked": false
     * }
     */

    @PostMapping("/skills/search")
    public ResponseEntity<SkillPaginatedResponseDTO> getCharacterSkills(@RequestBody SkillFiltersDTO filters) {
        log.info("POST /api/characters/skills/search - Buscando habilidades del personaje: {}",
                filters.getCharacterId());

        if (filters.getCharacterId() == null) {
            throw new IllegalArgumentException("CharacterId es requerido");
        }

        SkillPaginatedResponseDTO response = characterService.getCharacterSkills(filters);
        return ResponseEntity.ok(response);
    }

}
