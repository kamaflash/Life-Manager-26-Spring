package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.RequirementEntity;
import com.pet.businessdomain.shareddto.dto.RequirementDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequirementMapper {

    // ENTITY → DTO
    RequirementDTO toDto(RequirementEntity entity);
    List<RequirementDTO> toDtoList(List<RequirementEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    RequirementEntity toEntity(RequirementDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(RequirementDTO dto, @MappingTarget RequirementEntity entity);
}