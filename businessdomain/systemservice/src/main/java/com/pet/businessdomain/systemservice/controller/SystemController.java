/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.systemservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.exceptions.BusinessRuleException;

import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.pet.businessdomain.systemservice.services.TimeAdvanceService;
import com.pet.businessdomain.systemservice.transactions.BusinessTransactions;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;
import com.pet.businessdomain.systemservice.services.SystemService;

/**
 *
 * @author Pc
 */
@Slf4j
@RestController
@RequestMapping("/api/systems")
public class SystemController {
    private static final int SIZE = 5;

    @Autowired
    private SystemService systemService;
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private SystemMapper systemMapper;

    @Autowired
    private BusinessTransactions businessTransactions;

    @Autowired
    private TimeAdvanceService timeAdvanceService;
    @GetMapping
    public ResponseEntity<?> getAllSystems(
            @RequestParam(name = "page",defaultValue = "0") int page) {
        int size = 0;
        if(page == 0) {
            size=9;
        } else {
            size = SIZE;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<SystemEntity> systemsPage = systemRepository.findAll(pageable);

        if (systemsPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No existen mascotas");
        }
        List<SystemEntity> petsList = systemsPage.getContent();
        List<SystemDto> listPetDto = systemMapper.toDtoList(petsList);
        Map<String, Object> response = new HashMap<>();
        response.put("systems", listPetDto);
        response.put("currentPage", systemsPage.getNumber());
        response.put("totalItems", systemsPage.getTotalElements());
        response.put("totalPages", systemsPage.getTotalPages());
        response.put("pageSize", systemsPage.getSize());
        response.put("hasNext", systemsPage.hasNext());
        response.put("hasPrevious", systemsPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/uid/{uid}")
    public SystemDto getSystemByUID(@PathVariable(name="uid") Long uid) throws BusinessRuleException {
        Optional<SystemEntity> optSystem = systemService.getSystemByUidOP(uid);
        SystemEntity system = systemMapper.fromOptional(optSystem);
        SystemDto systemDto = systemMapper.toDto(system);
        log.info("systemDto: "+systemDto);

            return systemDto;

    }

    @PostMapping
    public ResponseEntity<?> createSystem(@RequestBody SystemDto systemDto) throws BusinessRuleException, UnknownHostException, MessagingException {
        // Convertir DTO a Entidad
        systemDto = systemService.createSystem(systemDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(systemDto);
    }
//    @PostMapping("/cero_advance")
//    public LocalTime ceroAdvance(@RequestParam(name = "characterId") Long characterId)
//            throws BusinessRuleException, UnknownHostException, MessagingException {
//
//        Optional<SystemEntity> optSystem = systemService.getSystemById(characterId);
//        SystemEntity system = systemMapper.fromOptional(optSystem);
//        SystemDto systemDto = systemMapper.toDto(system);
//
//        CharacterDto characterDto = businessTransactions.getPerson(systemDto.getUid());
//        LocalTime time = systemService.getEducationEndTime(characterDto, false);
//        LocalTime newTime = time.minusMinutes(30);
//
//        // CORRECCIÓN: Obtener solo la hora del LocalDateTime
//        LocalDateTime actualityDateTime = systemDto.getActualityAt();
//        LocalTime actualityAt = actualityDateTime.toLocalTime();
//
//        // Calcular diferencia en minutos considerando el cambio de día
//        long minutesDifference;
//        if (newTime.isBefore(actualityAt)) {
//            minutesDifference = Duration.between(actualityAt, LocalTime.MIDNIGHT).toMinutes()
//                    + Duration.between(LocalTime.MIDNIGHT, newTime).toMinutes();
//        } else {
//            minutesDifference = Duration.between(actualityAt, newTime).toMinutes();
//        }
//
//        long hoursDifference = minutesDifference / 60;
//        long result = hoursDifference * 10;
//        long resultStress = hoursDifference * 2;
//        int absoluteResult = Math.toIntExact(Math.abs(result));
//        int absoluteResultStress = Math.toIntExact(Math.abs(resultStress));
//
//        // Actualizar estadísticas
//        characterDto.getStats().setEnergy(Math.min(100, characterDto.getStats().getEnergy() + absoluteResult));
//        characterDto.getStats().setStress(Math.max(0, characterDto.getStats().getStress() - absoluteResultStress));
//
//        characterDto = businessTransactions.updatePerson(characterDto);
//
//        return newTime;
//    }
//
//    @PostMapping("/firts_advance")
//    public LocalTime firtsAdvance(@RequestParam(name = "characterId") Long characterId) throws BusinessRuleException, UnknownHostException, MessagingException {
//        Optional<SystemEntity> optSystem = systemService.getSystemById(characterId);
//        SystemEntity system = systemMapper.fromOptional(optSystem);
//        SystemDto systemDto = systemMapper.toDto(system);
//
//        CharacterDto characterDto = businessTransactions.getPerson(systemDto.getUid());
//        LocalTime time = systemService.getEducationEndTime(characterDto,false);
//        log.info("systemDto: "+systemDto);
//
//        return time;
//    }
//    @PostMapping("/seconds_advance")
//    public LocalTime secondsAdvance(@RequestParam(name = "characterId") Long characterId, @RequestParam(name = "slim") LocalDateTime slim) throws BusinessRuleException, UnknownHostException, MessagingException {
//        Optional<SystemEntity> optSystem = systemService.getSystemById(characterId);
//        SystemEntity system = systemMapper.fromOptional(optSystem);
//        SystemDto systemDto = systemMapper.toDto(system);
//
//        CharacterDto characterDto = businessTransactions.getPerson(systemDto.getUid());
//        LocalTime time = systemService.getEducationEndTime(characterDto,true);
//        characterDto = systemService.setTimeSlim(characterDto, slim);
//        systemService.updateCharacter(characterDto);
//        system = systemService.plusSystems(characterDto,time);
//        system.setPa(5);
//        systemRepository.save(system);
//        return time;
//    }
//
//    @PostMapping("/weeckend")
//    public void weeckendAdvance(@RequestParam(name = "characterId") Long characterId, @RequestParam(name = "slim") LocalDateTime slim) throws BusinessRuleException, UnknownHostException, MessagingException {
//        Optional<SystemEntity> optSystem = systemService.getSystemById(characterId);
//        SystemEntity system = systemMapper.fromOptional(optSystem);
//        SystemDto systemDto = systemMapper.toDto(system);
//
//        CharacterDto characterDto = businessTransactions.getPerson(systemDto.getUid());
//        LocalDateTime actuality = system.getActualityAt();
//        LocalTime at8 = LocalTime.from(LocalDateTime.of(actuality.toLocalDate(), LocalTime.of(10, 0)));
//        system = systemService.plusSystems(characterDto,at8);
//        system.setPa(5);
//        systemRepository.save(system);
//
//        LocalDateTime actualityDateTime = systemDto.getActualityAt();
//        LocalTime actualityAt = actualityDateTime.toLocalTime();
//
//        // Calcular diferencia en minutos considerando el cambio de día
//        long minutesDifference;
//        if (at8.isBefore(actualityAt)) {
//            minutesDifference = Duration.between(actualityAt, LocalTime.MIDNIGHT).toMinutes()
//                    + Duration.between(LocalTime.MIDNIGHT, at8).toMinutes();
//        } else {
//            minutesDifference = Duration.between(actualityAt, at8).toMinutes();
//        }
//
//        long hoursDifference = minutesDifference / 60;
//        long result = hoursDifference * 10;
//        long resultStress = hoursDifference * 2;
//        int absoluteResult = Math.toIntExact(Math.abs(result));
//        int absoluteResultStress = Math.toIntExact(Math.abs(resultStress));
//
//        // Actualizar estadísticas
//        characterDto.getStats().setEnergy(Math.min(100, characterDto.getStats().getEnergy() + absoluteResult));
//        if(characterDto.getStats().getStress() > 60){
//            characterDto.getStats().setStress(50);
//        } else {
//            characterDto.getStats().setStress(20);
//        }
//
//        characterDto = businessTransactions.updatePerson(characterDto);
//
//    }
    @GetMapping("/processed-data")
    public void processedAdvance(@RequestParam(name = "characterId") Long characterId) throws BusinessRuleException, UnknownHostException, MessagingException {
        Optional<SystemEntity> optSystem = systemService.getSystemById(characterId);
        SystemEntity system = systemMapper.fromOptional(optSystem);
        SystemDto systemDto = systemMapper.toDto(system);

        CharacterDto characterDto = businessTransactions.getPerson(systemDto.getUid());

        Map<String, Object> response2 = businessTransactions.processPendingInterviews(characterDto.getId());
        List<JobApplicationDTO> jobApplicationDTOS = businessTransactions.getApplicationsByCharacterAndStatus(characterDto.getId(), EnumAll.ApplicationStatus.OFFERED);
        if(!jobApplicationDTOS.isEmpty()) {
            List<JobContractDTO> generatedContracts = jobApplicationDTOS.stream()
                    .map(application -> {
                        try {
                            return businessTransactions.generateContract(application.getId());
                        } catch (Exception e) {
                            log.error("Error generating contract for application {}: {}", application.getId(), e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            log.info("Generated {} contracts out of {} applications", generatedContracts.size(), jobApplicationDTOS.size());
        }
        Map<String, Object> response = businessTransactions.processedAdvance(characterDto.getId(), 70);



    }
    @PostMapping("/post")
    public SystemDto createSystemPost(@RequestBody SystemDto systemDto) throws BusinessRuleException, UnknownHostException, MessagingException {
        systemDto = systemService.createSystem(systemDto);

        return systemDto;
    }


    @PutMapping("/{id}/{pa}")
    public ResponseEntity<?> updateSystem(@PathVariable(name="id") Long id, @PathVariable(name="pa") Integer pa, @RequestBody LocalDateTime actualityAt) throws BusinessRuleException {
        if (actualityAt != null) {
            SystemDto dto = systemService.updateSystem(id, actualityAt, pa);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No es aceptable");
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        systemRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hecho");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSystem(@PathVariable(name = "id") Long id) {
        Optional<SystemEntity> find = systemRepository.findById(id);
        if (find.isPresent()) {
            SystemDto systemDto = systemMapper.toDto(find.get());
            systemRepository.delete(find.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(systemDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No es aceptable");
        }
    }

    /**
     * Endpoint unificado para avanzar el tiempo
     * POST /api/systems/advance
     */
    @PostMapping("/advance")
    public ResponseEntity<TimeAdvanceResponseDTO> advance(@RequestBody TimeAdvanceRequestDTO request) {
        log.info("POST /api/systems/advance - Tipo: {} - Personaje: {}",
                request.getAdvanceType(), request.getCharacterId());

        TimeAdvanceResponseDTO response = timeAdvanceService.advance(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint legacy - mantener por compatibilidad
     */
    @PostMapping("/cero_advance")
    public LocalTime ceroAdvancea(@RequestParam(name = "characterId") Long characterId) {
        TimeAdvanceRequestDTO request = new TimeAdvanceRequestDTO();
        request.setCharacterId(characterId);
        request.setAdvanceType(EnumSystems.AdvanceType.NORMAL_DAY);
        TimeAdvanceResponseDTO response = timeAdvanceService.advance(request);

        return response.getNewActualityAt().toLocalTime();
    }

    @PostMapping("/weeckend")
    public void weeckendAdvance(@RequestParam(name = "characterId") Long characterId) {
        TimeAdvanceRequestDTO request = new TimeAdvanceRequestDTO();
        request.setCharacterId(characterId);
        request.setAdvanceType(EnumSystems.AdvanceType.WEEKEND);
        timeAdvanceService.advance(request);
    }
    /**
     * Obtiene información del día actual para un personaje
     * GET /api/systems/day-info/{characterId}
     */
    @GetMapping("/day-info/{characterId}")
    public ResponseEntity<DayInfoDTO> getDayInfo(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/systems/day-info/{} - Obteniendo información del día", characterId);

        // Obtener el sistema del personaje
        Optional<SystemEntity> optSystem = systemService.getSystemById(characterId);
        if (optSystem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SystemEntity system = optSystem.get();
        LocalDateTime actualityAt = system.getActualityAt();
        LocalDate currentDate = actualityAt.toLocalDate();

        // 🔥 Determinar el día de la semana
        java.time.DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        int dayOfWeekNumber = dayOfWeek.getValue(); // 1=Lunes, 7=Domingo

        // 🔥 CORRECCIÓN: Viernes (5), Sábado (6), Domingo (7) son fin de semana
        boolean isWeekend = dayOfWeekNumber == 5 || dayOfWeekNumber == 6;

        // Obtener nombre del día en español
        String dayName = systemService.getDayNameInSpanish(dayOfWeekNumber);

        // Verificar si es vacaciones (ejemplo: del 25 junio al 10 septiembre)
        boolean isVacation = systemService.isVacationPeriod(currentDate);

        // Determinar la estación del año
        String season = systemService.getSeason(currentDate);

        // Determinar el clima (opcional, puedes poner uno fijo o aleatorio)
        String weather = systemService.getRandomWeather();

        // ✨ NUEVA LÓGICA: Información de fin de mes
        int currentDay = currentDate.getDayOfMonth();
        int daysInMonth = currentDate.lengthOfMonth();
        boolean isLastDayOfMonth = currentDay == daysInMonth;
        boolean isLastWeekOfMonth = currentDay >= (daysInMonth - 6);
        int daysUntilEndOfMonth = daysInMonth - currentDay;

        // ✨ Para los últimos 3 días del mes (más específico)
        boolean isLastThreeDays = currentDay >= (daysInMonth - 2);

        // ✨ Porcentaje del mes completado
        double monthProgress = (double) currentDay / daysInMonth * 100;

        log.info("📅 Información del día: {} - ¿Fin de semana? {} - Día {}/{} - Restan {} días para fin de mes",
                dayName, isWeekend, currentDay, daysInMonth, daysUntilEndOfMonth);

        DayInfoDTO dayInfo = DayInfoDTO.builder()
                .dayName(dayName)
                .dayOfWeek(dayOfWeekNumber)
                .isWeekend(isWeekend)
                .isVacation(isVacation)
                .isHoliday(false) // Puedes implementar festivos si quieres
                .season(season)
                .weather(weather)
                // ✨ Añadir nuevos campos
                .currentDay(currentDay)
                .currentMonth(currentDate.getMonthValue())
                .currentYear(currentDate.getYear())
                .daysInMonth(daysInMonth)
                .isLastDayOfMonth(isLastDayOfMonth)
                .isLastWeekOfMonth(isLastWeekOfMonth)
                .isLastThreeDays(isLastThreeDays)
                .daysUntilEndOfMonth(daysUntilEndOfMonth)
                .monthProgress(monthProgress)
                .build();

        return ResponseEntity.ok(dayInfo);
    }
}
