package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.personservice.dto.EducationExperienceDto;
import com.pet.businessdomain.personservice.entities.EducationExperienceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EducationExperienceMapper {
    EducationExperienceDto toDto(EducationExperienceEntity entity);
    EducationExperienceEntity toEntity(EducationExperienceDto dto);
}
