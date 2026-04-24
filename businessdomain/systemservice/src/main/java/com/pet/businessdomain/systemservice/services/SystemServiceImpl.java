/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.*;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.exceptions.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
        return systemRepository.findById(id);
    }

    @Override
    public SystemEntity getSystemByUid(Long uid) {
        return systemRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("System not found for uid: " + uid));
    }

    @Override
    public Optional<SystemEntity> getSystemByUidOP(Long uid) {
        return systemRepository.findByUid(uid);
    }
    @Override
    public SystemDto createSystem(SystemDto systemDto) {
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
        if (!systemRepository.existsById(id)) {
            throw new RuntimeException("System not found with id: " + id);
        }
        systemRepository.deleteById(id);
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
            dto.setProgress(getProgress(2000, dto.getInvestedHours()));
            dto.setInvestedHours(hours + 6);
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
        revisedData(character);

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

        int stressGain = (int) hours;
        // Sumar energía (controlando máximo si quieres)
        int currentEnergy = character.getStats().getEnergy();
        int newEnergy = Math.min(currentEnergy + energyGain, 100);
        character.getStats().setEnergy(newEnergy);

        int currentStress = character.getStats().getStress();
        int newStress = Math.min(currentStress + stressGain, 100);
        character.getStats().setStress(newStress);
        return character;
    }

    private Integer getProgress(Integer durationHours, Integer investedHours) {
        if (durationHours == null || durationHours == 0) {
            return 0;
        }

        double invested = (investedHours != null) ? investedHours : 0;

        return (int) Math.round((invested / durationHours) * 100);
    }
    @Override
    public void revisedData(CharacterDto character) {
        // 1. Obtener todas las solicitudes de becas del personaje
        List<ScholarshipApplicationDto> applications = businessTransactions.getBecas(character.getId());

        if (applications == null || applications.isEmpty()) {
            log.info("No hay solicitudes de becas para el personaje {}", character.getId());
            return;
        }

        // 2. Filtrar solo las solicitudes PENDIENTES
        List<ScholarshipApplicationDto> pendingApplications = applications.stream()
                .filter(app -> app.getStatus() == EnumFormation.ApplicationStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingApplications.isEmpty()) {
            log.info("No hay solicitudes pendientes para el personaje {}", character.getId());
            return;
        }

        log.info("Procesando {} solicitudes de becas pendientes para personaje {}", pendingApplications.size(), character.getId());

        // 3. Procesar cada solicitud pendiente
        for (ScholarshipApplicationDto application : pendingApplications) {
            // Obtener los detalles de la beca
            ScholarshipDto scholarship = businessTransactions.getScholarshipById(application.getScholarshipId());

            if (scholarship == null) {
                log.warn("No se encontró la beca con ID {} para la solicitud {}", application.getScholarshipId(), application.getId());
                continue;
            }

            // 4. Evaluar si el personaje cumple los requisitos
            boolean meetsRequirements = checkScholarshipRequirements(character, scholarship);

            if (meetsRequirements) {
                // APROBAR beca
                approveScholarship(character, application, scholarship);
            } else {
                // RECHAZAR beca
                rejectScholarship(character, application, scholarship);
            }
        }
    }

    /**
     * Verifica si el personaje cumple los requisitos de la beca
     */
    private boolean checkScholarshipRequirements(CharacterDto character, ScholarshipDto scholarship) {
        // Verificar nota mínima
//        if (scholarship.getMinGrade() != null) {
//            double characterGrade = character.getStats() != null ?
//                    character.getStats().getGrade() : 0;
//            if (characterGrade < scholarship.getMinGrade()) {
//                log.info("Personaje {} no cumple nota mínima: {} < {}",
//                        character.getId(), characterGrade, scholarship.getMinGrade());
//                return false;
//            }
//        }

        // Verificar XP mínimo
        if (scholarship.getMinXpRequired() != null) {
            int characterXp = character.getXpAcademy() != null ? character.getXpAcademy() : 0;
            if (characterXp < scholarship.getMinXpRequired()) {
                log.info("Personaje {} no cumple XP mínimo: {} < {}",
                        character.getId(), characterXp, scholarship.getMinXpRequired());
                return false;
            }
        }

        // Verificar nivel educativo
//        if (scholarship.getEducationLevel() != null && character.getEducationLevel() != null) {
//            if (!isEducationLevelValid(character.getEducationLevel(), scholarship.getEducationLevel())) {
//                log.info("Personaje {} no cumple nivel educativo requerido: {} requería {}",
//                        character.getId(), character.getEducationLevel(), scholarship.getEducationLevel());
//                return false;
//            }
//        }

        // Verificar ingreso familiar máximo
        if (scholarship.getMaxFamilyIncome() != null && scholarship.getMaxFamilyIncome().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal familyIncome = character.getAccounts().get(0).getIncomes().get(0).getAmount() != null ? character.getAccounts().get(0).getIncomes().get(0).getAmount() : BigDecimal.valueOf(0);
            if (familyIncome.compareTo(scholarship.getMaxFamilyIncome()) > 0)  {
                log.info("Personaje {} supera ingreso familiar máximo: {} > {}",
                        character.getId(), familyIncome, scholarship.getMaxFamilyIncome());
                return false;
            }
        }

        // Verificar mérito académico (si requiere)
//        if (scholarship.getRequiresMerit() != null && scholarship.getRequiresMerit()) {
//            boolean hasMerit = character.getHasAcademicMerit() != null && character.getHasAcademicMerit();
//            if (!hasMerit) {
//                log.info("Personaje {} no tiene mérito académico requerido", character.getId());
//                return false;
//            }
//        }

        log.info("Personaje {} CUMPLE todos los requisitos para la beca {}", character.getId(), scholarship.getId());
        return true;
    }

    /**
     * Acepta una solicitud de beca y crea el ingreso correspondiente
     */
    private void approveScholarship(CharacterDto character, ScholarshipApplicationDto application, ScholarshipDto scholarship) {
        log.info("Aprobando beca {} para personaje {}", scholarship.getId(), character.getId());

        try {
            // 1. Actualizar estado de la solicitud a APPROVED
            ScholarshipApplicationDto updatedApplication = businessTransactions.updateScholarshipStatus(
                    application.getId(),
                    EnumFormation.ApplicationStatus.APPROVED
            );

            if (updatedApplication == null) {
                log.error("No se pudo actualizar el estado de la solicitud {}", application.getId());
                return;
            }

            // 2. Crear income con la cantidad de la beca
            createScholarshipIncome(character, scholarship);

            // 3. Enviar notificación de aprobación
            sendScholarshipApprovalNotification(character, scholarship, true);

            log.info("Beca {} aprobada y procesada correctamente para personaje {}",
                    scholarship.getId(), character.getId());

        } catch (Exception e) {
            log.error("Error al aprobar beca {} para personaje {}: {}",
                    scholarship.getId(), character.getId(), e.getMessage());
        }
    }

    /**
     * Rechaza una solicitud de beca
     */
    private void rejectScholarship(CharacterDto character, ScholarshipApplicationDto application, ScholarshipDto scholarship) {
        log.info("Rechazando solicitud de beca {}", application.getId());

        try {
            ScholarshipApplicationDto updatedApplication = businessTransactions.updateScholarshipStatus(
                    application.getId(),
                    EnumFormation.ApplicationStatus.REJECTED
            );
// 3. Enviar notificación de aprobación
            sendScholarshipApprovalNotification(character, scholarship,false);
            if (updatedApplication != null) {
                log.info("Solicitud de beca {} rechazada correctamente", application.getId());
            }
        } catch (Exception e) {
            log.error("Error al rechazar solicitud de beca {}: {}", application.getId(), e.getMessage());
        }
    }

    /**
     * Crea un ingreso en la cuenta del personaje por la cantidad de la beca
     */
    private void createScholarshipIncome(CharacterDto character, ScholarshipDto scholarship) {
        try {
            // Obtener la cuenta principal del personaje
            if (character.getAccounts() == null || character.getAccounts().isEmpty()) {
                log.warn("Personaje {} no tiene cuentas asociadas", character.getId());
                return;
            }

            Long accountId = character.getAccounts().get(0).getId();

            // Crear el DTO de ingreso
            SIncomeResponseDto incomeDto = new SIncomeResponseDto();
            incomeDto.setAmount(scholarship.getAmount());
            incomeDto.setExternalRefId(character.getId());
            incomeDto.setExternalRefType("SCHOLARSHIP");
            incomeDto.setActive(true);
            incomeDto.setCategory(EnumAll.ExpenseCategory.SCHOLARSHIP);
            incomeDto.setAmount(scholarship.getAmount());
            incomeDto.setFrequency(EnumAll.Frequency.OTHER);
            incomeDto.setSource("Beca: " + scholarship.getTitle());

            // Llamar al servicio de finanzas para crear el ingreso
            SIncomeResponseDto createdIncome = businessTransactions.setIncome(incomeDto, accountId);

            if (createdIncome != null) {
                log.info("Ingreso de beca creado: {} € para personaje {}", scholarship.getAmount(), character.getId());
            }

        } catch (Exception e) {
            log.error("Error al crear ingreso de beca para personaje {}: {}", character.getId(), e.getMessage());
        }
    }

    /**
     * Envía una notificación al personaje informando que su beca fue aprobada
     */
    private void sendScholarshipApprovalNotification(CharacterDto character, ScholarshipDto scholarship, boolean isAccept) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(character.getId());
        notification.setFromUserId(character.getUid());
        notification.setType(NotificationType.SYSTEM);
        if(isAccept) {
            notification.setTitle("¡Beca Aprobada! 🎉");
            notification.setSubTitle("Tu solicitud para " + scholarship.getTitle() + " ha sido aprobada");
            notification.setMessage(String.format(
                    "¡Felicidades! Tu beca de %s € ha sido aprobada y el dinero ha sido depositado en tu cuenta.",
                    scholarship.getAmount()
            ));
            notification.setEventType(NotificationEventType.SCHOLARSHIP_APPROVED);
        } else {
            notification.setTitle("¡Beca Rechazada! 🎉");
            notification.setSubTitle("Tu solicitud para " + scholarship.getTitle() + " ha sido rechazada");
            notification.setMessage("¡Lo sentimos! Tu beca ha sido rechaza por no cumplir los requisitos.");
            notification.setEventType(NotificationEventType.SCHOLARSHIP_REJECTED);

        }

        notification.setResourceType(NotificationResourceType.COURSE);
        notification.setResourceId(character.getId());
        notification.setActionUrl("/profile");
        notification.setRead(false);

        businessTransactions.setNotifications(notification);
    }

    /**
     * Obtiene el nombre del día en español
     */
    @Override
    public String getDayNameInSpanish(int dayOfWeekNumber) {
        switch (dayOfWeekNumber) {
            case 1: return "Lunes";
            case 2: return "Martes";
            case 3: return "Miércoles";
            case 4: return "Jueves";
            case 5: return "Viernes";
            case 6: return "Sábado";
            case 7: return "Domingo";
            default: return "Desconocido";
        }
    }

    /**
     * Verifica si la fecha está en periodo de vacaciones
     * Vacaciones de verano: del 25 de junio al 10 de septiembre
     */
    @Override
    public boolean isVacationPeriod(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        // Verano: 25 junio (mes 6) hasta 10 septiembre (mes 9)
        if ((month == 6 && day >= 25) ||
                (month == 7) ||
                (month == 8) ||
                (month == 9 && day <= 9)) {
            return true;
        }

        // Navidad: 20 diciembre al 7 enero
        if ((month == 12 && day >= 20) ||
                (month == 1 && day <= 7)) {
            return true;
        }

        return false;
    }

    /**
     * Determina la estación del año según la fecha
     */
    @Override
    public String getSeason(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        // Primavera: 21 marzo - 20 junio
        if ((month == 3 && day >= 21) || month == 4 || month == 5 || (month == 6 && day <= 20)) {
            return "Primavera";
        }
        // Verano: 21 junio - 20 septiembre
        if ((month == 6 && day >= 21) || month == 7 || month == 8 || (month == 9 && day <= 20)) {
            return "Verano";
        }
        // Otoño: 21 septiembre - 20 diciembre
        if ((month == 9 && day >= 21) || month == 10 || month == 11 || (month == 12 && day <= 20)) {
            return "Otoño";
        }
        // Invierno: 21 diciembre - 20 marzo
        return "Invierno";
    }

    /**
     * Genera un clima aleatorio (opcional)
     */
    @Override
    public String getRandomWeather() {
        String[] weathers = {"☀️ Soleado", "⛅ Parcialmente nublado", "☁️ Nublado", "🌧️ Lluvioso", "⛈️ Tormenta", "🌫️ Niebla"};
        Random random = new Random();
        return weathers[random.nextInt(weathers.length)];
    }
}
