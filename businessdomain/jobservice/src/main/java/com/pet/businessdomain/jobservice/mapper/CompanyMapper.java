package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import com.pet.businessdomain.shareddto.dto.CompanyDTO;
import com.pet.businessdomain.shareddto.dto.CompanySummaryDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    // ENTITY → DTO
    CompanyDTO toDto(CompanyEntity entity);
    List<CompanyDTO> toDtoList(List<CompanyEntity> entities);

    // ENTITY → SUMMARY DTO
    CompanySummaryDTO toSummaryDto(CompanyEntity entity);
    List<CompanySummaryDTO> toSummaryDtoList(List<CompanyEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "positions", ignore = true)
    CompanyEntity toEntity(CompanyDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "positions", ignore = true)
    void updateEntity(CompanyDTO dto, @MappingTarget CompanyEntity entity);
}