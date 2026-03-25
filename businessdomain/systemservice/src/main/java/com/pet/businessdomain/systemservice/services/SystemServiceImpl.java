/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.exceptions.BusinessRuleException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.pet.businessdomain.systemservice.transactions.BusinessTransactions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;

/**
 *
 * @author Pc
 */
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private SystemMapper systemMapper;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private BusinessTransactions businessTransactions;
    // Add any required dependencies here (e.g., repositories, mappers)

    @Override
    public List<SystemDto> getAllSystems() {
        List<SystemDto> listSystemDto = systemMapper.toDtoList(systemRepository.findAll());

        return listSystemDto;
    }

    @Override
    public Optional<SystemEntity> getSystemById(Long id) {
        Optional<SystemEntity> opt = systemRepository.findById(id);
        return opt;
    }

    @Override
    public Optional<SystemEntity> getSystemByUid(Long uid) {
        Optional<SystemEntity> opt = systemRepository.findByUid(uid);
        return opt;

    }

    @Override
    public SystemDto createSystem(SystemDto systemDto) {
        systemDto.setCreatedAt(LocalDateTime.now());
        SystemEntity system = systemMapper.toEntity(systemDto);
        system = systemRepository.save(system);
        return systemMapper.toDto(system);
    }

    @Override
    public SystemDto updateSystem(Long id, SystemDto systemDto) throws BusinessRuleException {

        log.info("Buscando System con ID: {}", id);

        SystemEntity system = systemRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException(
                        "0002",
                        "Error validación. Systema no encontrada.",
                        HttpStatus.PRECONDITION_FAILED
                ));


        // ===== DATOS PERSONALES =====
        system.setActualityAt(systemDto.getActualityAt());
        system.setVeces(system.getVeces() + 1);
        CharacterDto characterDto = businessTransactions.getPerson(system.getUid());



        try {
            SystemEntity saved = systemRepository.save(system);
            return systemMapper.toDto(saved);

        } catch (Exception e) {
            log.error("Error al actualizar System", e);
            throw new BusinessRuleException(
                    "0003",
                    "Error al guardar los cambios del usuario",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void deleteSystem(Long id) {
        // Implementation here
    }
    @Override
    public double getTransportModifier(CharacterDto character) {
        if (character.getInventory() == null || character.getInventory().isEmpty()) {
            return 1.0;
        }

        Map<Long, Double> modifiers = Map.of(
                6L, 0.40,
                7L, 0.50,
                8L, 0.20,
                9L, 0.15,
                10L, 0.05
        );

        for (CharacterInventoryResponseDTO item : character.getInventory()) {
            Long productId = item.getProductId();

            if (modifiers.containsKey(productId)) {
                return modifiers.get(productId);
            }
        }

        return 1.0;
    }
    @Override
    public double getEducationHours(CharacterDto character) {
        if (character.getEducation() == null || character.getEducation().isEmpty()) {
            return 0.0;
        }

        Map<Long, Double> educationBaseHours = Map.of(
                104L, 8.0
                // aquí puedes añadir más formaciones
        );

        double transportModifier = getTransportModifier(character);

        for (CharacterTrainingDto edu : character.getEducation()) {
            Long trainingId = edu.getTrainingId();

            if (educationBaseHours.containsKey(trainingId)) {
                return educationBaseHours.get(trainingId) - transportModifier;
            }
        }

        return 0.0;
    }

    public LocalTime getEducationEndTime(CharacterDto character) {
        double hours = getEducationHours(character);

        if (hours <= 0) {
            return null;
        }

        LocalTime startTime = LocalTime.of(8, 0); // 08:00
        return startTime.plusMinutes((long) (hours * 60));
    }
}
