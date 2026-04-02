package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.ScholarshipDto;
import com.pet.businessdomain.formationservice.entities.ScholarshipEntity;
import com.pet.businessdomain.formationservice.mapper.ScholarshipMapper;
import com.pet.businessdomain.formationservice.repository.ScholarshipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class ScholarshipServiceImpl implements ScholarshipService {

    @Autowired
    private ScholarshipRepository repository;
    @Autowired
    private ScholarshipMapper mapper;

    @Override
    public ScholarshipDto create(ScholarshipDto dto) {
        ScholarshipEntity entity = mapper.toEntity(dto);
        ScholarshipEntity saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public ScholarshipDto update(Long id, ScholarshipDto dto) {
        ScholarshipEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setAmount(dto.getAmount());
        entity.setActive(dto.getActive());
        entity.setMinXpRequired(dto.getMinXpRequired());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ScholarshipDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));
    }

    @Override
    public List<ScholarshipDto> getAllActive() {

        List<ScholarshipEntity> entities = repository.findByActiveTrue();
        return mapper.toDtoList(entities);
    }

    @Override
    public List<ScholarshipDto> getAllForCharacter(CharacterDto characterDto) {
        return List.of();
    }


    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}