package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.MissionEntity;
import com.pet.businessdomain.shareddto.dto.MissionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                ObjectiveMapper.class,
                RewardMapper.class,
                MissionRequirementMapper.class,
                EnumMapper.class
        }
)
public interface MissionMapper {

    MissionResponseDto toDto(MissionEntity entity);

    @Mapping(target = "id", ignore = true)  // ✅ Ignorar ID al mapear a entidad
    @Mapping(target = "createdAt", ignore = true)
    MissionEntity toEntity(MissionResponseDto dto);
}
