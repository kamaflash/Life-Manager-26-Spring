package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.shareddto.dto.CharacterTrainingDto;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICharacterTrainingMapper {
    // ===== ENTITY -> DTO =====
    CharacterTrainingDto toDto(CharacterTraining entity);

    List<CharacterTrainingDto> toDtoList(List<CharacterTraining> entities);

    // ===== DTO -> ENTITY =====
    CharacterTraining toEntity(CharacterTrainingDto dto);

    List<CharacterTraining> toEntityList(List<CharacterTrainingDto> dtos);

    // ===== ACTUALIZACIÓN PARCIAL =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CharacterTrainingDto dto, @MappingTarget CharacterTraining entity);

    // ===== SOPORTE OPTIONAL =====
    default CharacterTraining fromOptional(Optional<CharacterTraining> opt) {
        return opt.orElse(null);
    }
}
