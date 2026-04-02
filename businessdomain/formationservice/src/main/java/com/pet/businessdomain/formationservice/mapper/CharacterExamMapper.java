package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.shareddto.dto.CharacterExamDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CharacterExamMapper {

    CharacterExamDto toDto(CharacterExam entity);

    CharacterExam toEntity(CharacterExamDto dto);

    List<CharacterExamDto> toDtoList(List<CharacterExam> entities);

    List<CharacterExam> toEntityList(List<CharacterExamDto> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CharacterExamDto dto, @MappingTarget CharacterExam entity);
}
