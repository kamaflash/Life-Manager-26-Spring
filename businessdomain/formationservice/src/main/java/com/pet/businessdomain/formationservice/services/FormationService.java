/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.shareddto.dto.FormationDto;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import java.util.List;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;

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
            EnumAll.EducationLevel educationLevel,
            Integer academicLevel,
            Integer academicXp,
            EnumAll.CareerInterest careerInterest
    );

    void deactivateFormation(Long id) throws BusinessRuleException;

}
