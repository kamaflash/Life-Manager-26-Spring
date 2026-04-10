package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.WorkRelationshipEntity;
import com.pet.businessdomain.shareddto.dto.WorkRelationshipDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkRelationshipMapper {

    // ENTITY → DTO
    WorkRelationshipDTO toDto(WorkRelationshipEntity entity);
    List<WorkRelationshipDTO> toDtoList(List<WorkRelationshipEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterJobId", ignore = true)
    WorkRelationshipEntity toEntity(WorkRelationshipDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterJobId", ignore = true)
    void updateEntity(WorkRelationshipDTO dto, @MappingTarget WorkRelationshipEntity entity);
}