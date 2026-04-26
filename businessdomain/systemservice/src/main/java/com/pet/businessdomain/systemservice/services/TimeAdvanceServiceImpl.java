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
@RequiredArgsConstructor
public class TimeAdvanceServiceImpl implements TimeAdvanceService {

    private Map<EnumSystems.AdvanceType, AdvanceStrategy> strategies;

    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final MonthEndStrategy monthEndStrategy;
    private final WeekendStrategy weekendStrategy;
    private final NormalDayStrategy normalDayStrategy;
    private final List<AdvanceStrategy> strategyList;

    @PostConstruct
    public void init() {
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

        // Verificar si es fin de mes (último día del mes actual)
        if (isLastDayOfMonth(request)) {
            log.info("📅 Es el último día del mes, ejecutando estrategia compuesta...");
            return executeMonthEndComposite(request);
        }

        // Comportamiento normal (no es fin de mes)
        AdvanceStrategy strategy = strategies.get(request.getAdvanceType());
        if (strategy == null) {
            log.warn("No se encontró estrategia para tipo: {}, usando NORMAL_DAY", request.getAdvanceType());
            strategy = normalDayStrategy;
        }

        return strategy.execute(request);
    }

    /**
     * Ejecuta la estrategia compuesta cuando es el último día del mes
     */
    private TimeAdvanceResponseDTO executeMonthEndComposite(TimeAdvanceRequestDTO request) {
        log.info("=== EJECUTANDO ESTRATEGIA COMPUESTA DE FIN DE MES ===");

        // Obtener la fecha actual para saber qué tipo de día es (normal o fin de semana)
        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        LocalDateTime currentDateTime = systemDto.getActualityAt();

        boolean isWeekend = isWeekend(currentDateTime);

        log.info("📅 Fecha actual (último día del mes): {} - {}",
                currentDateTime.toLocalDate(),
                isWeekend ? "FIN DE SEMANA" : "DÍA NORMAL");

        // 1. Primero ejecutar la estrategia de fin de mes (cierre, nóminas, etc.)
        log.info("📊 1. Ejecutando cierre de mes...");
        TimeAdvanceResponseDTO monthEndResponse = monthEndStrategy.execute(request);

        if (!monthEndResponse.isSuccess()) {
            log.error("❌ Falló el cierre de mes");
            return monthEndResponse;
        }

        // 2. Luego ejecutar la estrategia del día actual (NormalDay o Weekend) para avanzar al día siguiente
        log.info("➡️ 2. Ejecutando estrategia del día actual para avanzar...");

        TimeAdvanceRequestDTO dayRequest = TimeAdvanceRequestDTO.builder()
                .characterId(request.getCharacterId())
                .advanceType(isWeekend ? EnumSystems.AdvanceType.WEEKEND : EnumSystems.AdvanceType.NORMAL_DAY)
                .forceAdvance(false)
                .build();

        AdvanceStrategy dayStrategy = isWeekend ? weekendStrategy : normalDayStrategy;
        TimeAdvanceResponseDTO dayResponse = dayStrategy.execute(dayRequest);

        // 3. Combinar resultados
        TimeAdvanceResponseDTO combined = combineResponses(monthEndResponse, dayResponse);

        log.info("=== ESTRATEGIA COMPUESTA DE FIN DE MES COMPLETADA ===");
        log.info("Nueva fecha: {}", combined.getNewActualityAt());

        return combined;
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

    private boolean isWeekend(LocalDateTime date) {
        if (date == null) return false;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

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
                .title("📆 Cierre de mes completado")
                .description("Se ha procesado el cierre del mes y se ha avanzado al siguiente día.")
                .build());

        return TimeAdvanceResponseDTO.builder()
                .success(monthEnd.isSuccess() && day.isSuccess())
                .message(monthEnd.getMessage() + " | " + day.getMessage())
                .newActualityAt(day.getNewActualityAt())
                .paRemaining(day.getPaRemaining())
                .energyChange(monthEnd.getEnergyChange() + day.getEnergyChange())
                .stressChange(monthEnd.getStressChange() + day.getStressChange())
                .xpEarned(monthEnd.getXpEarned() + day.getXpEarned())
                .events(allEvents)
                .build();
    }
}