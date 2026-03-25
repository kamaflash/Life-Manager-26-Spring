package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.CharacterApplicationEntity;
import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.shareddto.dto.CharacterApplicationDto;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CharacterApplicationMapper {

    // ENTITY → DTO
    CharacterApplicationDto toDto(CharacterApplicationEntity entity);
    List<CharacterApplicationDto> toDtoList(List<CharacterApplicationEntity> entities);

    // DTO → ENTITY (CREATE)
    CharacterApplicationEntity toEntity(CharacterApplicationDto dto);

    // DTO → ENTITY (UPDATE)
    void updateEntity(CharacterApplicationDto dto, @MappingTarget CharacterApplicationEntity entity);


    default List<CharacterApplicationEntity> toEntityList(List<CharacterApplicationDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toEntity).toList();
    }
}
