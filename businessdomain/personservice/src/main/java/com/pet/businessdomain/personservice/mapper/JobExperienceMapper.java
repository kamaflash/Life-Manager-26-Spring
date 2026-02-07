package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.personservice.dto.JobExperienceDto;
import com.pet.businessdomain.personservice.entities.JobExperienceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobExperienceMapper {
    JobExperienceDto toDto(JobExperienceEntity entity);
    JobExperienceEntity toEntity(JobExperienceDto dto);
}

