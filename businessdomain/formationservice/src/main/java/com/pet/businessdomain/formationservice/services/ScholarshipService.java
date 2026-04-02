package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.ScholarshipDto;

import java.util.List;

public interface ScholarshipService {

    ScholarshipDto create(ScholarshipDto dto);

    ScholarshipDto update(Long id, ScholarshipDto dto);

    ScholarshipDto getById(Long id);

    List<ScholarshipDto> getAllActive();
    List<ScholarshipDto> getAllForCharacter(CharacterDto characterDto);

    void delete(Long id);
}
