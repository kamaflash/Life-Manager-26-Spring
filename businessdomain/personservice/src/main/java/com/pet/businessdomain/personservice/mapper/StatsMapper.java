package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.shareddto.dto.StatsDto;
import com.pet.businessdomain.personservice.entities.CharacterStats;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    StatsDto toDto(CharacterStats stats);
    CharacterStats toEntity(StatsDto dto);
}

