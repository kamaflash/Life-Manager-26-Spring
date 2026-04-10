package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.JobEventEntity;
import com.pet.businessdomain.shareddto.dto.JobEventDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobEventMapper {

    // ENTITY → DTO
    JobEventDTO toDto(JobEventEntity entity);
    List<JobEventDTO> toDtoList(List<JobEventEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterJobId", ignore = true)
    @Mapping(target = "occurredAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "resolved", constant = "false")
    JobEventEntity toEntity(JobEventDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterJobId", ignore = true)
    void updateEntity(JobEventDTO dto, @MappingTarget JobEventEntity entity);
}