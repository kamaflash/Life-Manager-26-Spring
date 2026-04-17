package com.pet.businessdomain.eventservice.mapper;

import com.pet.businessdomain.eventservice.entities.CharacterEventRecord;
import com.pet.businessdomain.shareddto.dto.CharacterEventRecordResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                EnumMapper.class
        })
public interface CharacterEventRecordMapper {

    CharacterEventRecordResponseDto toDto(CharacterEventRecord entity);

    CharacterEventRecord toEntity(CharacterEventRecordResponseDto dto);
}
