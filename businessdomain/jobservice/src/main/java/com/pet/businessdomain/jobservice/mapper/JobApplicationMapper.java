package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.JobApplicationEntity;
import com.pet.businessdomain.shareddto.dto.JobApplicationDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    // ENTITY → DTO
    @Mapping(source = "vacancy.position.title", target = "positionTitle")
    @Mapping(source = "vacancy.position.company.name", target = "companyName")
    JobApplicationDTO toDto(JobApplicationEntity entity);
    List<JobApplicationDTO> toDtoList(List<JobApplicationEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vacancy", ignore = true)
    @Mapping(target = "appliedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "stage", constant = "APPLICATION")
    JobApplicationEntity toEntity(JobApplicationDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vacancy", ignore = true)
    void updateEntity(JobApplicationDTO dto, @MappingTarget JobApplicationEntity entity);
}