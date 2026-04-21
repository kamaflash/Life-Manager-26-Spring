/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.SystemDto;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.exceptions.BusinessRuleException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author Pc
 */

public interface SystemService {
    List<SystemDto> getAllSystems();
    Optional<SystemEntity> getSystemById(Long id);
    Optional<SystemEntity> getSystemByUidOP(Long id);
    SystemEntity getSystemByUid(Long uid);
    SystemDto createSystem(SystemDto system);
    SystemDto updateSystem(Long id, LocalDateTime localDateTime, Integer pa) throws BusinessRuleException;
    void deleteSystem(Long id);
    double getTransportModifier(CharacterDto character);
    double getEducationHours(CharacterDto character, boolean exit);
    LocalTime getEducationEndTime(CharacterDto character, boolean exit);
    void updateCharacter(CharacterDto character);
    SystemEntity plusSystems(CharacterDto character, LocalTime hours);
    CharacterDto setTimeSlim(CharacterDto character, LocalDateTime slim);
    public String getRandomWeather();
    String getSeason(LocalDate date);
    boolean isVacationPeriod(LocalDate date);
    String getDayNameInSpanish(int dayOfWeekNumber);
}
