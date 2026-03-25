package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.entities.ScholarshipApplicationEntity;
import com.pet.businessdomain.shareddto.dto.ScholarshipApplicationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScholarshipApplicationMapper {


    ScholarshipApplicationDto toDto(ScholarshipApplicationEntity entity);

    ScholarshipApplicationEntity toEntity(ScholarshipApplicationDto dto);

    List<ScholarshipApplicationDto> toDtoList(List<ScholarshipApplicationEntity> entities);

    List<ScholarshipApplicationEntity> toEntityList(List<ScholarshipApplicationDto> dtos);
}
