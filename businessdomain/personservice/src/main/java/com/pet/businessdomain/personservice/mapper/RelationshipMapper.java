package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.shareddto.dto.RelationshipDto;
import com.pet.businessdomain.personservice.entities.RelationshipEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RelationshipMapper {
    RelationshipDto toDto(RelationshipEntity entity);
    RelationshipEntity toEntity(RelationshipDto dto);
}
