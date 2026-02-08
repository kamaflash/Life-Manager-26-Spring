/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.dto.CharacterTrainingDto;
import com.pet.businessdomain.formationservice.dto.FormationDto;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import java.util.List;
import java.util.Optional;

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
    List<CharacterTrainingDto> toDtoListFull(List<CharacterTraining> formations);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(FormationDto dto, @MappingTarget Formation entity);
}
