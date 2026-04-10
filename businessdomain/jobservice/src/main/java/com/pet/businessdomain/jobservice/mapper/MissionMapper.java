package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.MissionEntity;
import com.pet.businessdomain.shareddto.dto.MissionDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    // ENTITY → DTO
    @Mapping(target = "remainingHours", expression = "java(calculateRemainingHours(entity))")
    @Mapping(target = "progress", expression = "java(calculateProgress(entity))")
    MissionDTO toDto(MissionEntity entity);
    List<MissionDTO> toDtoList(List<MissionEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterJobId", ignore = true)
    @Mapping(target = "assignedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "completed", constant = "false")
    MissionEntity toEntity(MissionDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterJobId", ignore = true)
    void updateEntity(MissionDTO dto, @MappingTarget MissionEntity entity);

    // Métodos auxiliares (implementación por defecto)
    default Integer calculateRemainingHours(MissionEntity entity) {
        if (entity.getCompleted() || entity.getCompletedAt() != null) return 0;
        if (entity.getDeadlineHours() == null) return null;

        long hoursPassed = java.time.Duration.between(
                entity.getAssignedAt(),
                java.time.LocalDateTime.now()
        ).toHours();

        return Math.max(0, entity.getDeadlineHours() - (int) hoursPassed);
    }

    default Integer calculateProgress(MissionEntity entity) {
        if (entity.getCompleted()) return 100;
        if (entity.getDeadlineHours() == null || entity.getDeadlineHours() == 0) return 0;

        long hoursPassed = java.time.Duration.between(
                entity.getAssignedAt(),
                java.time.LocalDateTime.now()
        ).toHours();

        return (int) Math.min(100, (hoursPassed * 100) / entity.getDeadlineHours());
    }
}