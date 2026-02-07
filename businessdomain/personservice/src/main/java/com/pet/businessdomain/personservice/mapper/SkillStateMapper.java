package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.personservice.dto.SkillStateDto;
import com.pet.businessdomain.personservice.entities.SkillStateEmbeddable;
import org.mapstruct.Mapper;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SkillStateMapper {
    SkillStateDto toDto(SkillStateEmbeddable entity);
    SkillStateEmbeddable toEntity(SkillStateDto dto);

    default Map<String, SkillStateDto> mapToDto(Map<String, SkillStateEmbeddable> skills) {
        if (skills == null) return null;
        return skills.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> toDto(e.getValue())));
    }

    default Map<String, SkillStateEmbeddable> mapToEntity(Map<String, SkillStateDto> skills) {
        if (skills == null) return null;
        return skills.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> toEntity(e.getValue())));
    }
}

