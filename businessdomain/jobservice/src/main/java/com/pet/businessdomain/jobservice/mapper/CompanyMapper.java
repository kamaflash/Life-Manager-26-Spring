package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.dto.CompanyDto;
import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    // ENTITY → DTO
    CompanyDto toDto(CompanyEntity entity);
    List<CompanyDto> toDtoList(List<CompanyEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    CompanyEntity toEntity(CompanyDto dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(CompanyDto dto, @MappingTarget CompanyEntity entity);
}
