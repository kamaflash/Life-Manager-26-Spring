package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.repository.FormationRepository;
import com.pet.businessdomain.shareddto.dto.FormationDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormationServiceImpl implements FormationService {
    @Autowired
    private final FormationRepository formationRepository;
    @Autowired
    private final FormationMapper formationMapper;

    @Override
    public Formation getById(Long id) {
        return formationRepository.findById(id).orElse(null);
    }

    @Override
    public FormationDto createFormation(FormationDto dto) throws BusinessRuleException {
        if (dto.getCode() == null || dto.getCode().isBlank()) {
            throw new BusinessRuleException("1001", "El código es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (formationRepository.existsByCode(dto.getCode())) {
            throw new BusinessRuleException("1002", "Ya existe una formación con este código", HttpStatus.CONFLICT);
        }
        Formation formation = formationMapper.toEntity(dto);
        formation.setActive(true);
        Formation saved = formationRepository.save(formation);
        return formationMapper.toDto(saved);
    }

    @Override
    public FormationDto updateFormation(Long id, FormationDto dto) throws BusinessRuleException {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("1003", "Formación no encontrada", HttpStatus.BAD_REQUEST));

        formationMapper.updateEntityFromDto(dto, formation); // MapStruct actualizará solo los campos no nulos
        Formation saved = formationRepository.save(formation);
        return formationMapper.toDto(saved);
    }

    @Override
    public void deactivateFormation(Long id) throws BusinessRuleException {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("1004", "Formación no encontrada", HttpStatus.BAD_REQUEST));

        formation.setActive(false);
        formationRepository.save(formation);
    }

    @Override
    public void delete(Long id) {
        formationRepository.deleteById(id);
    }

    @Override
    public List<Formation> getAllActive() {
        return formationRepository.findByActiveTrue();
    }

    @Override
    public List<Formation> getByCategory(String category) {
        return formationRepository.findByCategory(EnumAll.CareerInterest.valueOf(category));
    }

    @Override
    public Formation save(Formation formation) {
        return formationRepository.save(formation);
    }
}