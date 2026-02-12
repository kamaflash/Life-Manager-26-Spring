package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.dto.JobExperienceDto;
import com.pet.businessdomain.jobservice.entities.JobExperienceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobExperienceMapper {
    JobExperienceDto toDto(JobExperienceEntity entity);
    JobExperienceEntity toEntity(JobExperienceDto dto);
}

