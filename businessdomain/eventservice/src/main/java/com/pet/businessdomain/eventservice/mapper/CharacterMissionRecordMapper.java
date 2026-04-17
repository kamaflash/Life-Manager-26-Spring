package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.CharacterMissionRecord;
import com.pet.businessdomain.shareddto.dto.CharacterMissionRecordResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                EnumMapper.class
        })
public interface CharacterMissionRecordMapper {

    CharacterMissionRecordResponseDto toDto(CharacterMissionRecord entity);

    CharacterMissionRecord toEntity(CharacterMissionRecordResponseDto dto);
}
