/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.dto.FormationDto;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.repository.FormationRepository;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
/**
 *
 * @author Pc
 */
@Service
@Slf4j
public class FormationServiceImpl implements FormationService {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    // =========================
    // ➕ CREAR FORMACIÓN
    // =========================
    @Override
    public FormationDto createFormation(FormationDto formationDto) throws BusinessRuleException {

        if (formationDto.getCode() == null || formationDto.getCode().isBlank()) {
            throw new BusinessRuleException(
                    "1001",                               // código de error
                    "El código de la formación es obligatorio",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (formationRepository.existsByCode(formationDto.getCode())) {
            throw new BusinessRuleException(
                    "1002",
                    "Ya existe una formación con ese código",
                    HttpStatus.CONFLICT
            );
        }

        Formation formation = formationMapper.toEntity(formationDto);
        formation.setActive(true);

        Formation saved = formationRepository.save(formation);
        log.info("Formación creada: {}", saved.getCode());

        return formationMapper.toDto(saved);
    }

    // =========================
    // ✏️ ACTUALIZAR FORMACIÓN
    // =========================
    @Override
    public FormationDto updateFormation(Long id, FormationDto formationDto) throws BusinessRuleException {

        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException(
                        "1001",                               // código de error
                        "La formación no existe",
                        HttpStatus.BAD_REQUEST
                ));

        formationMapper.updateEntityFromDto(formationDto, formation);

        Formation updated = formationRepository.save(formation);
        log.info("Formación actualizada: {}", updated.getId());

        return formationMapper.toDto(updated);
    }

    // =========================
    // 🔍 OBTENER POR ID
    // =========================
    @Override
    public FormationDto getFormationById(Long id) throws BusinessRuleException {

        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException(
                        "1001",                               // código de error
                        "La formación no existe",
                        HttpStatus.BAD_REQUEST
                ));

        return formationMapper.toDto(formation);
    }

    // =========================
    // 🔍 OBTENER POR CÓDIGO
    // =========================
    @Override
    public FormationDto getFormationByCode(String code) throws BusinessRuleException {

        Formation formation = formationRepository.findByCode(code)
                .orElseThrow(() -> new BusinessRuleException(
                        "1001",                               // código de error
                        "La formación no existe",
                        HttpStatus.BAD_REQUEST
                ));

        return formationMapper.toDto(formation);
    }

    // =========================
    // 🔓 FORMACIONES DISPONIBLES
    // =========================
    @Override
    public List<FormationDto> getAvailableFormations(
            Enum.EducationLevel educationLevel,
            Integer academicLevel,
            Integer academicXp,
            Enum.CareerInterest careerInterest
    ) {

        List<Formation> formations = formationRepository.findAvailableFormations(
                educationLevel,
                academicLevel,
                academicXp,
                careerInterest
        );

        return formationMapper.toDtoList(formations);
    }

    // =========================
    // ❌ DESACTIVAR FORMACIÓN
    // =========================
    @Override
    public void deactivateFormation(Long id) throws BusinessRuleException {

        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException(
                        "1001",                               // código de error
                        "La formación no existe",
                        HttpStatus.BAD_REQUEST
                ));

        formation.setActive(false);
        formationRepository.save(formation);

        log.info("Formación desactivada: {}", id);
    }
}
