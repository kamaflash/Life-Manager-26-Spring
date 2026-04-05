package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.repository.FormationRepository;
import com.pet.businessdomain.shareddto.dto.FormationDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FormationServiceImpl implements FormationService {
    
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    @Override
    public Formation getById(Long id) {
        log.debug("Buscando formación con ID: {}", id);
        return formationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Formación no encontrada con ID: {}", id);
                    return new RuntimeException("Formación no encontrada con id: " + id);
                });
    }

    @Override
    public FormationDto createFormation(FormationDto dto) throws BusinessRuleException {
        log.info("Creando nueva formación con código: {}", dto != null ? dto.getCode() : "null");
        
        if (dto == null) {
            log.warn("Intento de crear formación con DTO nulo");
            throw new BusinessRuleException("1000", "Datos de formación requeridos", HttpStatus.BAD_REQUEST);
        }
        if (dto.getCode() == null || dto.getCode().isBlank()) {
            log.warn("Intento de crear formación sin código");
            throw new BusinessRuleException("1001", "El código es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (formationRepository.existsByCode(dto.getCode())) {
            log.warn("Ya existe formación con código: {}", dto.getCode());
            throw new BusinessRuleException("1002", "Ya existe una formación con este código", HttpStatus.CONFLICT);
        }

        Formation formation = formationMapper.toEntity(dto);
        formation.setActive(true);
        Formation saved = formationRepository.save(formation);
        log.info("Formación creada exitosamente con ID: {}", saved.getId());
        return formationMapper.toDto(saved);
    }

    @Override
    public FormationDto updateFormation(Long id, FormationDto dto) throws BusinessRuleException {
        log.info("Actualizando formación con ID: {}", id);
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Formación no encontrada para actualizar: {}", id);
                    return new BusinessRuleException("1003", "Formación no encontrada", HttpStatus.NOT_FOUND);
                });

        formationMapper.updateEntityFromDto(dto, formation);
        Formation saved = formationRepository.save(formation);
        log.info("Formación actualizada exitosamente: {}", id);
        return formationMapper.toDto(saved);
    }

    @Override
    public void deactivateFormation(Long id) throws BusinessRuleException {
        log.info("Desactivando formación con ID: {}", id);
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Formación no encontrada para desactivar: {}", id);
                    return new BusinessRuleException("1004", "Formación no encontrada", HttpStatus.NOT_FOUND);
                });

        if (!formation.getActive()) {
            log.warn("Intento de desactivar formación ya desactivada: {}", id);
            throw new BusinessRuleException("1005", "La formación ya está desactivada", HttpStatus.CONFLICT);
        }

        formation.setActive(false);
        formationRepository.save(formation);
        log.info("Formación desactivada exitosamente: {}", id);
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando formación con ID: {}", id);
        formationRepository.deleteById(id);
        log.info("Formación eliminada: {}", id);
    }

    @Override
    public List<Formation> getAllActive() {
        log.debug("Obteniendo todas las formaciones activas");
        return formationRepository.findByActiveTrue();
    }

    @Override
    public List<Formation> getByCategory(String category) {
        log.debug("Obteniendo formaciones por categoría: {}", category);
        return formationRepository.findByCategory(EnumAll.CareerInterest.valueOf(category));
    }

    @Override
    public Formation save(Formation formation) {
        if (formation == null) {
            log.warn("Intento de guardar formación nula");
            throw new IllegalArgumentException("La formación no puede ser nula");
        }
        log.debug("Guardando formación: {}", formation.getId());
        return formationRepository.save(formation);
    }
}