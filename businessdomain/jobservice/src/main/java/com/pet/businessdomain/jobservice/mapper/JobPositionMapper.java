package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.dto.JobPositionDto;
import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobPositionMapper {

    // =========================
    // ENTITY → DTO
    // =========================

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    List<JobPositionDto> toDtoList(List<JobPositionEntity> entities);

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    JobPositionDto toDto(JobPositionEntity entity);


    // =========================
    // DTO → ENTITY (CREATE)
    // =========================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", source = "companyId")
    JobPositionEntity toEntity(JobPositionDto dto);


    // =========================
    // DTO → ENTITY (UPDATE)
    // =========================

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", source = "companyId")
    void updateEntity(JobPositionDto dto, @MappingTarget JobPositionEntity entity);


    // =========================
    // Custom mapper Long → CompanyEntity
    // =========================

    default CompanyEntity map(Long companyId) {
        if (companyId == null) return null;
        CompanyEntity company = new CompanyEntity();
        company.setId(companyId);
        return company;
    }

    default List<JobPositionEntity> toEntityList(List<JobPositionDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(this::toEntity).toList();
    }
}
