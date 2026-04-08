package com.pet.businessdomain.personservice.controller;


import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.personservice.repository.CharacterRepository;
import com.pet.businessdomain.personservice.services.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
