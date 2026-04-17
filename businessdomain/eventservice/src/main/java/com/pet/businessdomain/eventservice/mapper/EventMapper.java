package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.EventEntity;
import com.pet.businessdomain.shareddto.dto.EventResponseDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                RewardMapper.class,
                MissionRequirementMapper.class,
                EnumMapper.class
        }
)
public interface EventMapper {

    EventResponseDto toDto(EventEntity entity);

    EventEntity toEntity(EventResponseDto dto);
}
