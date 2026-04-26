package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.EnumFormation;
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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NormalDayStrategy implements AdvanceStrategy {
    public static final int PA = 8;

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

        // 1. Obtener sistema y personaje
        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        CharacterDto character = businessTransactions.getPerson(systemDto.getUid());

        LocalDateTime currentDateTime = systemDto.getActualityAt();
        log.info("📅 Fecha/Hora actual: {}", currentDateTime);

        // 2. Verificar si tiene educación COMPLETADA (ids 104 o 105)
        boolean hasCompletedEducation = checkCompletedEducation(character);

        TimeAdvanceResponseDTO response;

        if (hasCompletedEducation) {
            log.info("🎓 Personaje tiene educación completada. Ejecutando lógica de TRABAJO.");
            response = executeJobDay(request, system, character, currentDateTime);
        } else {
            log.info("📚 Personaje NO tiene educación completada. Ejecutando lógica de EDUCACIÓN.");
            response = executeEducationDay(request, system, character, currentDateTime);
        }

        // 3. Procesar datos pendientes y obtener sus eventos
        List<DayEventDTO> pendingEvents = new ArrayList<>();
        processPendingData(character, pendingEvents);

        // 4. COMBINAR los eventos de la respuesta con los eventos de datos pendientes
        if (response.getEvents() != null) {
            response.getEvents().addAll(pendingEvents);
        } else {
            // Si response no tiene eventos, crear una nueva lista
            List<DayEventDTO> allEvents = new ArrayList<>();
            if (response.getEvents() != null) {
                allEvents.addAll(response.getEvents());
            }
            allEvents.addAll(pendingEvents);

            // Reconstruir la respuesta con los eventos combinados
            response = TimeAdvanceResponseDTO.builder()
                    .success(response.isSuccess())
                    .message(response.getMessage())
                    .newActualityAt(response.getNewActualityAt())
                    .paRemaining(response.getPaRemaining())
                    .energyChange(response.getEnergyChange())
                    .stressChange(response.getStressChange())
                    .xpEarned(response.getXpEarned())
                    .statChanges(response.getStatChanges())
                    .events(allEvents)
                    .build();
        }

        return response;
    }

    /**
     * Verifica si el personaje tiene educación completada con ids 104 o 105
     */
    private boolean checkCompletedEducation(CharacterDto character) {
        if (character.getEducation() == null || character.getEducation().isEmpty()) {
            return false;
        }

        List<Long> educationIds = List.of(104L, 105L);

        return character.getEducation().stream()
                .anyMatch(edu -> educationIds.contains(edu.getTrainingId())
                        && edu.getStatus() != null
                        && edu.getStatus().toString().equals("COMPLETED"));
    }

    /**
     * Lógica para días de EDUCACIÓN
     */
    private TimeAdvanceResponseDTO executeEducationDay(TimeAdvanceRequestDTO request,
                                                       SystemEntity system,
                                                       CharacterDto character,
                                                       LocalDateTime currentDateTime) {
        List<DayEventDTO> events = new ArrayList<>();

        // Calcular horas de descanso (desde ahora hasta inicio de clases)
        long restHours = timeCalculator.calculateRestHours(currentDateTime);
        log.info("Horas de descanso hasta inicio de clases: {} horas", restHours);

        // Obtener minutos de viaje según vehículo
        int travelMinutes = timeCalculator.getTravelTimeMinutes(character);
        String vehicle = timeCalculator.getTravelVehicle(character);
        log.info("Tiempo de viaje: {} minutos", travelMinutes);

        // Calcular hora de salida y llegada según vehículo
        LocalTime departureTime = timeCalculator.calculateDepartureTime(travelMinutes);
        LocalTime arrivalTime = timeCalculator.calculateArrivalTime(travelMinutes);
        log.info("Salida de casa: {}, Llegada a casa: {}", departureTime, arrivalTime);

        // Calcular horas de estudio base
        long studyHours = timeCalculator.getStudyHours();
        log.info("Horas de estudio base: {} horas", studyHours);

        // Actualizar el progreso de la educación
        if (character.getEducation() != null && !character.getEducation().isEmpty()) {
            CharacterTrainingDto education = character.getEducation().get(0);

            int currentInvestedHours = education.getInvestedHours() != null ? education.getInvestedHours() : 0;
            int newInvestedHours = currentInvestedHours + (int) studyHours;
            education.setInvestedHours(newInvestedHours);
            FormationDto formationDto = businessTransactions.getFormation(education.getTrainingId());
            int totalDuration = formationDto.getDurationHours() != null ? formationDto.getDurationHours() : 0;
            int newProgress = calculateProgress(totalDuration, newInvestedHours);
            education.setProgress(newProgress);

            businessTransactions.updateAppTrainning(education);

            log.info("Educación actualizada - Horas invertidas: {} → {}, Progreso: {}%",
                    currentInvestedHours, newInvestedHours, newProgress);
        }

        // Calcular ganancias/pérdidas basadas en descanso y estudio
        int energyGainFromRest = energyCalculator.calculateForRest(restHours);
        int energyCostFromStudy = energyCalculator.calculateForStudy(studyHours);
        int stressReductionFromRest = stressCalculator.calculateForRest(restHours);
        int stressIncreaseFromStudy = stressCalculator.calculateForStudy(studyHours);

        int currentEnergy = character.getStats().getEnergy();
        int currentStress = character.getStats().getStress();

        int energyAfterRest = Math.min(100, currentEnergy + energyGainFromRest);
        int newEnergy = Math.max(0, energyAfterRest - Math.abs(energyCostFromStudy));
        int stressAfterRest = Math.max(0, currentStress - stressReductionFromRest);
        int newStress = Math.min(100, stressAfterRest + stressIncreaseFromStudy);
        int totalEnergyChange = newEnergy - currentEnergy;
        int totalStressChange = newStress - currentStress;

        log.info("Energía: {} + {} descanso - {} estudio = {} ({}→{})",
                currentEnergy, energyGainFromRest, Math.abs(energyCostFromStudy), newEnergy, currentEnergy, newEnergy);
        log.info("Estrés: {} - {} descanso + {} estudio = {} ({}→{})",
                currentStress, stressReductionFromRest, stressIncreaseFromStudy, newStress, currentStress, newStress);

        // Evento: Descanso nocturno
        if (restHours > 0) {
            events.add(DayEventDTO.builder()
                    .type("rest")
                    .title("Despertandote")
                    .description(String.format("Has descansado %d horas", restHours))
                    .energyGain(energyGainFromRest)
                    .date(String.format("%s", departureTime.minusMinutes(20)))
                    .stressReduction(stressReductionFromRest)
                    .build());
        }

        // Evento: Desplazamiento a clase
        events.add(DayEventDTO.builder()
                .type("travel")
                .title("Desplazamiento")
                .description(String.format("Sales de casa para llegar a clase (%s)", vehicle))
                .date(String.format("%s", departureTime))
                .build());

        // Evento: Jornada de estudio
        events.add(DayEventDTO.builder()
                .type("study")
                .title("Entrando a clase")
                .date(String.format("%s", TimeCalculator.SCHOOL_START_TIME))
                .build());
        events.add(DayEventDTO.builder()
                .type("study")
                .title("Saliendo de clase")
                .description(String.format("Has estudiado %d horas ",
                        studyHours))
                .energyGain(energyCostFromStudy)
                .stressReduction(-stressIncreaseFromStudy)
                .date(String.format("%s", TimeCalculator.SCHOOL_END_TIME))
                .build());
        // Evento: Desplazamiento a casa
        events.add(DayEventDTO.builder()
                .type("travel")
                .title("Regreso a casa")
                .description(String.format("Vuelves a casa (%s)", vehicle))
                .date(String.format("%s", arrivalTime))
                .build());

        character.getStats().setEnergy(newEnergy);
        character.getStats().setStress(newStress);
        businessTransactions.updatePerson(character);

        // Avanzar al siguiente día
        LocalDateTime newActuality = currentDateTime
                .plusDays(1)
                .withHour(arrivalTime.getHour())
                .withMinute(arrivalTime.getMinute())
                .withSecond(0)
                .withNano(0);

        // Actualizar sistema
        system.setActualityAt(newActuality);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);
        system.setPa(PA);
        systemRepository.save(system);

        log.info("✅ Sistema actualizado - Nueva fecha: {}, PA restantes: {}", newActuality, system.getPa());

//        events.add(DayEventDTO.builder()
//                .type("system")
//                .title("Tiempo avanzado")
//                .description(String.format("Has completado la jornada. Ahora son las %s del día %s",
//                        arrivalTime, newActuality.toLocalDate()))
//                .date(String.format("%s", departureTime))
//                .build());

        return TimeAdvanceResponseDTO.builder()
                .success(true)
                .message(String.format("Jornada de estudio completada. Estudiado %d horas. Descansado %d horas.", studyHours, restHours))
                .newActualityAt(newActuality)
                .paRemaining(system.getPa())
                .energyChange(totalEnergyChange)
                .stressChange(totalStressChange)
                .events(events)
                .build();
    }

    /**
     * Lógica para días de TRABAJO (COMPLETADA)
     */
    private TimeAdvanceResponseDTO executeJobDay(TimeAdvanceRequestDTO request,
                                                 SystemEntity system,
                                                 CharacterDto character,
                                                 LocalDateTime currentDateTime) {
        List<DayEventDTO> events = new ArrayList<>();

        log.info("💼 EJECUTANDO LÓGICA DE TRABAJO");

        // Obtener el trabajo activo del personaje
        CharacterJobDTO activeJob = null;
        if (character.getJobs() != null && !character.getJobs().isEmpty()) {
            activeJob = character.getJobs().stream()
                    .filter(job -> job.getActive() != null && job.getActive())
                    .findFirst()
                    .orElse(null);
        }

        // Si no tiene trabajo activo, mostrar mensaje y avanzar
        if (activeJob == null || activeJob.getVacancy() == null) {
            log.warn("⚠️ Personaje sin trabajo activo válido");
            events.add(DayEventDTO.builder()
                    .type("warning")
                    .title("⚠️ Sin trabajo activo")
                    .description("No tienes un trabajo activo. Busca empleo para ganar dinero.")
                    .build());

            // Avanzar tiempo sin trabajar
            LocalDateTime newActuality = currentDateTime.plusDays(1)
                    .withHour(10)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);

            system.setActualityAt(newActuality);
            system.setUpdateAt(LocalDateTime.now());
            system.setVeces(system.getVeces() + 1);
            system.setPa(PA);
            systemRepository.save(system);

            return TimeAdvanceResponseDTO.builder()
                    .success(true)
                    .message("Día completado. No tienes trabajo activo.")
                    .newActualityAt(newActuality)
                    .paRemaining(system.getPa())
                    .energyChange(0)
                    .stressChange(0)
                    .events(events)
                    .build();
        }

        JobVacancyDTO vacancy = activeJob.getVacancy();
        String jobTitle = activeJob.getPositionTitle();
        String companyName = activeJob.getCompanyName();

        // Obtener horario de trabajo de la vacante
        LocalTime jobStartTime = vacancy.getStartTime() != null ? vacancy.getStartTime() : LocalTime.of(9, 0);
        LocalTime jobEndTime = vacancy.getEndTime() != null ? vacancy.getEndTime() : LocalTime.of(17, 0);

        // Calcular horas de trabajo
        long workHours = java.time.Duration.between(jobStartTime, jobEndTime).toHours();
        if (workHours <= 0) workHours = 8;

        log.info("📊 Horario laboral: {} - {} ({} horas)", jobStartTime, jobEndTime, workHours);

        // Calcular horas de descanso hasta el trabajo
        LocalDateTime jobStartDateTime = currentDateTime.plusDays(1)
                .withHour(jobStartTime.getHour())
                .withMinute(jobStartTime.getMinute())
                .withSecond(0)
                .withNano(0);
        long restHours = Math.max(0, java.time.Duration.between(currentDateTime, jobStartDateTime).toMinutes() / 60);

        // Calcular cambios de energía y estrés
        int energyGainFromRest = energyCalculator.calculateForRest(restHours);
        int stressReductionFromRest = stressCalculator.calculateForRest(restHours);
        int energyCostFromWork = energyCalculator.calculateForWork(workHours);
        int stressIncreaseFromWork = stressCalculator.calculateForWork(workHours);

        // Calcular ganancias diarias
        int dailyEarnings = calculateDailyJobEarnings(activeJob, vacancy, workHours);

        // Actualizar estadísticas
        int currentEnergy = character.getStats().getEnergy();
        int currentStress = character.getStats().getStress();

        int energyAfterRest = Math.min(100, currentEnergy + energyGainFromRest);
        int newEnergy = Math.max(0, energyAfterRest - Math.abs(energyCostFromWork));
        int stressAfterRest = Math.max(0, currentStress - stressReductionFromRest);
        int newStress = Math.min(100, stressAfterRest + stressIncreaseFromWork);

        int totalEnergyChange = newEnergy - currentEnergy;
        int totalStressChange = newStress - currentStress;

        character.getStats().setEnergy(newEnergy);
        character.getStats().setStress(newStress);

        // Ingresar ganancias diarias
        if (dailyEarnings > 0 && character.getAccounts() != null && !character.getAccounts().isEmpty()) {
            SFinanceAccountResponseDto primaryAccount = character.getAccounts().get(0);
            primaryAccount.setBalance(primaryAccount.getBalance().add(BigDecimal.valueOf(dailyEarnings)));
            log.info("💰 Ganancia diaria añadida: {}€", dailyEarnings);
        }

        // Actualizar estadísticas del trabajo
        if (activeJob.getPerformance() != null && activeJob.getPerformance() < 100) {
            activeJob.setPerformance(Math.min(100, activeJob.getPerformance() + 1));
        }
        if (activeJob.getStressLevel() != null && activeJob.getStressLevel() > 0) {
            activeJob.setStressLevel(Math.max(0, activeJob.getStressLevel() - 2));
        }

        businessTransactions.updatePerson(character);

        // Avanzar tiempo al final del trabajo
        LocalDateTime newActuality = currentDateTime.plusDays(1)
                .withHour(jobEndTime.getHour())
                .withMinute(jobEndTime.getMinute())
                .withSecond(0)
                .withNano(0);

        system.setActualityAt(newActuality);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);
        system.setPa(PA);
        systemRepository.save(system);

        // Crear eventos
        if (restHours > 0) {
            events.add(DayEventDTO.builder()
                    .type("rest")
                    .title("Descanso nocturno")
                    .description(String.format("Has descansado %d horas antes de trabajar", restHours))
                    .energyGain(energyGainFromRest)
                    .date(jobStartTime.toString())
                    .stressReduction(stressReductionFromRest)
                    .build());
        }

        events.add(DayEventDTO.builder()
                .type("work_start")
                .title("Comenzando jornada laboral")
                .description(String.format("Te diriges a trabajar como %s en %s a las %s",
                        jobTitle, companyName, jobStartTime))
                .date(jobStartTime.toString())

                .build());

        events.add(DayEventDTO.builder()
                .type("work")
                .title("Jornada laboral completada")
                .description(String.format("Has trabajado %d horas como %s en %s de %s a %s",
                        workHours, jobTitle, companyName, jobStartTime, jobEndTime))
                .energyGain(energyCostFromWork)
                .stressReduction(-stressIncreaseFromWork)
                .moneyEarned(dailyEarnings)
                .date(jobEndTime.toString())

                .build());

        events.add(DayEventDTO.builder()
                .type("system")
                .title("Tiempo avanzado")
                .description(String.format("Has completado tu jornada laboral. Ahora son las %s del día %s",
                        jobEndTime, newActuality.toLocalDate()))
                .date(jobEndTime.toString())

                .build());

        String message = String.format("Jornada laboral completada. Has ganado %d€ trabajando %d horas.",
                dailyEarnings, workHours);

        log.info("✅ Jornada laboral completada - Ganancia: {}€, Energía: {}→{}, Estrés: {}→{}",
                dailyEarnings, currentEnergy, newEnergy, currentStress, newStress);

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
     * Calcula las ganancias diarias del trabajo
     */
    private int calculateDailyJobEarnings(CharacterJobDTO job, JobVacancyDTO vacancy, long workHours) {
        BigDecimal annualSalary = job.getCurrentSalary();

        if (annualSalary == null && vacancy != null) {
            BigDecimal minSalary = vacancy.getMinSalary();
            BigDecimal maxSalary = vacancy.getMaxSalary();
            if (maxSalary != null && minSalary != null) {
                annualSalary = minSalary.add(maxSalary).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            } else if (minSalary != null) {
                annualSalary = minSalary;
            }
        }

        if (annualSalary == null) {
            log.warn("No se pudo determinar salario, usando valor por defecto de 10€/hora");
            return (int) (workHours * 10);
        }

        // Calcular días laborables por semana
        List<EnumAll.WorkingDay> workingDays = vacancy.getWorkingDays();
        int workingDaysCount = (workingDays != null && !workingDays.isEmpty()) ? workingDays.size() : 5;

        // Salario diario = anual / 52 semanas / días por semana
        BigDecimal weeklySalary = annualSalary.divide(BigDecimal.valueOf(52), 2, RoundingMode.HALF_UP);
        BigDecimal dailySalary = weeklySalary.divide(BigDecimal.valueOf(workingDaysCount), 2, RoundingMode.HALF_UP);

        log.info("💰 Salario diario calculado: {}€", dailySalary.intValue());

        return dailySalary.intValue();
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
        return Math.min(100, progress);
    }

    /**
     * Procesa datos pendientes (aplicaciones, entrevistas, etc.)
     * ✅ Usa los métodos existentes de BusinessTransactions
     */
    private void processPendingData(CharacterDto character, List<DayEventDTO> events) {
        try {
            log.info("🔄 Procesando datos pendientes para personaje: {}", character.getId());

            // 1. Procesar entrevistas pendientes
            Map<String, Object> interviewResp = businessTransactions.processPendingInterviews(character.getId());
            log.info("Respuesta entrevistas: {}", interviewResp);

            if (interviewResp != null && interviewResp.containsKey("interviews")) {
                List<Map<String, Object>> interviews = (List<Map<String, Object>>) interviewResp.get("interviews");
                if (interviews != null && !interviews.isEmpty()) {
                    events.add(DayEventDTO.builder()
                            .type("interview")
                            .title("📅 ¡Nuevas entrevistas programadas!")
                            .description(String.format("Tienes %d entrevista(s) pendiente(s). Revisa tu calendario.", interviews.size()))
                            .build());
                }
            }

            // 2. Procesar aplicaciones de trabajo
            Map<String, Object> advanceResp = businessTransactions.processedAdvance(character.getId(), 70);
            log.info("Respuesta avance: {}", advanceResp);

            if (advanceResp != null && advanceResp.containsKey("applications")) {
                List<Map<String, Object>> applications = (List<Map<String, Object>>) advanceResp.get("applications");

                if (applications != null && !applications.isEmpty()) {
                    int acceptedCount = 0;
                    int rejectedCount = 0;

                    for (Map<String, Object> app : applications) {
                        String status = (String) app.get("status");
                        String jobTitle = (String) app.get("jobTitle");

                        if ("ACCEPTED".equals(status)) {
                            acceptedCount++;
                            events.add(DayEventDTO.builder()
                                    .type("job_application")
                                    .title("🎯 ¡Postulación avanzada!")
                                    .description(String.format("Tu postulación para '%s' ha pasado a la fase de entrevista.", jobTitle))
                                    .build());
                        } else if ("REJECTED".equals(status)) {
                            rejectedCount++;
                        }
                    }

                    if (acceptedCount > 0) {
                        log.info("✅ {} postulaciones aceptadas", acceptedCount);
                    }
                    if (rejectedCount > 0) {
                        log.info("❌ {} postulaciones rechazadas", rejectedCount);
                    }
                }
            }

            // 3. Revisar datos del personaje
            systemService.revisedData(character);

        } catch (Exception e) {
            log.error("Error procesando datos pendientes: {}", e.getMessage(), e);
        }
    }

    @Override
    public EnumSystems.AdvanceType getType() {
        return EnumSystems.AdvanceType.NORMAL_DAY;
    }
}
