package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.shareddto.dto.JobPositionDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {JobVacancyMapper.class})
public interface JobPositionMapper {

    // ENTITY → DTO
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    JobPositionDTO toDto(JobPositionEntity entity);
    List<JobPositionDTO> toDtoList(List<JobPositionEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "vacancies", ignore = true)
    @Mapping(target = "active", constant = "true")
    JobPositionEntity toEntity(JobPositionDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "vacancies", ignore = true)
    void updateEntity(JobPositionDTO dto, @MappingTarget JobPositionEntity entity);
}