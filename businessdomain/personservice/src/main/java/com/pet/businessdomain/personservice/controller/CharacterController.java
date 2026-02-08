package com.pet.businessdomain.personservice.controller;


import com.pet.businessdomain.personservice.dto.CharacterDto;
import com.pet.businessdomain.personservice.repository.CharacterRepository;
import com.pet.businessdomain.personservice.services.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    @Autowired
    private final CharacterService characterService;

    @Autowired
    private final CharacterRepository characterRepository;

    // Crear un nuevo personaje
    @PostMapping
    public ResponseEntity<CharacterDto> createCharacter(@RequestBody CharacterDto characterDto) {
        CharacterDto created = characterService.createCharacter(characterDto);
        return ResponseEntity.ok(created);
    }

    // Obtener todos los personajes
    @GetMapping
    public ResponseEntity<List<CharacterDto>> getAllCharacters() {
        List<CharacterDto> characters = characterService.getAllCharacters();
        return ResponseEntity.ok(characters);
    }

    // Obtener personaje por id
    @GetMapping("/{id}")
    public ResponseEntity<CharacterDto> getCharacterById(@PathVariable(name = "id") Long id) {
        CharacterDto character = characterService.getCharacterById(id);
        return ResponseEntity.ok(character);
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
    public ResponseEntity<CharacterDto> updateCharacter(
            @PathVariable Long id,
            @RequestBody CharacterDto characterDto) {
        CharacterDto updated = characterService.updateCharacter(id, characterDto);
        return ResponseEntity.ok(updated);
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
