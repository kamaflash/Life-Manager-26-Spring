package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.entities.ScholarshipEntity;
import com.pet.businessdomain.shareddto.dto.ScholarshipDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScholarshipMapper {
    ScholarshipMapper INSTANCE = Mappers.getMapper(ScholarshipMapper.class);

    ScholarshipDto toDto(ScholarshipEntity entity);

    ScholarshipEntity toEntity(ScholarshipDto dto);

    List<ScholarshipDto> toDtoList(List<ScholarshipEntity> entities);

    List<ScholarshipEntity> toEntityList(List<ScholarshipDto> dtos);
}
