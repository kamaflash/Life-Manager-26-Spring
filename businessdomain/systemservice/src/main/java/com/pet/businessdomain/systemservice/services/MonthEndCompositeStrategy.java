package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.SystemDto;
import com.pet.businessdomain.shareddto.dto.TimeAdvanceRequestDTO;
import com.pet.businessdomain.shareddto.dto.TimeAdvanceResponseDTO;
import com.pet.businessdomain.shareddto.dto.DayEventDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;
import com.pet.businessdomain.systemservice.transactions.BusinessTransactions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthEndCompositeStrategy implements AdvanceStrategy {

    private final MonthEndStrategy monthEndStrategy;
    private final WeekendStrategy weekendStrategy;
    private final NormalDayStrategy normalDayStrategy;
    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final BusinessTransactions businessTransactions;

    @Override
    public TimeAdvanceResponseDTO execute(TimeAdvanceRequestDTO request) {
        log.info("=== INICIANDO AVANCE COMPUESTO: FIN DE MES + DÍA NORMAL/FIN DE SEMANA ===");

        // 1. Obtener sistema y fecha actual
        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        LocalDateTime currentDateTime = systemDto.getActualityAt();

        log.info("📅 Fecha actual antes de fin de mes: {}", currentDateTime);

        // 2. Ejecutar primero la estrategia de fin de mes
        TimeAdvanceResponseDTO monthEndResponse = monthEndStrategy.execute(request);

        log.info("✅ Fin de mes completado. Nueva fecha: {}", monthEndResponse.getNewActualityAt());

        // 3. Determinar qué tipo de día es después del fin de mes
        LocalDateTime afterMonthEnd = monthEndResponse.getNewActualityAt();
        boolean isWeekend = isWeekend(afterMonthEnd);

        log.info("📅 Después del fin de mes es: {} - {}",
                afterMonthEnd.toLocalDate(),
                isWeekend ? "FIN DE SEMANA" : "DÍA NORMAL");

        // 4. Crear un nuevo request para el día siguiente
        TimeAdvanceRequestDTO nextDayRequest = TimeAdvanceRequestDTO.builder()
                .characterId(request.getCharacterId())
                .build();

        // 5. Ejecutar la estrategia correspondiente al tipo de día
        TimeAdvanceResponseDTO dayResponse;
        if (isWeekend) {
            log.info("🎉 Ejecutando estrategia de FIN DE SEMANA");
            dayResponse = weekendStrategy.execute(nextDayRequest);
        } else {
            log.info("📚 Ejecutando estrategia de DÍA NORMAL");
            dayResponse = normalDayStrategy.execute(nextDayRequest);
        }

        // 6. Combinar los resultados
        TimeAdvanceResponseDTO combinedResponse = combineResponses(monthEndResponse, dayResponse);

        log.info("=== AVANCE COMPUESTO COMPLETADO ===");
        log.info("Eventos totales: {}", combinedResponse.getEvents().size());

        return combinedResponse;
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

        // Agregar un evento indicando la transición
        allEvents.add(DayEventDTO.builder()
                .type("system")
                .title("Transición mensual completada")
                .description("Se ha procesado el cierre de mes y se ha avanzado al siguiente día.")
                .build());

        return TimeAdvanceResponseDTO.builder()
                .success(monthEnd.isSuccess() && day.isSuccess())
                .message(monthEnd.getMessage() + " " + day.getMessage())
                .newActualityAt(day.getNewActualityAt())
                .paRemaining(day.getPaRemaining())
                .energyChange(monthEnd.getEnergyChange() + day.getEnergyChange())
                .stressChange(monthEnd.getStressChange() + day.getStressChange())
                .xpEarned(monthEnd.getXpEarned() + day.getXpEarned())
                .events(allEvents)
                .build();
    }

    @Override
    public EnumSystems.AdvanceType getType() {
        return EnumSystems.AdvanceType.MONTH_END;
    }
}