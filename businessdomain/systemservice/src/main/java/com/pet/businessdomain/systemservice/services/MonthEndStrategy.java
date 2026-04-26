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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthEndStrategy implements AdvanceStrategy {

    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final SystemService systemService;
    private final BusinessTransactions businessTransactions;
    private final EnergyCalculator energyCalculator;
    private final StressCalculator stressCalculator;

    // XP base por avance mensual
    private static final int BASE_XP_PER_JOB = 100;
    private static final int XP_PER_PERFORMANCE_POINT = 2;
    private static final int XP_PER_SATISFACTION_POINT = 1;
    private static final int BONUS_XP_FOR_ACTIVE = 50;
    private static final int BONUS_XP_FOR_PROMOTION = 200;

    @Override
    public TimeAdvanceResponseDTO execute(TimeAdvanceRequestDTO request) {
        log.info("=== INICIANDO AVANCE DE FIN DE MES ===");
        log.info("Personaje ID: {}", request.getCharacterId());

        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        CharacterDto character = businessTransactions.getPerson(systemDto.getUid());

        LocalDateTime currentDateTime = systemDto.getActualityAt();
        log.info("📅 Fecha/Hora actual: {}", currentDateTime);

        TimeAdvanceResponseDTO response = executeMonthlyAdvance(system, character, currentDateTime);
        processPendingData(character);

        return response;
    }

    private TimeAdvanceResponseDTO executeMonthlyAdvance(SystemEntity system,
                                                         CharacterDto character,
                                                         LocalDateTime currentDateTime) {
        List<DayEventDTO> events = new ArrayList<>();
        int totalXpGained = 0;
        int totalMonthlySalary = 0;
        List<PayrollDTO> createdPayrolls = new ArrayList<>();

        log.info("📊 Procesando avance mensual...");

        // ✅ SIEMPRE avanzar el tiempo, incluso sin trabajos
        LocalDateTime newActuality = currentDateTime.plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // Verificar si tiene trabajos activos
        boolean hasActiveJobs = false;
        List<CharacterJobDTO> activeJobs = new ArrayList<>();

        if (character.getJobs() != null && !character.getJobs().isEmpty()) {
            activeJobs = character.getJobs().stream()
                    .filter(job -> job.getActive() != null && job.getActive())
                    .toList();

            hasActiveJobs = !activeJobs.isEmpty();

            if (hasActiveJobs) {
                log.info("✅ Procesando {} trabajos activos", activeJobs.size());

                // Procesar cada trabajo activo
                for (CharacterJobDTO job : activeJobs) {
                    int jobXp = calculateMonthlyXp(job);
                    totalXpGained += jobXp;

                    int monthlySalary = calculateMonthlySalary(job);
                    totalMonthlySalary += monthlySalary;

                    updateJobStats(job);

                    // ✨ CREAR NÓMINA PARA ESTE TRABAJO
                    PayrollDTO payroll = createPayrollForJob(character, job, monthlySalary,
                            currentDateTime.getYear(), currentDateTime.getMonthValue());
                    monthlySalary = payroll.getNetSalary().intValue();
                    if (payroll != null) {
                        createdPayrolls.add(payroll);
                        log.info("Nómina creada para trabajo ID: {}, Neto: {}€",
                                job.getId(), payroll.getNetSalary());
                    }

                    events.add(DayEventDTO.builder()
                            .type("monthly_work_advance")
                            .title("Avance mensual en " + job.getPositionTitle())
                            .description(String.format(
                                    "Has progresado en tu trabajo como %s en %s. Ganaste %d XP laboral.",
                                    job.getPositionTitle(), job.getCompanyName(), jobXp))
                            .xpEarned(jobXp)
                            .build());

                    events.add(DayEventDTO.builder()
                            .type("monthly_salary")
                            .title("Salario mensual")
                            .description(String.format(
                                    "Has recibido tu salario mensual de %d€ por tu trabajo como %s en %s.\n",
                                    monthlySalary, job.getPositionTitle(), job.getCompanyName()) +
                                    (payroll != null ? String.format(" Nómina generada: %d€ netos",
                                            payroll.getNetSalary().intValue()) : ""))
                            .moneyEarned(monthlySalary)
                            .build());

                    log.info("Trabajo procesado: {} - {} XP, {}€ salario",
                            job.getPositionTitle(), jobXp, monthlySalary);
                }

                // 💰 INGRESAR SALARIO MENSUAL SOLO UNA VEZ
                if (totalMonthlySalary > 0 && character.getAccounts() != null && !character.getAccounts().isEmpty()) {
                    SFinanceAccountResponseDto primaryAccount = character.getAccounts().get(0);
                    primaryAccount.setBalance(primaryAccount.getBalance().add(BigDecimal.valueOf(totalMonthlySalary)));

                    // ✅ Crear registro de ingreso por el total
                    createMonthlySalaryIncomeRecord(character, totalMonthlySalary, activeJobs);

                    // ✅ Marcar nóminas como pagadas
                    for (PayrollDTO payroll : createdPayrolls) {
                        markPayrollAsPaid(payroll.getId(), primaryAccount.getId());
                        log.info("Nómina ID: {} marcada como pagada a cuenta: {}",
                                payroll.getId(), primaryAccount.getId());
                    }

                    log.info("Salario mensual total ingresado: {}€", totalMonthlySalary);
                }

                // Actualizar XP laboral
                if (character.getXpJobs() == null) {
                    character.setXpJobs(0);
                }
                character.setXpJobs(character.getXpJobs() + totalXpGained);

                // Verificar subida de nivel
                int currentLevel = character.getLevel() != null ? character.getLevel() : 1;
                int xpForNextLevel = calculateXpForNextLevel(currentLevel);

                if (character.getXpJobs() >= xpForNextLevel) {
                    character.setLevel(currentLevel + 1);
                    events.add(DayEventDTO.builder()
                            .type("level_up")
                            .title("¡Subida de nivel laboral!")
                            .description(String.format("Has subido al nivel %d en tu carrera profesional.", character.getLevel()))
                            .xpEarned(totalXpGained)
                            .build());
                }
            }
        }

        if (!hasActiveJobs) {
            log.info("⚠️ Personaje sin trabajos activos - Solo avanzando tiempo");
            events.add(DayEventDTO.builder()
                    .type("info")
                    .title("Fin de mes")
                    .description("No tienes trabajos activos. El mes ha terminado sin cambios laborales.")
                    .build());
        }

        // ✅ SIEMPRE guardar cambios del personaje
        businessTransactions.updatePerson(character);

        // ✅ SIEMPRE actualizar sistema
        system.setActualityAt(newActuality);
        system.setUpdateAt(LocalDateTime.now());
        system.setVeces(system.getVeces() + 1);
        system.setPa(Math.max(0, system.getPa() - 1));
        systemRepository.save(system);

        StringBuilder message = new StringBuilder();
        if (hasActiveJobs) {
            message.append(String.format("Avance mensual completado. Has ganado %d XP laboral y %d€ de salario.",
                    totalXpGained, totalMonthlySalary));
            if (!createdPayrolls.isEmpty()) {
                message.append(String.format(" Se han generado %d nóminas.", createdPayrolls.size()));
            }
        } else {
            message.append("Avance mensual completado. No tenías trabajos activos.");
        }

        return buildResponse(system, character, newActuality, events, totalXpGained, totalMonthlySalary, message.toString());
    }

    /**
     * Calcula el salario MENSUAL completo del trabajo
     */
    private int calculateMonthlySalary(CharacterJobDTO job) {
        JobVacancyDTO vacancy = job.getVacancy();
        if (vacancy == null) {
            log.warn("Vacante nula para trabajo: {}", job.getPositionTitle());
            return 0;
        }

        BigDecimal currentSalary = job.getCurrentSalary();

        // Si no hay currentSalary, calcular de la vacante
        if (currentSalary == null) {
            BigDecimal minSalary = vacancy.getMinSalary();
            BigDecimal maxSalary = vacancy.getMaxSalary();

            if (maxSalary != null && minSalary != null) {
                currentSalary = minSalary.add(maxSalary).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            } else if (minSalary != null) {
                currentSalary = minSalary;
            } else {
                log.warn("No se pudo determinar salario para trabajo: {}", job.getPositionTitle());
                return 0;
            }
        }

        // Calcular salario mensual (anual / 12)
        BigDecimal monthlySalary = currentSalary.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        // Bonus por rendimiento (hasta +20%)
        if (job.getPerformance() != null && job.getPerformance() > 0) {
            BigDecimal performanceBonus = monthlySalary.multiply(BigDecimal.valueOf(job.getPerformance()))
                    .divide(BigDecimal.valueOf(500), 2, RoundingMode.HALF_UP);
            monthlySalary = monthlySalary.add(performanceBonus);
            log.info("Bonus por rendimiento ({}%): +{}€", job.getPerformance(), performanceBonus);
        }

        // Bonus por satisfacción (hasta +10%)
        if (job.getSatisfaction() != null && job.getSatisfaction() > 0) {
            BigDecimal satisfactionBonus = monthlySalary.multiply(BigDecimal.valueOf(job.getSatisfaction()))
                    .divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
            monthlySalary = monthlySalary.add(satisfactionBonus);
            log.info("Bonus por satisfacción ({}%): +{}€", job.getSatisfaction(), satisfactionBonus);
        }

        // Bonus por promociones (+10% por cada promoción)
        if (job.getPromotionsReceived() != null && job.getPromotionsReceived() > 0) {
            BigDecimal promotionBonus = monthlySalary.multiply(BigDecimal.valueOf(job.getPromotionsReceived()))
                    .divide(BigDecimal.valueOf(10), 2, RoundingMode.HALF_UP);
            monthlySalary = monthlySalary.add(promotionBonus);
            log.info("Bonus por {} promociones: +{}€", job.getPromotionsReceived(), promotionBonus);
        }

        // Bonus por bonos recibidos
        if (job.getBonusesReceived() != null && job.getBonusesReceived() > 0) {
            BigDecimal bonusAmount = BigDecimal.valueOf(job.getBonusesReceived() * 50);
            monthlySalary = monthlySalary.add(bonusAmount);
            log.info("Bonus especiales: +{}€", bonusAmount);
        }

        log.info("Salario mensual calculado para {}: {}€", job.getPositionTitle(), monthlySalary.intValue());
        return monthlySalary.intValue();
    }

    /**
     * ✅ CORREGIDO: Crea SOLO UN registro de ingreso de salario mensual
     */
    private void createMonthlySalaryIncomeRecord(CharacterDto character, int totalMonthlySalary, List<CharacterJobDTO> jobs) {
        try {
            if (character.getAccounts() == null || character.getAccounts().isEmpty()) {
                log.warn("No hay cuentas bancarias para el personaje");
                return;
            }

            SFinanceAccountResponseDto account = character.getAccounts().get(0);

            // ✅ Crear SOLO UN ingreso por el total del salario mensual
            SIncomeResponseDto incomeDto = new SIncomeResponseDto();
            incomeDto.setExternalRefType("MONTHLY_SALARY");
            incomeDto.setAmount(BigDecimal.valueOf(totalMonthlySalary));

            // Listar los trabajos que contribuyeron al salario
            String jobNames = jobs.stream()
                    .map(job -> String.format("%s en %s", job.getPositionTitle(), job.getCompanyName()))
                    .collect(java.util.stream.Collectors.joining(", "));

            incomeDto.setSource(String.format("Salario mensual - %s", jobNames));
            incomeDto.setExternalRefId(account.getId());
            incomeDto.setCategory(EnumAll.ExpenseCategory.SALARY);
            incomeDto.setFrequency(EnumAll.Frequency.MONTHLY);

            // ✅ Solo una llamada a setIncome
            businessTransactions.setIncome(incomeDto, account.getId());
            log.info("Registro de ingreso mensual creado en sistema financiero por {}€", totalMonthlySalary);

        } catch (Exception e) {
            log.error("Error creando registro de ingreso mensual: {}", e.getMessage(), e);
        }
    }

    private int calculateMonthlyXp(CharacterJobDTO job) {
        int xp = BASE_XP_PER_JOB;

        if (job.getPerformance() != null && job.getPerformance() > 0) {
            xp += job.getPerformance() * XP_PER_PERFORMANCE_POINT;
        }
        if (job.getSatisfaction() != null && job.getSatisfaction() > 0) {
            xp += job.getSatisfaction() * XP_PER_SATISFACTION_POINT;
        }
        if (job.getActive() != null && job.getActive()) {
            xp += BONUS_XP_FOR_ACTIVE;
        }
        if (job.getPromotionsReceived() != null && job.getPromotionsReceived() > 0) {
            xp += job.getPromotionsReceived() * BONUS_XP_FOR_PROMOTION;
        }
        if (job.getBonusesReceived() != null && job.getBonusesReceived() > 0) {
            xp += job.getBonusesReceived() * 50;
        }
        if (job.getStartDate() != null) {
            long monthsActive = ChronoUnit.MONTHS.between(job.getStartDate(), LocalDateTime.now());
            xp += (int) (monthsActive * 10);
        }

        return Math.max(0, xp);
    }

    private void updateJobStats(CharacterJobDTO job) {
        if (job.getPerformance() != null && job.getPerformance() < 100) {
            job.setPerformance(Math.min(100, job.getPerformance() + 5));
        }
        if (job.getStressLevel() != null && job.getStressLevel() > 0) {
            job.setStressLevel(Math.max(0, job.getStressLevel() - 5));
        }
        if (job.getSatisfaction() != null && job.getSatisfaction() < 100) {
            job.setSatisfaction(Math.min(100, job.getSatisfaction() + 3));
        }
    }

    private int calculateXpForNextLevel(int currentLevel) {
        return (int) (100 * currentLevel * 1.5);
    }

    private TimeAdvanceResponseDTO buildResponse(SystemEntity system,
                                                 CharacterDto character,
                                                 LocalDateTime newActuality,
                                                 List<DayEventDTO> events,
                                                 int xpGained,
                                                 int moneyEarned,
                                                 String message) {
        events.add(DayEventDTO.builder()
                .type("system")
                .title("Avance mensual completado")
                .description(String.format("Has completado el avance mensual. Fecha: %s", newActuality.toLocalDate()))
                .xpEarned(xpGained)
                .moneyEarned(moneyEarned)
                .build());

        return TimeAdvanceResponseDTO.builder()
                .success(true)
                .message(message)
                .newActualityAt(newActuality)
                .paRemaining(system.getPa())
                .energyChange(0)
                .stressChange(0)
                .xpEarned(xpGained)
                .statChanges(java.util.Map.of("money", moneyEarned, "xp", xpGained))
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
        return EnumSystems.AdvanceType.MONTH_END;
    }


    /**
     * Crea una nómina para un trabajo específico
     */
    private PayrollDTO createPayrollForJob(CharacterDto character, CharacterJobDTO job,
                                           int monthlySalary, int year, int month) {
        try {
            // Validaciones iniciales
            if (character == null || character.getId() == null) {
                log.error("Character or character ID is null");
                return null;
            }

            if (job == null || job.getId() == null) {
                log.error("Job or job ID is null");
                return null;
            }

            // Obtener ID de la cuenta principal
            Long accountId = null;
            if (character.getAccounts() != null && !character.getAccounts().isEmpty()) {
                accountId = character.getAccounts().get(0).getId();
                log.info("Using account ID: {}", accountId);
            } else {
                log.warn("No accounts found for character {}", character.getId());
            }

            // Calcular neto (deducciones estándar: 20% IRPF + 6.35% Seguridad Social)
            BigDecimal grossSalary = BigDecimal.valueOf(monthlySalary);
            BigDecimal taxDeduction = grossSalary.multiply(BigDecimal.valueOf(0.20));
            BigDecimal socialSecurity = grossSalary.multiply(BigDecimal.valueOf(0.0635));
            BigDecimal totalDeductions = taxDeduction.add(socialSecurity);
            BigDecimal netSalary = grossSalary.subtract(totalDeductions);

            // Calcular horas y días trabajados
            int daysWorked = estimateDaysWorkedInMonth(job, year, month);
            int hoursWorked = daysWorked * 8;

            log.info("Creating payroll for character={}, job={}, salary={}, net={}",
                    character.getId(), job.getId(), monthlySalary, netSalary);

            // Construir el DTO de nómina
            PayrollDTO payroll = PayrollDTO.builder()
                    .characterId(character.getId())
                    .jobId(job.getId())
                    .accountId(accountId)
                    .jobTitle(job.getPositionTitle())
                    .companyName(job.getCompanyName())
                    .year(year)
                    .month(month)
                    .periodStart(LocalDate.of(year, month, 1))
                    .periodEnd(LocalDate.of(year, month, getDaysInMonth(year, month)))
                    .baseSalary(grossSalary)
                    .bonus(BigDecimal.ZERO)
                    .deductions(totalDeductions)
                    .netSalary(netSalary)
                    .hoursWorked(hoursWorked)
                    .daysWorked(daysWorked)
                    .performance(job.getPerformance())
                    .satisfaction(job.getSatisfaction())
                    .status("PENDING")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .processedBy("SYSTEM_MONTH_END")
                    .build();

            // Validar que el payroll tenga datos mínimos
            if (payroll.getCharacterId() == null || payroll.getJobId() == null) {
                log.error("Payroll validation failed: characterId={}, jobId={}",
                        payroll.getCharacterId(), payroll.getJobId());
                return null;
            }

            log.info("Payroll DTO created: {}", payroll);

            // Llamar a BusinessTransactions para crear la nómina
            PayrollDTO created = businessTransactions.createPayroll(payroll);

            if (created != null && created.getId() != null) {
                log.info("✅ Nómina creada exitosamente - ID: {}, Neto: {}€", created.getId(), created.getNetSalary());
                return created;
            } else {
                log.warn("⚠️ No se pudo crear la nómina para trabajo: {}. Created response is null or missing ID",
                        job.getPositionTitle());
                return null;
            }

        } catch (Exception e) {
            log.error("❌ Error creando nómina para trabajo {}: {}", job.getPositionTitle(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * Marca una nómina como pagada
     */
    private void markPayrollAsPaid(Long payrollId, Long accountId) {
        try {
            PayrollDTO updated = businessTransactions.markPayrollAsPaid(payrollId, accountId);
            if (updated != null && "PAID".equals(updated.getStatus())) {
                log.info("Nómina {} marcada como pagada", payrollId);
            }
        } catch (Exception e) {
            log.error("Error marcando nómina {} como pagada: {}", payrollId, e.getMessage());
        }
    }

    /**
     * Estima los días trabajados en el mes según los días laborales del trabajo
     */
    private int estimateDaysWorkedInMonth(CharacterJobDTO job, int year, int month) {
        try {
            JobVacancyDTO vacancy = job.getVacancy();
            if (vacancy == null || vacancy.getWorkingDays() == null) {
                return 20; // Días estimados por defecto
            }

            List<EnumAll.WorkingDay> workingDays = vacancy.getWorkingDays();
            if (workingDays.isEmpty()) {
                return 20;
            }

            // Contar días laborables en el mes
            LocalDate firstDay = LocalDate.of(year, month, 1);
            LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

            int workingDaysCount = 0;
            LocalDate current = firstDay;

            while (!current.isAfter(lastDay)) {
                DayOfWeek dayOfWeek = current.getDayOfWeek();
                boolean isWorkingDay = workingDays.stream().anyMatch(wd ->
                        convertToDayOfWeek(wd) == dayOfWeek);

                if (isWorkingDay) {
                    workingDaysCount++;
                }
                current = current.plusDays(1);
            }

            return workingDaysCount;

        } catch (Exception e) {
            log.warn("Error estimando días trabajados, usando valor por defecto: 20");
            return 20;
        }
    }

    /**
     * Convierte WorkingDay a DayOfWeek
     */
    private DayOfWeek convertToDayOfWeek(EnumAll.WorkingDay workingDay) {
        return switch (workingDay) {
            case MONDAY -> DayOfWeek.MONDAY;
            case TUESDAY -> DayOfWeek.TUESDAY;
            case WEDNESDAY -> DayOfWeek.WEDNESDAY;
            case THURSDAY -> DayOfWeek.THURSDAY;
            case FRIDAY -> DayOfWeek.FRIDAY;
            case SATURDAY -> DayOfWeek.SATURDAY;
            case SUNDAY -> DayOfWeek.SUNDAY;
        };
    }

    /**
     * Obtiene los días del mes
     */
    private int getDaysInMonth(int year, int month) {
        return LocalDate.of(year, month, 1).lengthOfMonth();
    }
}