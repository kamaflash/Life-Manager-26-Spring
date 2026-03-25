package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.shareddto.dto.JobVacancyDto;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface JobVacancyMapper {

    // ENTITY → DTO
    @Mapping(target = "positionId", source = "position.id")
    JobVacancyDto toDto(JobVacancyEntity entity);
    List<JobVacancyDto> toDtoList(List<JobVacancyEntity> entities);


    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "position", source = "positionId")
    JobVacancyEntity toEntity(JobVacancyDto dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "position", source = "positionId")
    void updateEntity(JobVacancyDto dto, @MappingTarget JobVacancyEntity entity);

    // MAPPING MANUAL positionId → JobPositionEntity
    default JobPositionEntity map(Long positionId) {
        if (positionId == null) return null;
        JobPositionEntity position = new JobPositionEntity();
        position.setId(positionId);
        return position;
    }

    default List<JobVacancyEntity> toEntityList(List<JobVacancyDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toEntity).toList();
    }
}
