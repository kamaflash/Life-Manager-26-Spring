package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.shareddto.dto.JobVacancyDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RequirementMapper.class})
public interface JobVacancyMapper {

    // ENTITY → DTO
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.title", target = "positionTitle")
    @Mapping(source = "position.company.name", target = "companyName")
    @Mapping(source = "position.company.logoUrl", target = "companyLogo")
    @Mapping(source = "description", target = "description")
    JobVacancyDTO toDto(JobVacancyEntity entity);
    List<JobVacancyDTO> toDtoList(List<JobVacancyEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "openingDate", expression = "java(java.time.LocalDate.now())")
    JobVacancyEntity toEntity(JobVacancyDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "position", ignore = true)
    void updateEntity(JobVacancyDTO dto, @MappingTarget JobVacancyEntity entity);
}