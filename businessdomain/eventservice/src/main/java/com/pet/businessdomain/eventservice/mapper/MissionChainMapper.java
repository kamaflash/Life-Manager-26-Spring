package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.MissionChainEntity;
import com.pet.businessdomain.shareddto.dto.MissionChainResponseDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                MissionMapper.class
        }
)
public interface MissionChainMapper {

    MissionChainResponseDto toDto(MissionChainEntity entity);

    MissionChainEntity toEntity(MissionChainResponseDto dto);
}
