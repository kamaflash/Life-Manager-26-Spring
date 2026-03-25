/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.entities.CharacterStats;
import com.pet.businessdomain.shareddto.dto.CharacterStatsDto;
import com.pet.businessdomain.shareddto.dto.CharacterTrainingDto;
import com.pet.businessdomain.shareddto.dto.FormationDto;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import java.util.List;

import org.mapstruct.*;

/**
 *
 * @author Pc
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FormationMapper {
    FormationDto toDto(Formation formation);
    Formation toEntity(FormationDto formationDto);
    List<FormationDto> toDtoList(List<Formation> formations);
    List<CharacterTraining> toDtoListT(List<CharacterTraining> formations);
    List<CharacterTrainingDto> toDtoListFull(List<CharacterTraining> formations);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(FormationDto dto, @MappingTarget Formation entity);

    // 🔴 AÑADE ESTO
    CharacterStatsDto map(CharacterStats stats);

    CharacterStats map(CharacterStatsDto statsDto);
}
