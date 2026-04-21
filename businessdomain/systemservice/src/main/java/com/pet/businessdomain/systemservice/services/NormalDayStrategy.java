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
public class NormalDayStrategy implements AdvanceStrategy {
    public static final LocalTime SCHOOL_START_TIME = LocalTime.of(8, 0);  // 08:00 - Inicio de clases
    public static final LocalTime SCHOOL_END_TIME = LocalTime.of(14, 0);   // 14:00 - Fin de clases
    public static final int BASE_HOURS_PER_DAY = 6;
    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final SystemService systemService;
    private final BusinessTransactions businessTransactions;
    private final EnergyCalculator energyCalculator;
    private final StressCalculator stressCalculator;
    private final TimeCalculator timeCalculator;

    @Override
    public TimeAdvanceResponseDTO execute(TimeAdvanceRequestDTO request) {
        log.info("=== INICIANDO AVANCE DE DÍA NORMAL ===");
        log.info("Personaje ID: {}", request.getCharacterId());

        List<DayEventDTO> events = new ArrayList<>();

        // 1. Obtener sistema y personaje
        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        CharacterDto character = businessTransactions.getPerson(systemDto.getUid());

        LocalDateTime currentDateTime = systemDto.getActualityAt();
        log.info("📅 Fecha/Hora actual: {}", currentDateTime);

        // 2. Calcular horas de descanso (desde ahora hasta inicio de clases)
        long restHours = timeCalculator.calculateRestHours(currentDateTime);
        log.info("😴 Horas de descanso hasta inicio de clases: {} horas", restHours);

        // 3. Obtener minutos de viaje según vehículo
        int travelMinutes = timeCalculator.getTravelTimeMinutes(character);
        log.info("🚗 Tiempo de viaje: {} minutos", travelMinutes);

        // 4. Calcular hora de salida y llegada según vehículo
        LocalTime departureTime = timeCalculator.calculateDepartureTime(travelMinutes);
        LocalTime arrivalTime = timeCalculator.calculateArrivalTime(travelMinutes);
        log.info("🚌 Salida de casa: {}, Llegada a casa: {}", departureTime, arrivalTime);

        // 5. Calcular horas de estudio base
        long studyHours = timeCalculator.getStudyHours();
        log.info("📚 Horas de estudio base: {} horas", studyHours);

        // ============================================================
        // 🔥 NUEVO: Actualizar el progreso de la educación
        // ============================================================
        if (character.getEducation() != null && !character.getEducation().isEmpty()) {
            CharacterTrainingDto education = character.getEducation().get(0);

            // Horas invertidas actuales + horas de estudio del día
            int currentInvestedHours = education.getInvestedHours() != null ? education.getInvestedHours() : 0;
            int newInvestedHours = currentInvestedHours + (int) studyHours;
            education.setInvestedHours(newInvestedHours);

            // Calcular nuevo progreso basado en duración total del curso
            int totalDuration = education.getInvestedHours() != null ? education.getInvestedHours() : 0;
            int newProgress = calculateProgress(totalDuration, newInvestedHours);
            education.setProgress(newProgress);

            // Actualizar la educación
            businessTransactions.updateAppTrainning(education);

            log.info("📚 Educación actualizada - Horas invertidas: {} → {}, Progreso: {}%",
                    currentInvestedHours, newInvestedHours, newProgress);

            events.add(DayEventDTO.builder()
                    .type("education")
                    .title("📖 Progreso académico")
                    .description(String.format("Has estudiado %d horas. Progreso: %d%%", studyHours, newProgress))
                    .xpGained((int) (studyHours * 10)) // Ejemplo: 10 XP por hora
                    .build());
        }

        // 6. Calcular ganancias/pérdidas basadas en descanso y estudio
        int energyGainFromRest = energyCalculator.calculateForRest(restHours);
        int energyCostFromStudy = energyCalculator.calculateForStudy(studyHours);
        int totalEnergyChange = energyGainFromRest + energyCostFromStudy;

        int stressReductionFromRest = stressCalculator.calculateForRest(restHours);
        int stressIncreaseFromStudy = stressCalculator.calculateForStudy(studyHours);
        int totalStressChange = stressReductionFromRest - stressIncreaseFromStudy;

        log.info("⚡ Energía: +{} por descanso, {} por estudio = {} total",
                energyGainFromRest, energyCostFromStudy, totalEnergyChange);
        log.info("😰 Estrés: -{} por descanso, +{} por estudio = {} total",
                stressReductionFromRest, stressIncreaseFromStudy, totalStressChange);

        // Evento: Descanso nocturno
        if (restHours > 0) {
            events.add(DayEventDTO.builder()
                    .type("rest")
                    .title("😴 Descanso nocturno")
                    .description(String.format("Has descansado %d horas", restHours))
                    .energyGain(energyGainFromRest)
                    .stressReduction(stressReductionFromRest)
                    .build());
        }

        // Evento: Desplazamiento a clase
        events.add(DayEventDTO.builder()
                .type("travel")
                .title("🚌 Desplazamiento")
                .description(String.format("Sales de casa a las %s para llegar a clase", departureTime))
                .build());

        // Evento: Jornada de estudio
        events.add(DayEventDTO.builder()
                .type("study")
                .title("📚 Jornada de estudio")
                .description(String.format("Has estudiado %d horas (de %s a %s)",
                        studyHours, TimeCalculator.SCHOOL_START_TIME, TimeCalculator.SCHOOL_END_TIME))
                .energyGain(energyCostFromStudy)
                .stressReduction(-stressIncreaseFromStudy)
                .build());

        // Evento: Desplazamiento a casa
        events.add(DayEventDTO.builder()
                .type("travel")
                .title("🏠 Regreso a casa")
                .description(String.format("Llegas a casa a las %s", arrivalTime))
                .build());

        // 7. Actualizar estadísticas del personaje
        int currentEnergy = character.getStats().getEnergy();
        int currentStress = character.getStats().getStress();

        int newEnergy = Math.min(100, Math.max(0, currentEnergy + totalEnergyChange));
        int newStress = Math.min(100, Math.max(0, currentStress + totalStressChange));

        character.getStats().setEnergy(newEnergy);
        character.getStats().setStress(newStress);
        businessTransactions.updatePerson(character);

        events.add(DayEventDTO.builder()
                .type("stats")
                .title("📊 Estadísticas actualizadas")
                .description(String.format("Energía: %d%% → %d%% | Estrés: %d%% → %d%%",
                        currentEnergy, newEnergy, currentStress, newStress))
                .energyGain(totalEnergyChange)
                .stressReduction(-totalStressChange)
                .build());

        // 8. ACTUALIZAR SISTEMA - Avanzar al siguiente día
        LocalDateTime nextDaySchoolStart = currentDateTime
                .plusDays(1)
                .withHour(TimeCalculator.SCHOOL_START_TIME.getHour())
                .withMinute(TimeCalculator.SCHOOL_START_TIME.getMinute())
                .withSecond(0)
                .withNano(0);

        log.info("📆 Avanzando al inicio de clases: {}", nextDaySchoolStart);

        LocalDateTime newActuality = nextDaySchoolStart
                .withHour(arrivalTime.getHour())
                .withMinute(arrivalTime.getMinute())
                .withSecond(0)
                .withNano(0);

        log.info("📆 Nueva fecha/hora después de la jornada: {}", newActuality);

        // Actualizar sistema
        system.setActualityAt(newActuality);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);

        // Restar 1 PA (NO resetear a 5)
        int newPa = system.getPa() - 1;
        system.setPa(Math.max(0, newPa));

        systemRepository.save(system);

        log.info("✅ Sistema actualizado - Nueva fecha: {}, PA restantes: {}", newActuality, system.getPa());
        log.info("=== AVANCE COMPLETADO ===");

        events.add(DayEventDTO.builder()
                .type("system")
                .title("⏰ Tiempo avanzado")
                .description(String.format("Has completado la jornada. Ahora son las %s del día %s",
                        arrivalTime, newActuality.toLocalDate()))
                .build());

        // 9. Procesar datos pendientes
        processPendingData(character);

        return TimeAdvanceResponseDTO.builder()
                .success(true)
                .message(String.format("Jornada completada. Estudiado %d horas. Descansado %d horas.", studyHours, restHours))
                .newActualityAt(newActuality)
                .paRemaining(system.getPa())
                .energyChange(totalEnergyChange)
                .stressChange(totalStressChange)
                .events(events)
                .build();
    }

    /**
     * Calcula el progreso basado en horas invertidas y duración total
     */
    private int calculateProgress(Integer totalDuration, Integer investedHours) {
        if (totalDuration == null || totalDuration == 0) {
            return 0;
        }
        int invested = investedHours != null ? investedHours : 0;
        int progress = (int) Math.round(((double) invested / totalDuration) * 100);
        return Math.min(100, progress); // No superar 100%
    }

    private void processPendingData(CharacterDto character) {
        try {
            log.info("📋 Procesando datos pendientes para personaje: {}", character.getId());
            businessTransactions.processPendingInterviews(character.getId());
            businessTransactions.processedAdvance(character.getId(), 70);
        } catch (Exception e) {
            log.error("Error procesando datos pendientes: {}", e.getMessage());
        }
    }

    @Override
    public EnumSystems.AdvanceType getType() {
        return EnumSystems.AdvanceType.NORMAL_DAY;
    }
}