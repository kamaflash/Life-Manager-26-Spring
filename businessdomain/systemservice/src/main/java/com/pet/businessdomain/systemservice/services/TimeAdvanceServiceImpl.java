package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.DayEventDTO;
import com.pet.businessdomain.shareddto.dto.SystemDto;
import com.pet.businessdomain.shareddto.dto.TimeAdvanceRequestDTO;
import com.pet.businessdomain.shareddto.dto.TimeAdvanceResponseDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TimeAdvanceServiceImpl implements TimeAdvanceService {

    private Map<EnumSystems.AdvanceType, AdvanceStrategy> strategies;
    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final MonthEndStrategy monthEndStrategy;
    private final WeekendStrategy weekendStrategy;
    private final NormalDayStrategy normalDayStrategy;
    private final List<AdvanceStrategy> strategyList;  // ← Para inyectar todas las estrategias

    // Constructor con todas las dependencias
    public TimeAdvanceServiceImpl(SystemRepository systemRepository,
                                  SystemMapper systemMapper,
                                  MonthEndStrategy monthEndStrategy,
                                  WeekendStrategy weekendStrategy,
                                  NormalDayStrategy normalDayStrategy,
                                  List<AdvanceStrategy> strategyList) {
        this.systemRepository = systemRepository;
        this.systemMapper = systemMapper;
        this.monthEndStrategy = monthEndStrategy;
        this.weekendStrategy = weekendStrategy;
        this.normalDayStrategy = normalDayStrategy;
        this.strategyList = strategyList;
    }

    @PostConstruct
    public void init() {
        // Inicializar el mapa de estrategias después de que Spring inyecte todo
        strategies = new EnumMap<>(EnumSystems.AdvanceType.class);
        for (AdvanceStrategy strategy : strategyList) {
            strategies.put(strategy.getType(), strategy);
        }
        log.info("Estrategias cargadas: {}", strategies.keySet());
    }
    @Override
    public TimeAdvanceResponseDTO advance(TimeAdvanceRequestDTO request) {
        log.info("Avanzando tiempo - Tipo: {} - Personaje: {}",
                request.getAdvanceType(), request.getCharacterId());

        // Verificar si es fin de mes y el tipo es NORMAL_DAY
        if (request.getAdvanceType() == EnumSystems.AdvanceType.NORMAL_DAY && isLastDayOfMonth(request)) {
            log.info("📅 Es fin de mes, ejecutando estrategia compuesta...");
            return executeMonthEndComposite(request);
        }

        // Comportamiento normal
        AdvanceStrategy strategy = strategies.get(request.getAdvanceType());
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + request.getAdvanceType());
        }

        return strategy.execute(request);
    }

    /**
     * Ejecuta la estrategia compuesta: primero fin de mes, luego el día correspondiente
     */
    private TimeAdvanceResponseDTO executeMonthEndComposite(TimeAdvanceRequestDTO request) {
        log.info("=== EJECUTANDO ESTRATEGIA COMPUESTA: FIN DE MES + DÍA SIGUIENTE ===");

        // 1. Ejecutar estrategia de fin de mes
        TimeAdvanceResponseDTO monthEndResponse = monthEndStrategy.execute(request);

        if (!monthEndResponse.isSuccess()) {
            log.error("❌ Falló la ejecución del fin de mes");
            return monthEndResponse;
        }

        log.info("✅ Fin de mes completado. Nueva fecha: {}", monthEndResponse.getNewActualityAt());

        // 2. Determinar qué tipo de día es después del fin de mes
        LocalDateTime afterMonthEnd = monthEndResponse.getNewActualityAt();
        boolean isWeekend = isWeekend(afterMonthEnd);

        log.info("📅 Después del fin de mes es {} - {}",
                afterMonthEnd.toLocalDate(),
                isWeekend ? "FIN DE SEMANA" : "DÍA NORMAL");

        // 3. Crear nuevo request para el día siguiente
        TimeAdvanceRequestDTO nextDayRequest = new TimeAdvanceRequestDTO();
        nextDayRequest.setCharacterId(request.getCharacterId());
        nextDayRequest.setAdvanceType(isWeekend ?
                EnumSystems.AdvanceType.WEEKEND : EnumSystems.AdvanceType.NORMAL_DAY);
        nextDayRequest.setForceAdvance(false);

        // 4. Ejecutar la estrategia correspondiente
        AdvanceStrategy nextDayStrategy = isWeekend ? weekendStrategy : normalDayStrategy;
        TimeAdvanceResponseDTO dayResponse = nextDayStrategy.execute(nextDayRequest);

        // 5. Combinar resultados
        return combineResponses(monthEndResponse, dayResponse);
    }

    /**
     * Verifica si el día actual es el último del mes
     */
    private boolean isLastDayOfMonth(TimeAdvanceRequestDTO request) {
        try {
            SystemEntity system = systemRepository.findById(request.getCharacterId())
                    .orElse(null);
            if (system == null) return false;

            SystemDto systemDto = systemMapper.toDto(system);
            LocalDateTime currentDateTime = systemDto.getActualityAt();

            // Verificar si es el último día del mes
            boolean isLastDay = currentDateTime.getDayOfMonth() ==
                    currentDateTime.toLocalDate().lengthOfMonth();

            if (isLastDay) {
                log.info("📅 Detección de fin de mes: {} es el último día del mes",
                        currentDateTime.toLocalDate());
            }

            return isLastDay;

        } catch (Exception e) {
            log.error("Error verificando fin de mes: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si una fecha es fin de semana
     */
    private boolean isWeekend(LocalDateTime date) {
        if (date == null) return false;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * Combina las respuestas de dos estrategias
     */
    private TimeAdvanceResponseDTO combineResponses(TimeAdvanceResponseDTO monthEnd,
                                                    TimeAdvanceResponseDTO day) {
        List<DayEventDTO> allEvents = new ArrayList<>();

        if (monthEnd.getEvents() != null) {
            allEvents.addAll(monthEnd.getEvents());
        }
        if (day.getEvents() != null) {
            allEvents.addAll(day.getEvents());
        }

        // Agregar evento de transición
        allEvents.add(DayEventDTO.builder()
                .type("system")
                .title("🔄 Cierre de mes completado")
                .description(String.format("Se ha procesado el cierre del mes y se ha avanzado al %s",
                        day.getNewActualityAt().toLocalDate()))
                .build());

        int totalEnergyChange = monthEnd.getEnergyChange() + day.getEnergyChange();
        int totalStressChange = monthEnd.getStressChange() + day.getStressChange();
        int totalXpEarned = monthEnd.getXpEarned() + day.getXpEarned();

        String combinedMessage = String.format("%s | %s",
                monthEnd.getMessage(), day.getMessage());

        return TimeAdvanceResponseDTO.builder()
                .success(monthEnd.isSuccess() && day.isSuccess())
                .message(combinedMessage)
                .newActualityAt(day.getNewActualityAt())
                .paRemaining(day.getPaRemaining())
                .energyChange(totalEnergyChange)
                .stressChange(totalStressChange)
                .xpEarned(totalXpEarned)
                .events(allEvents)
                .build();
    }
}