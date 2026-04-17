package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.RequirementEntity;
import com.pet.businessdomain.shareddto.dto.MissionRequirementDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                EnumMapper.class
        })
public interface MissionRequirementMapper {

    MissionRequirementDto toDto(RequirementEntity entity);

    RequirementEntity toEntity(MissionRequirementDto dto);
}
