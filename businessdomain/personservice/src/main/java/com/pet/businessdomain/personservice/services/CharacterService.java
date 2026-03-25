package com.pet.businessdomain.personservice.services;


import com.pet.businessdomain.shareddto.dto.CharacterDto;

import java.util.List;

public interface CharacterService {

    CharacterDto createCharacter(CharacterDto characterDto);

    CharacterDto getCharacterById(Long id);

    CharacterDto getCharacterByUid(Long uid);

    List<CharacterDto> getAllCharacters();

    CharacterDto updateCharacterName(Long id, String name);

    CharacterDto updateCharacter(Long id, CharacterDto characterDto);

    void deleteCharacter(Long id);
}
