package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.CharacterJobEntity;
import com.pet.businessdomain.shareddto.dto.CharacterJobDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {WorkRelationshipMapper.class, MissionMapper.class})
public interface CharacterJobMapper {

    // ENTITY → DTO
    @Mapping(source = "vacancy.position.title", target = "positionTitle")
    @Mapping(source = "vacancy.position.company.name", target = "companyName")
    @Mapping(source = "vacancy.position.company.logoUrl", target = "companyLogo")
    @Mapping(source = "vacancy.minSalary", target = "currentSalary")
    CharacterJobDTO toDto(CharacterJobEntity entity);
    List<CharacterJobDTO> toDtoList(List<CharacterJobEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vacancy", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "performance", constant = "50")
    @Mapping(target = "satisfaction", constant = "50")
    @Mapping(target = "stressLevel", constant = "30")
    @Mapping(target = "promotionsReceived", constant = "0")
    @Mapping(target = "bonusesReceived", constant = "0")
    CharacterJobEntity toEntity(CharacterJobDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vacancy", ignore = true)
    void updateEntity(CharacterJobDTO dto, @MappingTarget CharacterJobEntity entity);
}