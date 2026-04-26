package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;
import com.pet.businessdomain.systemservice.transactions.BusinessTransactions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeekendStrategy implements AdvanceStrategy {
    public static final int PA = 8;

    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final SystemService systemService;
    private final BusinessTransactions businessTransactions;
    private final EnergyCalculator energyCalculator;
    private final StressCalculator stressCalculator;
    private final TimeCalculator timeCalculator;

    // IDs de trabajos de fin de semana (ids entre 32 y 42)
    private static final List<Long> WEEKEND_JOB_IDS = List.of(32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L, 41L, 42L);

    @Override
    public TimeAdvanceResponseDTO execute(TimeAdvanceRequestDTO request) {
        log.info("=== INICIANDO AVANCE DE FIN DE SEMANA ===");
        log.info("Personaje ID: {}", request.getCharacterId());

        // 1. Obtener sistema y personaje
        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        CharacterDto character = businessTransactions.getPerson(systemDto.getUid());

        LocalDateTime currentDateTime = systemDto.getActualityAt();
        log.info("📅 Fecha/Hora actual: {}", currentDateTime);

        // 2. Verificar si tiene trabajo activo con ID entre 32 y 42
        boolean hasWeekendJob = checkWeekendJob(character);

        TimeAdvanceResponseDTO response;

        if (hasWeekendJob) {
            log.info("💼 Personaje tiene trabajo de fin de semana. Ejecutando lógica de TRABAJO.");
            response = executeWeekendJobDay(request, system, character, currentDateTime);
        } else {
            log.info("😴 Personaje NO tiene trabajo de fin de semana. Ejecutando lógica de DESCANSO.");
            response = executeWeekendRestDay(request, system, character, currentDateTime);
        }

        // Procesar datos pendientes (común para ambos casos)
        processPendingData(character);

        return response;
    }

    private boolean checkWeekendJob(CharacterDto character) {
        if (character.getJobs() == null || character.getJobs().isEmpty()) {
            log.info("Personaje no tiene trabajos activos");
            return false;
        }

        return character.getJobs().stream()
                .anyMatch(job -> {
                    boolean isActive = job.getActive() != null && job.getActive();
                    if (!isActive || job.getVacancy() == null) {
                        return false;
                    }

                    Long positionId = job.getVacancy().getPositionId();
                    boolean isWeekendJob = positionId != null && WEEKEND_JOB_IDS.contains(positionId);

                    if (isWeekendJob) {
                        log.info("✅ Trabajo de fin de semana encontrado - Position ID: {}, Título: {}",
                                positionId, job.getCompanyName());
                    }

                    return isWeekendJob;
                });
    }

    /**
     * Lógica para fin de semana con TRABAJO (SIN salario - solo ganancia diaria)
     */
    private TimeAdvanceResponseDTO executeWeekendJobDay(TimeAdvanceRequestDTO request,
                                                        SystemEntity system,
                                                        CharacterDto character,
                                                        LocalDateTime currentDateTime) {
        List<DayEventDTO> events = new ArrayList<>();

        log.info("💼 EJECUTANDO LÓGICA DE TRABAJO EN FIN DE SEMANA");

        // Encontrar el trabajo activo de fin de semana
        CharacterJobDTO activeJob = character.getJobs().stream()
                .filter(job -> job.getActive() != null && job.getActive()
                        && job.getVacancy() != null
                        && WEEKEND_JOB_IDS.contains(job.getVacancy().getPositionId()))
                .findFirst()
                .orElse(null);

        if (activeJob == null) {
            log.error("No se encontró trabajo activo de fin de semana");
            return executeWeekendRestDay(request, system, character, currentDateTime);
        }

        JobVacancyDTO vacancy = activeJob.getVacancy();
        String jobTitle = activeJob.getPositionTitle();
        String companyName = activeJob.getCompanyName();

        // Obtener horas de trabajo de la vacante
        Integer weeklyHours = vacancy.getWeeklyHours() != null ? vacancy.getWeeklyHours() : 20;

        // Calcular horas por día
        List<EnumAll.WorkingDay> workingDays = vacancy.getWorkingDays();
        int workingDaysCount = (workingDays != null && !workingDays.isEmpty()) ? workingDays.size() : 2;
        long workHours = Math.round((double) weeklyHours / workingDaysCount);

        // Obtener horario de inicio y fin
        LocalTime jobStartTime = vacancy.getStartTime() != null ? vacancy.getStartTime() : LocalTime.of(16, 0);
        LocalTime jobEndTime = vacancy.getEndTime() != null ? vacancy.getEndTime() : LocalTime.of(20, 0);

        // Si las horas calculadas no coinciden con el horario, usar la diferencia del horario
        long hoursFromSchedule = java.time.Duration.between(jobStartTime, jobEndTime).toHours();
        if (hoursFromSchedule > 0 && hoursFromSchedule < 24) {
            workHours = hoursFromSchedule;
        }

        log.info("📊 Horas de trabajo hoy: {}h | Horario: {} - {}", workHours, jobStartTime, jobEndTime);

        // Calcular horas de descanso hasta la hora de inicio del trabajo
        LocalDateTime jobStartDateTime = currentDateTime.plusDays(1)
                .withHour(jobStartTime.getHour())
                .withMinute(jobStartTime.getMinute())
                .withSecond(0)
                .withNano(0);
        long restHours = Math.max(0, java.time.Duration.between(currentDateTime, jobStartDateTime).toMinutes() / 60);

        int energyGainFromRest = energyCalculator.calculateForRest(restHours);
        int stressReductionFromRest = stressCalculator.calculateForRest(restHours);
        int energyCostFromWork = energyCalculator.calculateForWork(workHours);
        int stressIncreaseFromWork = stressCalculator.calculateForWork(workHours);

        // Calcular ganancias DIARIAS (NO el salario semanal)
        int dailyEarnings = calculateDailyEarnings(vacancy, workHours, workingDaysCount);

        // Actualizar estadísticas
        int currentEnergy = character.getStats().getEnergy();
        int currentStress = character.getStats().getStress();

        int energyAfterRest = Math.min(100, currentEnergy + energyGainFromRest);
        int newEnergy = Math.max(0, energyAfterRest - Math.abs(energyCostFromWork));
        int stressAfterRest = Math.max(0, currentStress - stressReductionFromRest);
        int newStress = Math.min(100, stressAfterRest + stressIncreaseFromWork);

        int totalEnergyChange = newEnergy - currentEnergy;
        int totalStressChange = newStress - currentStress;
// Obtener minutos de viaje según vehículo
        int travelMinutes = timeCalculator.getTravelTimeMinutes(character);
        String vehicle = timeCalculator.getTravelVehicle(character);
        log.info("Tiempo de viaje: {} minutos", travelMinutes);

        // Calcular hora de salida y llegada según vehículo
        LocalTime departureTime = timeCalculator.calculateDepartureTime(travelMinutes);
        LocalTime arrivalTime = timeCalculator.calculateArrivalTime(travelMinutes);
        character.getStats().setEnergy(newEnergy);
        character.getStats().setStress(newStress);

        // 💰 SOLO ingresar ganancias DIARIAS (NO salario semanal)
        if (dailyEarnings > 0 && character.getAccounts() != null && !character.getAccounts().isEmpty()) {
            SFinanceAccountResponseDto primaryAccount = character.getAccounts().get(0);
            primaryAccount.setBalance(primaryAccount.getBalance().add(BigDecimal.valueOf(dailyEarnings)));
            log.info("💰 Ganancia diaria añadida: {}€", dailyEarnings);
        }

        // Guardar cambios del personaje
        businessTransactions.updatePerson(character);

        // Avanzar el tiempo al final del trabajo
        LocalDateTime newActuality = calculateNewActuality(currentDateTime, jobEndTime);

        // Actualizar sistema
        system.setActualityAt(newActuality);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);
        system.setPa(PA);
        systemRepository.save(system);

        // Eventos
        if (restHours > 0) {
            events.add(DayEventDTO.builder()
                    .type("rest")
                    .title("Despertandote")
                    .description(String.format("Has descansado %d horas antes de trabajar", restHours))
                    .date(String.format("%s", jobStartTime.minusMinutes(20)))
                    .energyGain(energyGainFromRest)
                    .stressReduction(stressReductionFromRest)
                    .build());
        }
// Evento: Desplazamiento a clase
        events.add(DayEventDTO.builder()
                .type("travel")
                .title("Desplazamiento")
                .description(String.format("Sales de casa para llegar al trabajo (%s)", vehicle))
                .date(String.format("%s", departureTime))
                .build());
        events.add(DayEventDTO.builder()
                .type("work_start")
                .title("Comenzando jornada laboral")
                .description(String.format(" %s en %s", jobTitle, companyName))
                .date(String.format("%s", jobStartTime))
                .build());

        events.add(DayEventDTO.builder()
                .type("work")
                .title("Finalizando jornada laboral")
                .description(String.format("Has trabajado %d horas", workHours))
                .energyGain(energyCostFromWork)
                .stressReduction(-stressIncreaseFromWork)
                .moneyEarned(dailyEarnings)
                .date(String.format("%s", jobEndTime))

                .build());
// Evento: Desplazamiento a casa
        events.add(DayEventDTO.builder()
                .type("travel")
                .title("Regreso a casa")
                .description(String.format("Vuelves a casa (%s)", vehicle))
                .date(String.format("%s", arrivalTime))
                .build());
        String message = String.format("Jornada laboral completada. Has ganado %d€ trabajando %d horas.",
                dailyEarnings, workHours);

        return TimeAdvanceResponseDTO.builder()
                .success(true)
                .message(message)
                .newActualityAt(newActuality)
                .paRemaining(system.getPa())
                .energyChange(totalEnergyChange)
                .stressChange(totalStressChange)
                .events(events)
                .build();
    }

    /**
     * Calcula las ganancias diarias (SOLO ingreso diario, no salario completo)
     */
    private int calculateDailyEarnings(JobVacancyDTO vacancy, long workHours, int workingDaysCount) {
        if (vacancy == null) {
            return (int) (workHours * 10);
        }

        BigDecimal minSalary = vacancy.getMinSalary();
        BigDecimal maxSalary = vacancy.getMaxSalary();

        BigDecimal annualSalary;
        if (maxSalary != null && minSalary != null) {
            annualSalary = minSalary.add(maxSalary).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        } else if (minSalary != null) {
            annualSalary = minSalary;
        } else {
            return (int) (workHours * 10);
        }

        // Salario diario: anual / 52 semanas / días trabajados por semana
        BigDecimal weeklySalary = annualSalary.divide(BigDecimal.valueOf(52), 4, RoundingMode.HALF_UP);
        BigDecimal dailySalary = weeklySalary.divide(BigDecimal.valueOf(workingDaysCount), 2, RoundingMode.HALF_UP);

        return dailySalary.intValue();
    }

    private LocalDateTime calculateNewActuality(LocalDateTime currentDateTime, LocalTime jobEndTime) {
        LocalDateTime newActuality = currentDateTime.plusDays(1)
                .withHour(jobEndTime.getHour())
                .withMinute(jobEndTime.getMinute())
                .withSecond(0)
                .withNano(0);

        if (newActuality.isBefore(currentDateTime)) {
            newActuality = newActuality.plusDays(1);
        }

        return newActuality;
    }

    /**
     * Lógica para fin de semana de DESCANSO (sin trabajo)
     */
    private TimeAdvanceResponseDTO executeWeekendRestDay(TimeAdvanceRequestDTO request,
                                                         SystemEntity system,
                                                         CharacterDto character,
                                                         LocalDateTime currentDateTime) {
        List<DayEventDTO> events = new ArrayList<>();

        LocalDateTime weekendWakeTime = currentDateTime.plusDays(1).withHour(10).withMinute(0);
        long minutesDiff = timeCalculator.calculateMinutesDifference(currentDateTime, weekendWakeTime);
        long hoursDiff = minutesDiff / 60;

        int energyGain = energyCalculator.calculateForRest(hoursDiff);

        events.add(DayEventDTO.builder()
                .type("sleep_in")
                .title("Dormir hasta tarde")
                .description(String.format("Has descansado %d horas", hoursDiff))
                .date(String.format("%s", 10))
                .energyGain(energyGain)
                .build());

        int newEnergy = Math.min(100, character.getStats().getEnergy() + energyGain);
        character.getStats().setEnergy(newEnergy);

        int currentStress = character.getStats().getStress();
        int newStress;
        if (currentStress > 60) {
            newStress = 50;
        } else {
            newStress = 20;
        }
        character.getStats().setStress(newStress);

        businessTransactions.updatePerson(character);

        LocalDateTime newActuality = currentDateTime
                .plusDays(1)
                .withHour(weekendWakeTime.getHour())
                .withMinute(weekendWakeTime.getMinute())
                .withSecond(0)
                .withNano(0);

        system.setActualityAt(newActuality);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);
        system.setPa(PA);
        systemRepository.save(system);

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
            systemService.revisedData(character);
        } catch (Exception e) {
            log.error("Error procesando datos pendientes: {}", e.getMessage());
        }
    }

    @Override
    public EnumSystems.AdvanceType getType() {
        return EnumSystems.AdvanceType.WEEKEND;
    }
}