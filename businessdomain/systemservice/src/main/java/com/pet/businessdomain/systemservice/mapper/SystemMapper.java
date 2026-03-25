package com.pet.businessdomain.systemservice.mapper;

import com.pet.businessdomain.shareddto.dto.SystemDto;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemMapper {

    // ===== ENTITY -> DTO (LECTURA) =====
    SystemDto toDto(SystemEntity system);

    List<SystemDto> toDtoList(List<SystemEntity> systems);

    // ===== DTO -> ENTITY (USO CONTROLADO) =====
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SystemEntity toEntity(SystemDto systemDto);

    // ===== UPDATE PARCIAL (MUY IMPORTANTE) =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(SystemDto dto, @MappingTarget SystemEntity entity);

    // ===== OPTIONAL SUPPORT =====
    default SystemEntity fromOptional(Optional<SystemEntity> opt) {
        return opt.orElse(null);
    }

}
