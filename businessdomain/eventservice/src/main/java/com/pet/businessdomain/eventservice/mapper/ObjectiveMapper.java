package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.ObjectiveEntity;
import com.pet.businessdomain.shareddto.dto.ObjectiveDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                EnumMapper.class
        })
public interface ObjectiveMapper {

    ObjectiveDto toDto(ObjectiveEntity entity);

    ObjectiveEntity toEntity(ObjectiveDto dto);
}
