package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.JobExperienceEntity;
import com.pet.businessdomain.shareddto.dto.JobExperienceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobExperienceMapper {
    JobExperienceDto toDto(JobExperienceEntity entity);
    JobExperienceEntity toEntity(JobExperienceDto dto);
}

