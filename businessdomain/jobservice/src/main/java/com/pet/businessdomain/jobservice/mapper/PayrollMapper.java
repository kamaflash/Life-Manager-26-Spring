package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.PayrollEntity;
import com.pet.businessdomain.shareddto.dto.PayrollDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

    // ENTITY → DTO
    PayrollDTO toDto(PayrollEntity entity);
    List<PayrollDTO> toDtoList(List<PayrollEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PayrollEntity toEntity(PayrollDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(PayrollDTO dto, @MappingTarget PayrollEntity entity);
}
