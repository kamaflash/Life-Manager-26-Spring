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
    public SystemEntity getSystemByUid(Long uid) {
        Optional<SystemEntity> opt = systemRepository.findByUid(uid);
        return systemMapper.fromOptional(opt);

    }
    @Override
    public Optional<SystemEntity> getSystemByUidOP(Long uid) {
        Optional<SystemEntity> opt = systemRepository.findByUid(uid);
        return opt;

    }
    @Override
    public SystemDto createSystem(SystemDto systemDto) {
        systemDto.setCreatedAt(LocalDateTime.now());
        systemDto.setPa(5);
        SystemEntity system = systemMapper.toEntity(systemDto);
        system = systemRepository.save(system);
        return systemMapper.toDto(system);
    }

    @Override
    public SystemDto updateSystem(Long id, LocalDateTime localDateTime, Integer pa) throws BusinessRuleException {

        log.info("Buscando System con ID: {}", id);

        SystemEntity system = systemRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException(
                        "0002",
                        "Error validación. Systema no encontrada.",
                        HttpStatus.PRECONDITION_FAILED
                ));


        // ===== DATOS PERSONALES =====
        system.setActualityAt(localDateTime);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);
        system.setPa(pa != null ? system.getPa() - pa : system.getPa());
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
    public double getEducationHours(CharacterDto character, boolean exit) {
        if (character.getEducation() == null || character.getEducation().isEmpty()) {
            return 0.0;
        }

        Map<Long, Double> educationBaseHours = Map.of(
                104L, 8.0
                // aquí puedes añadir más formaciones
        );

        if(exit) {
            educationBaseHours = Map.of(
                    104L, 15.0
                    // aquí puedes añadir más formaciones
            );
        }
        double transportModifier = getTransportModifier(character);

        for (CharacterTrainingDto edu : character.getEducation()) {
            Long trainingId = edu.getTrainingId();

            if (educationBaseHours.containsKey(trainingId)) {
                return educationBaseHours.get(trainingId) - transportModifier;
            }
        }

        return 0.0;
    }
    @Override
    public LocalTime getEducationEndTime(CharacterDto character, boolean exit) {
        double hours = getEducationHours(character,exit);

        if (hours <= 0) {
            return null;
        }

        int hourPart = (int) hours; // Parte entera de las horas
        int minutePart = (int) ((hours - hourPart) * 60); // Convertimos la fracción a minutos




        return LocalTime.of(hourPart, minutePart);
    }

    @Override
    public void updateCharacter(CharacterDto character) {

        // EDUCATION
        if (character.getEducation() != null && !character.getEducation().isEmpty()) {

            var edu = character.getEducation().get(0);

            int hours = edu.getInvestedHours() != null ? edu.getInvestedHours() : 0;
            int progress = edu.getProgress() != null ? edu.getProgress() : 0;
            CharacterTrainingDto dto = businessTransactions.getTrainning(edu.getCharacterId(),edu.getTrainingId());
            dto.setInvestedHours(hours + 6);
            dto.setProgress(progress + 1);
            dto = businessTransactions.updateAppTrainning(dto);

        }

        // STATS
        if (character.getStats() != null) {

            var stats = character.getStats();

            int stress =  stats.getStress();
            int happiness = stats.getHappiness();
            int energy = stats.getEnergy();

            stats.setStress(Math.min(100, stress + 5));
            stats.setHappiness(Math.max(0, happiness - 5));
            stats.setEnergy(Math.max(0, energy - 30));
        }

        character = businessTransactions.updatePerson(character);
    }
    @Override
    public SystemEntity plusSystems(CharacterDto character, LocalTime hours) {
        // Obtenemos el SystemEntity y el DTO
        SystemEntity system = getSystemByUid(character.getUid());
        // Fecha/hora actual
        LocalDateTime current = system.getActualityAt();

        // Sumamos 1 día y ajustamos la hora y minuto según LocalTime
        LocalDateTime newActuality = current.plusDays(1)  // suma 1 día
                .withHour(hours.getHour())   // ajusta la hora
                .withMinute(hours.getMinute()) // ajusta los minutos
                .withSecond(0)
                .withNano(0);

        system.setActualityAt(newActuality);

        return system;
    }

    @Override
    public CharacterDto setTimeSlim(CharacterDto character, LocalDateTime slim) {

        // Hora objetivo (08:00)
        LocalDateTime nextWorkTime = slim.withHour(8).withMinute(0).withSecond(0).withNano(0);

        // Si ya pasó de las 08:00 → ir al siguiente día
        if (!slim.isBefore(nextWorkTime)) {
            nextWorkTime = nextWorkTime.plusDays(1);
        }

        // Calcular horas entre slim y siguiente 08:00
        long hours = java.time.Duration.between(slim, nextWorkTime).toHours();

        // Energía ganada (10 por hora)
        int energyGain = (int) hours * 10;
        // Estres Perdida (2 por hora)

        int stressGain = (int) hours * 2;
        // Sumar energía (controlando máximo si quieres)
        int currentEnergy = character.getStats().getEnergy();
        int newEnergy = Math.min(currentEnergy + energyGain, 100);
        character.getStats().setEnergy(newEnergy);

        int currentStress = character.getStats().getStress();
        int newStress = Math.min(currentStress - stressGain, 100);
        character.getStats().setEnergy(newEnergy);
        return character;
    }
}
