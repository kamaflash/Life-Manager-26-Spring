package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.shareddto.dto.ScholarshipDto;

import java.util.List;

public interface ScholarshipService {

    ScholarshipDto create(ScholarshipDto dto);

    ScholarshipDto update(Long id, ScholarshipDto dto);

    ScholarshipDto getById(Long id);

    List<ScholarshipDto> getAllActive();

    void delete(Long id);
}
