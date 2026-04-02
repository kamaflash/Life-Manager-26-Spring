package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.shareddto.dto.FormationExamDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FormationExamMapper {

    FormationExamDto toDto(FormationExam entity);

    FormationExam toEntity(FormationExamDto dto);

    List<FormationExamDto> toDtoList(List<FormationExam> entities);

    List<FormationExam> toEntityList(List<FormationExamDto> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(FormationExamDto dto, @MappingTarget FormationExam entity);
}
