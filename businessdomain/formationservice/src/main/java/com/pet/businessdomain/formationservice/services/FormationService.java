/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.shareddto.dto.FormationDto;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import java.util.List;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;

/**
 *
 * @author Pc
 */

public interface FormationService {
    Formation getById(Long id);

    List<Formation> getAllActive();

    List<Formation> getByCategory(String category);

    Formation save(Formation formation);

    void delete(Long id);
    FormationDto createFormation(FormationDto dto) throws BusinessRuleException;
    FormationDto updateFormation(Long id, FormationDto dto) throws BusinessRuleException;
    void deactivateFormation(Long id) throws BusinessRuleException;
}
