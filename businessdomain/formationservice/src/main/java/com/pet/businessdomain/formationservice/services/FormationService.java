/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.dto.FormationDto;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import java.util.List;
import java.util.Optional;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;

/**
 *
 * @author Pc
 */

public interface FormationService {
    FormationDto createFormation(FormationDto formationDto) throws BusinessRuleException;

    FormationDto updateFormation(Long id, FormationDto formationDto) throws BusinessRuleException;

    FormationDto getFormationById(Long id) throws BusinessRuleException;

    FormationDto getFormationByCode(String code) throws BusinessRuleException;

    List<FormationDto> getAvailableFormations(
            Enum.EducationLevel educationLevel,
            Integer academicLevel,
            Integer academicXp,
            Enum.CareerInterest careerInterest
    );

    void deactivateFormation(Long id) throws BusinessRuleException;

}
