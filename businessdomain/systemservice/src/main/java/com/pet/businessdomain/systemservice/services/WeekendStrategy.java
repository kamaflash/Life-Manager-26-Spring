package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;
import com.pet.businessdomain.systemservice.transactions.BusinessTransactions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeekendStrategy implements AdvanceStrategy {

    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final SystemService systemService;
    private final BusinessTransactions businessTransactions;
    private final EnergyCalculator energyCalculator;
    private final TimeCalculator timeCalculator;

    @Override
    public TimeAdvanceResponseDTO execute(TimeAdvanceRequestDTO request) {
        log.info("Ejecutando estrategia de fin de semana para personaje: {}", request.getCharacterId());

        List<DayEventDTO> events = new ArrayList<>();

        // 1. Obtener sistema y personaje
        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        CharacterDto character = businessTransactions.getPerson(systemDto.getUid());

        // Hora de despertar en fin de semana (10:00 AM)
        LocalTime weekendWakeTime = LocalTime.of(10, 0);
        LocalTime currentTime = systemDto.getActualityAt().toLocalTime();

        log.info("Hora actual: {}, Hora despertar fin semana: {}", currentTime, weekendWakeTime);

        // Calcular diferencia
        long minutesDiff = timeCalculator.calculateMinutesDifference(currentTime, weekendWakeTime);
        long hoursDiff = minutesDiff / 60;

        log.info("Minutos de diferencia: {}, Horas: {}", minutesDiff, hoursDiff);

        // Beneficios del fin de semana
        int energyGain = energyCalculator.calculateForRest(hoursDiff);

        events.add(DayEventDTO.builder()
                .type("sleep_in")
                .title("😴 Dormir hasta tarde")
                .description(String.format("Has descansado %d horas", hoursDiff))
                .energyGain(energyGain)
                .build());

        // Actualizar energía
        int newEnergy = Math.min(100, character.getStats().getEnergy() + energyGain);
        character.getStats().setEnergy(newEnergy);

        // Reset estrés según nivel actual
        int currentStress = character.getStats().getStress();
        int newStress;
        if (currentStress > 60) {
            newStress = 50;
            events.add(DayEventDTO.builder()
                    .type("stress_reset")
                    .title("😌 Reducción de estrés")
                    .description(String.format("Estrés reducido de %d%% a 50%%", currentStress))
                    .stressReduction(currentStress - newStress)
                    .build());
        } else {
            newStress = 20;
            events.add(DayEventDTO.builder()
                    .type("stress_reset")
                    .title("😊 Gran reducción de estrés")
                    .description(String.format("Estrés reducido de %d%% a 20%%", currentStress))
                    .stressReduction(currentStress - newStress)
                    .build());
        }
        character.getStats().setStress(newStress);

        // Guardar cambios del personaje
        businessTransactions.updatePerson(character);

        events.add(DayEventDTO.builder()
                .type("stats")
                .title("📊 Estadísticas actualizadas")
                .description(String.format("Energía: %d%% → %d%% | Estrés: %d%% → %d%%",
                        character.getStats().getEnergy() - energyGain, newEnergy,
                        currentStress, newStress))
                .energyGain(energyGain)
                .stressReduction(currentStress - newStress)
                .build());

        // 🔥 ACTUALIZAR SISTEMA
        LocalDateTime newActuality = systemDto.getActualityAt()
                .withHour(weekendWakeTime.getHour())
                .withMinute(weekendWakeTime.getMinute())
                .withSecond(0)
                .withNano(0);

        // Si la nueva hora es anterior a la actual (pasó de día), sumar un día
        if (newActuality.isBefore(systemDto.getActualityAt())) {
            newActuality = newActuality.plusDays(1);
            log.info("Se cruzó de día, nueva fecha: {}", newActuality);
        }

        // Actualizar sistema
        system.setActualityAt(newActuality);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);
        system.setPa(5);

        systemRepository.save(system);

        log.info("Sistema actualizado - Nueva fecha: {}, PA restantes: {}", newActuality, system.getPa());

        events.add(DayEventDTO.builder()
                .type("system")
                .title("⏰ Tiempo avanzado")
                .description(String.format("Nueva fecha: %s", newActuality))
                .build());

        // Procesar datos pendientes
        processPendingData(character);

        return TimeAdvanceResponseDTO.builder()
                .success(true)
                .message("Fin de semana completado. Has descansado y recuperado energía.")
                .newActualityAt(newActuality)
                .paRemaining(system.getPa())
                .energyChange(energyGain)
                .stressChange(-(currentStress - newStress))
                .events(events)
                .build();
    }

    private void processPendingData(CharacterDto character) {
        try {
            log.info("Procesando datos pendientes para personaje: {}", character.getId());
            businessTransactions.processPendingInterviews(character.getId());
            businessTransactions.processedAdvance(character.getId(), 70);
        } catch (Exception e) {
            log.error("Error procesando datos pendientes: {}", e.getMessage());
        }
    }

    @Override
    public EnumSystems.AdvanceType getType() {
        return EnumSystems.AdvanceType.WEEKEND;
    }
}
