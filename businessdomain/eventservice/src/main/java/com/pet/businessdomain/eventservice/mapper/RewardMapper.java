package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.RewardEntity;
import com.pet.businessdomain.shareddto.dto.RewardDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                EnumMapper.class
        })
public interface RewardMapper {

    RewardDto toDto(RewardEntity entity);

    RewardEntity toEntity(RewardDto dto);
}
